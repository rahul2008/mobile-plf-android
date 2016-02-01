#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include "ssdp.h"

void callback(char *, int);


int main() {
	printf("Start\n");
	if (0 == openSocket()) {
		registerCallback(&callback);
		startDiscovery();
		sendBroadcast();
	}
	sleep(30);
	stopDiscovery();
	closeSocket();
	printf("End\n");
	return 0;
}

void callback(char * buffer, int bufferLen) {
	printf("Buffer length: %d\n", bufferLen);
	printf("Message: %s\n", buffer);
}
