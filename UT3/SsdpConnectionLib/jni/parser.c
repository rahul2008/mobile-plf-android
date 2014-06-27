#include "ssdpPlatforms.h"
#include "parser.h"
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "logger.h"

#define STR_NT			"NT:"
#define STR_NT_ROOT		"urn:philips-com:device:DiProduct:1"    //Jeroen: Only continue if AirPurifiers
#define STR_NTS			"NTS:"
#define STR_NTS_ALIVE	"ssdp:alive"
#define STR_NTS_BYEBYE	"ssdp:byebye"
#define STR_USN			"USN:"
#define STR_LOCATION	"LOCATION:"
#define STR_BOOT_ID	    "BOOTID.UPNP.ORG:" //Manzer
#define STR_SERVER	    "SERVER:" //Manzer


// Removes spaces from beggining and replaces '\n' with '\0'
//

static void toUpperCase(char * buffer) {
	while (*buffer) {
		if (*buffer >= 'a' && *buffer <= 'z') {
			*buffer -= 32;
		}
		
		if (*buffer == ':') {
			while(*buffer++ && *buffer != '\n');
		} else {
			buffer++;
		}
	}
}

static int createNullTermString(char ** buffer) {
	if (NULL == buffer || NULL == *buffer) {
		return -1;
	}
	char * lineEnd = strchr(*buffer, '\n');
	if (NULL != lineEnd) {
		*lineEnd = '\0';
		lineEnd = strchr(*buffer, '\r');
		if (NULL != lineEnd) {
			*lineEnd = '\0';
		}
		
		while (**buffer && **buffer == ' ') {
			(*buffer)++;
		}
		return 0;
	}
	return -1;
}

// Parses ssdp buffer and put results into ssdpStruct
// Returns -1 if any error
//

int parseSSDP(char * buffer, int bufferSize, SsdpStruct * ssdpStruct) {
    //Just uncomment to see what's received in socket before  filtering
    //printf("SSDP CALLBACK:\n%s\n",buffer);
	//check for valid SSDP packet
	LOGD("SSDP CALLBACK:\n%s\n", buffer);
	toUpperCase(buffer);

	char* notify = strstr(buffer, "NOTIFY * HTTP/1.1");
	char* reply = strstr(buffer, "HTTP/1.1 200 OK");

	memset(ssdpStruct, 0, sizeof(SsdpStruct));
	
	if (NULL != notify || NULL != reply) {		
		//check for root packet
		if (NULL != strstr(buffer, STR_NT_ROOT)) {	
			//check for ssdp:alive or ssdp:byebye
			(*ssdpStruct).NTS = strstr(buffer, STR_NTS_ALIVE);
			if (NULL == (*ssdpStruct).NTS) {
				(*ssdpStruct).NTS = strstr(buffer, STR_NTS_BYEBYE);
			}
			
			//check for USN: and LOCATION:
			(*ssdpStruct).USN = strstr(buffer, STR_USN);
			(*ssdpStruct).LOCATION = strstr(buffer, STR_LOCATION);
			(*ssdpStruct).BOOT_ID = strstr(buffer, STR_BOOT_ID); //Manzer
			(*ssdpStruct).SERVER = strstr(buffer, STR_SERVER); //Manzer
			if (NULL == (*ssdpStruct).USN || NULL == (*ssdpStruct).LOCATION) {
				LOGE("Missing USN || LOCATION");
				return -1;
			}

			//Null terminate all strings and remove prefixes if necessary
			createNullTermString(&(*ssdpStruct).NTS);
			(*ssdpStruct).USN	+= strlen(STR_USN);
			createNullTermString(&(*ssdpStruct).USN);
			(*ssdpStruct).LOCATION += strlen(STR_LOCATION);			
			createNullTermString(&(*ssdpStruct).LOCATION);
			(*ssdpStruct).SERVER += strlen(STR_SERVER);	//Manzer
			createNullTermString(&(*ssdpStruct).SERVER); //Manzer
			if (NULL != (*ssdpStruct).BOOT_ID) {
				(*ssdpStruct).BOOT_ID += strlen(STR_BOOT_ID);	//Manzer
				createNullTermString(&(*ssdpStruct).BOOT_ID); //Manzer
			}
			return 0;
		}
	}
	return -1;
}
