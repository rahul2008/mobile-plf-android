#include <stdio.h>
#include <stdint.h>
//#include <conio.h>
#include <string.h>
#include <jni.h>

/* In-place string obfuscation */

void print_hex_bytes(unsigned char *s, int length) {
    printf("%s", s);
    for (int i = 0; i < length; i++) printf(" %02hhx", s[i]);
    printf("\n");
}


JNIEXPORT jchar * JNICALL
Java_com_philips_platform_appinfra_keybag_KeyBagLib_passingDataToJni(JNIEnv *env, jobject instance,jchar *test,
                                                 int lfsr, jstring stringValue_,jcharArray array,int length) {


    const jcharArray *array1 = (jcharArray const *) (*env)->GetCharArrayElements(env, array, 0);
//    const char *CharValueUTF = (*env)->GetStringUTFChars(env, array1, 0);
//    const char *CharValueUTF = (*env)->GetStringUTFChars(env, array1, 0);


    const jchar *stringValue = (*env)->GetStringChars(env, stringValue_, 0);
    const char *stringValueUTF = (*env)->GetStringUTFChars(env, stringValue_, 0);

    unsigned int i, lsb;
//    char s = (char) stringValueUTF;
    for (i = 0; i < length*8; i++)			// Iterating over bits
    {
        lsb = lfsr & 1u;				// Finding least significant bit
        lfsr >>= 1u;					// Right shifting
        if (lsb == 1u)
        {
            lfsr ^= 0xB400u;
            test[i / 8] ^= 1 << (i % 8);	// exor "1" with bit position i%8 for byte i/8 of the message
        }
    }
    return test;
}

JNIEXPORT jcharArray Java_com_philips_platform_appinfra_keybag_KeyBagLib_lfsrObfuscate(char s[], unsigned int length, unsigned int seed) {
    unsigned int i, lsb;
    unsigned int lfsr = seed;

    for (i = 0; i < length*8; i++)			// Iterating over bits
    {
        lsb = lfsr & 1u;				// Finding least significant bit
        lfsr >>= 1u;					// Right shifting
        if (lsb == 1u)
        {
            lfsr ^= 0xB400u;
            s[i / 8] ^= 1 << (i % 8);	// exor "1" with bit position i%8 for byte i/8 of the message
        }
    }
    return s;
}

/*JNIEXPORT jcharArray Java_com_philips_platform_appinfra_keybag_KeyBagLib_lfsrObfuscate2(JNIEnv *env, jobject instance,
                                                                                        jcharArray s, unsigned int length, unsigned int seed) {
    unsigned int i, lsb;
    unsigned int lfsr = seed;

    for (i = 0; i < length*8; i++)			// Iterating over bits
    {
        lsb = lfsr & 1u;				// Finding least significant bit
        lfsr >>= 1u;					// Right shifting
        if (lsb == 1u)
        {
            lfsr ^= 0xB400u;
            s[i / 8] ^= 1 << (i % 8);	// exor "1" with bit position i%8 for byte i/8 of the message
        }
    }
    return s;
}*/

JNIEXPORT jstring JNICALL
Java_com_philips_platform_appinfra_keybag_KeyBagLib_getMsgFromJni(JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "Hello World NDK");
}

JNIEXPORT jcharArray Java_com_philips_platform_appinfra_keybag_KeyBagLib_lfsrObfuscateTest(JNIEnv *env, jcharArray array,unsigned int length, unsigned int seed) {
    unsigned int i, lsb;
    unsigned int lfsr = seed;
    char  s[] = {};
    for (i = 0; i < length*8; i++)			// Iterating over bits
    {
        lsb = lfsr & 1u;				// Finding least significant bit
        lfsr >>= 1u;					// Right shifting
        if (lsb == 1u)
        {
            lfsr ^= 0xB400u;
            s[i / 8] ^= 1 << (i % 8);	// exor "1" with bit position i%8 for byte i/8 of the message
        }
    }
    return s;
}

Java_com_philips_platform_appinfra_keybag_KeyBagLib_lfsrMain(char* message) {
//    char message[] = "Raja Ram Mohan Roy";
    unsigned int len = (unsigned int)strlen(message);
    printf("Original message: %s\n", message);

    char *obfuscated =  Java_com_philips_platform_appinfra_keybag_KeyBagLib_lfsrObfuscate((char*)message, len, 0xACE1u);
    printf("Obfuscated results (in hex):");
    print_hex_bytes((unsigned char*)obfuscated, len); // Prints: Obfuscated results (in hex) ex: a9 a1 0e 57 62 9b 00 70 cd af 5e

    char *recovered = Java_com_philips_platform_appinfra_keybag_KeyBagLib_lfsrObfuscate((char*)message, len, 0xACE1u);
    printf("Recovered message: %s\n", recovered);

}






