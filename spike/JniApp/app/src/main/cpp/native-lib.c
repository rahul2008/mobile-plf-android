#include <stdio.h>
#include <jni.h>

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

//    char *buf = (char*)malloc(10);
//    strcpy(stringValueUTF, "123456789"); // with the null terminator the string adds up to 10 bytes
    jstring jstrBuf = (*env)->NewStringUTF(env, stringValueUTF);
//    return (*env)->NewStringUTF(env,"Ravi kiran");
    return jstrBuf;
}


JNIEXPORT jstring JNICALL
Java_com_philips_platform_appinfra_keybag_KeyBagLib_getMsgFromJni(JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "Hello World NDK");
}







