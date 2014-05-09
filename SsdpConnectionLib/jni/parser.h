#ifndef PARSER_HEADER
#define PARSER_HEADER

typedef struct {
	char * NTS;
	char * USN;
	char * BOOT_ID; //Manzer
	char * SERVER; //Manzer
	char * LOCATION;
} SsdpStruct;

// Parses ssdp buffer and put results into ssdpStruct
// Returns -1 if any error
//

int parseSSDP(char * buffer, int bufferSize, SsdpStruct * ssdpStruct);

#endif
