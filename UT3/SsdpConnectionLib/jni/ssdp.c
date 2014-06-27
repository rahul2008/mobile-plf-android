#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <sys/errno.h>
#include <netdb.h>
#include <stdio.h>
#include <pthread.h>
#include "ssdpPlatforms.h"
#include "logger.h"
#include "ssdp.h"
#include "parser.h"

#ifdef SSDP_ANDROID
#include <linux/in.h>
#endif

#ifdef SSDP_APPLE
#include <net/if.h> // For IFF_LOOPBACK
#include <ifaddrs.h>
#endif

#define SSDP_MULTICAST "239.255.255.250"
#define SSDP_PORT "1900"

#define RESPONSE_BUFFER_LEN 512

static char buffer[RESPONSE_BUFFER_LEN+1];

static pthread_t discoveryThread = (pthread_t)NULL;
static char dataPacket_MX3[] = 
    "M-SEARCH * HTTP/1.1\r\n"
    "HOST: 239.255.255.250:1900\r\n"
    "MAN: \"ssdp:discover\"\r\n"
    "ST: urn:philips-com:device:DiProduct:1\r\n"    //Jeroen: Only look for AirPurifiers
    "MX: 3\r\n"
    "\r\n";

static char dataPacket_MX5[] = 
	"M-SEARCH * HTTP/1.1\r\n"
	"HOST: 239.255.255.250:1900\r\n"
	"MAN: \"ssdp:discover\"\r\n"
	"ST: urn:philips-com:device:DiProduct:1\r\n"    //Jeroen: Only look for AirPurifiers
	"MX: 5\r\n"
	"\r\n";

static int listeningSocket;
static int broadcastSocket;

static void (*callback)(SsdpStruct *) = NULL;
static struct addrinfo *broadcastAddress;
//static struct addrinfo *listenAddress;

static void * discoveryThreadRunable(void *sock);
static int setMulticastInterface();
static volatile char terminate = 0;
static int configureSocket(int sock, char broadcast);

void registerCallback(void(*callback_to_register)(SsdpStruct *)) {
	callback = callback_to_register;
}

int startDiscovery() {
	if (NULL == callback) {
		return -1;
	}
	terminate = 0;
	return pthread_create(&discoveryThread, NULL, &discoveryThreadRunable, NULL);
}

static void * discoveryThreadRunable(void * param) {
	struct sockaddr_storage fromAddr;
	socklen_t fromAddrLen = sizeof(fromAddr);

	struct timeval selTimeout;
	fd_set sockSet;
	
	SsdpStruct ssdpStructure;	
	int socket;
	
	while (!terminate) {
		selTimeout.tv_sec = 1;
		selTimeout.tv_usec = 0;

		FD_ZERO(&sockSet);
		FD_SET(listeningSocket, &sockSet);
		FD_SET(broadcastSocket, &sockSet);

		if (select(listeningSocket + 1, &sockSet, NULL, NULL, &selTimeout) > 0) {
			if (FD_ISSET(listeningSocket, &sockSet)) {
				LOGV("M-SEARCH RESPONSE RECEIVED");
				socket = listeningSocket;
			} else if (FD_ISSET(broadcastSocket, &sockSet)) {
				LOGV("SSDP MULTICAST RECEIVED");
				socket = broadcastSocket;				
			} else {
				LOGV("QUITTING BROADCAST THREAD PA");
				return NULL;
			}

			FD_CLR(broadcastSocket, &sockSet);
			FD_CLR(listeningSocket, &sockSet);

			if (!terminate) {
				int numBytes = recvfrom(socket, buffer, RESPONSE_BUFFER_LEN, 0, (struct sockaddr *) &fromAddr, &fromAddrLen);

				if (numBytes <= 0) {
					//SOCKET CLOSED OR ERROR!!!
					LOGE("QUITTING BROADCAST THREAD PB");
					return NULL;
				} else {
					buffer[numBytes]='\0';
					LOGD("BUFFER device detials...:\n%s", buffer);
					if (0 == parseSSDP(buffer, numBytes, &ssdpStructure)) {
						callback(&ssdpStructure);
					}
				}
			}
		}
	}
	//THREAD TERMINATED
	LOGE("QUITTING DISCOVERY THREAD PC");
	return NULL;
}

void closeSocket() {
	close(listeningSocket);
	close(broadcastSocket);
	shutdown(listeningSocket, SHUT_RDWR);
	shutdown(broadcastSocket, SHUT_RDWR);
	freeaddrinfo(broadcastAddress);
}

void stopDiscovery() {
	if ((pthread_t)NULL != discoveryThread) {
		terminate = 1;
		pthread_join(discoveryThread, NULL);
		discoveryThread = (pthread_t)NULL;
	}
}

