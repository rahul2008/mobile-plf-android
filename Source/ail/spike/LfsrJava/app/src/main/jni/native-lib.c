#include <stdio.h>
#include <conio.h>
#include <string.h>

/* In-place string obfuscation */

void print_hex_bytes(unsigned char *s, int length)
{
	for (int i = 0; i<length; i++) printf(" %02hhx", s[i]);
	printf("\n");
}

void lfsr16_obfuscate(unsigned char *s, unsigned __int16 length, unsigned __int16 seed)
{
	unsigned __int16 i, lsb;
	unsigned __int16 lfsr = seed;

	for (i = 0; i < length*8; i++)			// Iterating over bits
	{
			lsb = lfsr & 1u;				// Finding least significant bit
			lfsr >>= 1u;					// Right shifting
			if (lsb == 1u)
			{
				s[i / 8] ^= 1 << (i % 8);	// exor "1" with bit position i%8 for byte i/8 of the message
				lfsr ^= 0xB400u;
			}
	}
}

int main()
{
	char message[] = "Hello world";
	unsigned __int16 len = (unsigned __int16)strlen(message);

	printf("Original message: %s\n", message);

	lfsr16_obfuscate((unsigned char*)message, len, 0xACE1u);

	printf("Obfuscated results (in hex):");
	print_hex_bytes((unsigned char*)message, len); // Prints: Obfuscated results (in hex): a9 a1 0e 57 62 9b 00 70 cd af 5e

	lfsr16_obfuscate((unsigned char*)message, len, 0xACE1u);
	printf("Recovered message: %s\n", message);

	getch();
	return 0;
}


