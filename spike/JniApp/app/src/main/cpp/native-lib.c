#include <stdio.h>
#include <jni.h>
#include <string.h>

/* In-place string obfuscation */

void print_hex_bytes(unsigned char *s, int length) {
    printf("%s", s);
    for (int i = 0; i < length; i++) printf(" %02hhx", s[i]);
    printf("\n");
}


JNIEXPORT jstring JNICALL
Java_com_philips_platform_appinfra_keybag_KeyBagLib_passingDataToJni(JNIEnv *env, jobject instance,
                                                                     jstring stringValue_,
                                                                     int length, int lfsr) {


    char *stringValueUTF = (char *) (*env)->GetStringUTFChars(env, stringValue_, 0);
    unsigned int i, lsb;
    for (i = 0; i < length * 8; i++)            // Iterating over bits
    {
        lsb = lfsr & 1u;                // Finding least significant bit
        lfsr >>= 1u;                    // Right shifting
        if (lsb == 1u) {
            lfsr ^= 0xB400u;
            stringValueUTF[i / 8] ^=
                    1 << (i % 8);    // exor "1" with bit position i%8 for byte i/8 of the message
        }
    }
//    (*env)->ReleaseStringUTFChars(env, stringValue_, stringValueUTF);
//    (*env)->ReleaseStringUTFChars(env, stringValue_, stringValueUTF);
//    return (*env)->NewStringUTF(env, stringValueUTF);

    strcpy(stringValueUTF, "123456789"); // with the null terminator the string adds up to 10 bytes
    return (*env)->NewStringUTF(env,"Ravi kiran");
}


JNIEXPORT jstring JNICALL
Java_com_philips_platform_appinfra_keybag_KeyBagLib_getMsgFromJni(JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "Hello World NDK");
}

JNIEXPORT jbyteArray JNICALL Java_com_philips_platform_appinfra_keybag_KeyBagLib_ConvertString(
        JNIEnv *env, jobject obj,
        jstring Buffer, int length, int lfsr) {
    // Array to fill with data
    jbyteArray Array;

    // Init  java byte array
    Array = (*env)->NewByteArray(env, length);
    char *NewBuffer = (char *) (*env)->GetStringUTFChars(env, Buffer, 0);


    // Set byte array region with the size of the SendData CommStruct.
    // Now we can send the data back.
    unsigned int i, lsb;
    for (i = 0; i < length * 8; i++)            // Iterating over bits
    {
        lsb = lfsr & 1u;                // Finding least significant bit
        lfsr >>= 1u;                    // Right shifting
        if (lsb == 1u) {
            lfsr ^= 0xB400u;
            NewBuffer[i / 8] ^=
                    1 << (i % 8);    // exor "1" with bit position i%8 for byte i/8 of the message
        }
    }

    (*env)->SetByteArrayRegion(env, Array, 0, length, (jbyte *) NewBuffer);

    (*env)->ReleaseStringUTFChars(env, Buffer, NewBuffer);
    // Return java array
    return Array;
}