int openSocket() {
	struct addrinfo addrCriteria;
	memset(&addrCriteria, 0, sizeof(addrCriteria));

	addrCriteria.ai_family		= PF_INET; 
	addrCriteria.ai_socktype	= SOCK_DGRAM;
	addrCriteria.ai_protocol	= IPPROTO_UDP; 
	addrCriteria.ai_flags		|= AI_NUMERICHOST;	 

	int rtnVal = getaddrinfo(SSDP_MULTICAST, SSDP_PORT, &addrCriteria, &broadcastAddress);
	if (0 != rtnVal) {
		printf("getaddrinfo failed\n");
		return -1;
	}

	broadcastSocket = socket(broadcastAddress->ai_family, broadcastAddress->ai_socktype, broadcastAddress->ai_protocol);
	if (broadcastSocket < 0) {
		printf("socket failed\n");
		return -1;
	}

	listeningSocket = socket(broadcastAddress->ai_family, broadcastAddress->ai_socktype, broadcastAddress->ai_protocol);
	if (listeningSocket < 0) {
		printf("socket failed\n");
		return -1;
	}

	if (0 != configureSocket(listeningSocket, 1)) {
		return -1;	
	};

	if (0 != configureSocket(broadcastSocket, 0)) {
		return -1;	
	};

	setMulticastInterface(listeningSocket);
    
	return 0;
}


static int configureSocket(int sock, char listeningSocket) {
	int rtnVal = 0;

	int reuseAddr = 1;
	rtnVal = setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &reuseAddr, sizeof(reuseAddr)); 
	if ( rtnVal < 0) {
		printf("setsockopt failed %d\n", rtnVal);
		return -1;
	}
    
    unsigned char mttl = 2;
    rtnVal = setsockopt(sock, IPPROTO_IP, IP_MULTICAST_TTL, &mttl, sizeof(mttl));
    if ( rtnVal < 0) {
		printf("IP_MULTICAST_TTL failed %d\n", rtnVal);
		return -1;
	}

    unsigned char loopack = 0;
    rtnVal = setsockopt(sock, IPPROTO_IP, IP_MULTICAST_LOOP, &loopack, sizeof(loopack));
    if ( rtnVal < 0) {
		printf("IP_MULTICAST_LOOP failed %d\n", rtnVal);
		return -1;
	}
    
	if (listeningSocket) {
		rtnVal = bind(sock, broadcastAddress->ai_addr, broadcastAddress->ai_addrlen);
		if ( rtnVal < 0) {
			printf("bind failed %d\n", rtnVal);
			return -1;
		}
	}
}


int sendBroadcastMX3() {
	int rtnVal = 0;
    ssize_t numBytes = sendto(broadcastSocket, dataPacket_MX3, strlen(dataPacket_MX3), 0, broadcastAddress->ai_addr, broadcastAddress->ai_addrlen);
	if (numBytes < 0) {
		rtnVal = -1;
		printf("sendBroadcast failed\n");
        perror("=>");
	}
	return rtnVal;
}

int sendBroadcastMX5() {
	int rtnVal = 0;
    ssize_t numBytes = sendto(broadcastSocket, dataPacket_MX5, strlen(dataPacket_MX5), 0, broadcastAddress->ai_addr, broadcastAddress->ai_addrlen);
	if (numBytes < 0) {
		rtnVal = -1;
		printf("sendBroadcast failed\n");
        perror("=>");
	}
	return rtnVal;
}

//---------------------------Private functions ----------------------------------------------------
//Sets the active network interface for the socket multicast
int setMulticastInterface(int sock) {
#if defined (SSDP_APPLE)
    //iOS requires to set network interface where to multicast
    struct ifaddrs *addresses;
    struct ifaddrs *cursor;
    
    if (getifaddrs(&addresses) != 0) return -1;
    
    cursor = addresses;
    while (cursor != NULL) {
        if (cursor -> ifa_addr -> sa_family == AF_INET
            && !(cursor -> ifa_flags & IFF_LOOPBACK)) // Ignore the loopback interface
        {
            // Check for WiFi adapter (named en0 always)
            if (!strcmp(cursor -> ifa_name, "en0") && cursor -> ifa_flags & IFF_MULTICAST ) {
                //the interface address
                struct sockaddr_in* ifaddr = ((struct sockaddr_in *)cursor->ifa_addr);
                
                struct ip_mreq joinRequest;
                joinRequest.imr_multiaddr = ((struct sockaddr_in *) broadcastAddress->ai_addr)->sin_addr;
                joinRequest.imr_interface.s_addr = ifaddr->sin_addr.s_addr; // Choose wifi for iOS
                 
                if (setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &joinRequest, sizeof(joinRequest)) < 0) {
                    printf("IP_ADD_MEMBERSHIP failed\n");
                    return -1;
                }
                 
                if (setsockopt(sock, IPPROTO_IP, IP_MULTICAST_IF, &ifaddr->sin_addr, sizeof(ifaddr->sin_addr)) < 0) {
                    printf("IP_MULTICAST_IF failed\n");
                    return -1;
                }
                break;
            }
        }
        cursor = cursor -> ifa_next;
    }
    
    freeifaddrs(addresses);
    
    return 0;
#elif defined (SSDP_ANDROID)
    //Android just lets system to choose interface
    struct ip_mreq joinRequest;
	joinRequest.imr_multiaddr = ((struct sockaddr_in *) broadcastAddress->ai_addr)->sin_addr;
	joinRequest.imr_interface.s_addr = 0; // Let the system choose the i/f
	
	if (setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &joinRequest, sizeof(joinRequest)) < 0) {
		LOGE("IP_ADD_MEMBERSHIP failed\n");
		return -1;
	}
    
    return 0;
#endif
}
