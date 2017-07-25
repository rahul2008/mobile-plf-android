#include <stdio.h>
#include <jni.h>
#include <string.h>
#include <stdlib.h> 
/* In-place string obfuscation */

void print_hex_bytes(char *s, int length) {
    printf("%s", s);
    for (int i = 0; i < length; i++) {
        printf(" %02hhx", s[i]);

        printf("\n");
        char string = s[i];

        printf("%c", string);
    }
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
    return (*env)->NewStringUTF(env, "b3a5085a2de916729f8e55955ba482656cfc");
}

JNIEXPORT jcharArray JNICALL
Java_com_philips_platform_appinfra_keybag_KeyBagLib_passingCharDataToJni(JNIEnv *env, jobject obj) {
    jcharArray message = "Hello world";
    jint length = (jint)strlen(message);
    char NewBuffer = 'a';
     (*env)->SetCharArrayRegion(env, message, 0, length, (jchar *) NewBuffer);
    return message;
//    return (*env)->NewStringUTF(env, "b3a5085a2de916729f8e55955ba482656cfc");
}

JNIEXPORT jcharArray JNICALL Java_com_philips_platform_appinfra_keybag_KeyBagLib_ConvertString(
        JNIEnv *env, jobject obj,
        jstring Buffer, int length, jint lfsr) {
    // Array to fill with data
    jcharArray Array;

    // Init  java byte array
    Array = (*env)->NewCharArray(env, length);
    jchar *NewBuffer = (jchar *) (*env)->GetStringUTFChars(env, Buffer, 0);


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

    (*env)->SetCharArrayRegion(env, Array, 0, length, NewBuffer);

    // Return java array
    return Array;
}







