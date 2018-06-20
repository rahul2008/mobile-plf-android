#include <stdio.h>
#include <stdint.h>
//#include <conio.h>
#include <string.h>

/* In-place string obfuscation */

void print_hex_bytes(unsigned char *s, int length)
{
    //printf("Storage size for char : %lu \n", sizeof(char));
    
    for (int i = 0; i<length; i++) printf(" %02hhx", s[i]);
    printf("\n");
}

char * lfsr16_obfuscate( char *s, unsigned int length, unsigned int seed)
{
    unsigned int i, lsb;
    unsigned int lfsr = seed;
    
    for (i = 0; i < length*8; i++)          // Iterating over bits
    {
        lsb = lfsr & 1u;                // Finding least significant bit
        lfsr >>= 1u;                    // Right shifting
        if (lsb == 1u)
        {
            lfsr ^= 0xB400u;
            s[i / 8] ^= 1 << (i % 8);   // exor "1" with bit position i%8 for byte i/8 of the message
        }
    }
    
    return s;
}

void lfsrMain()
{
    char message[] = "Raja Ram Mohan Roy";
    unsigned int len = (unsigned int)strlen(message);
    printf("Original message: %s\n", message);
    
    char *obfuscated =  lfsr16_obfuscate((char*)message, len, 0xACE1u);
    printf("Obfuscated results (in hex):");
    print_hex_bytes((unsigned char*)obfuscated, len); // Prints: Obfuscated results (in hex) ex: a9 a1 0e 57 62 9b 00 70 cd af 5e
    
    char *recovered = lfsr16_obfuscate((char*)message, len, 0xACE1u);
    printf("Recovered message: %s\n", recovered);
}








