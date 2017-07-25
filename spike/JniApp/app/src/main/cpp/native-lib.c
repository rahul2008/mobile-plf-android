#include <stdio.h>
#include <jni.h>
/* In-place string obfuscation */

JNIEXPORT jcharArray JNICALL Java_com_philips_platform_appinfra_keybag_KeyBagLib_obfuscateDeObfuscate(
        JNIEnv *env, jobject obj,
        jcharArray data, jint seed) {
    // Array to fill with data
    jcharArray array;

    // Init  java byte array

    jint length = (*env)->GetArrayLength(env, data);
    array = (*env)->NewCharArray(env, length);
    jchar *charArrayElements = (*env)->GetCharArrayElements(env, data, 0);

    // Set byte array region with the size of the SendData CommStruct.
    // Now we can send the data back.
    unsigned int i, lsb;
    for (i = 0; i < length * 8; i++)            // Iterating over bits
    {
        lsb = seed & 1u;                // Finding least significant bit
        seed >>= 1u;                    // Right shifting
        if (lsb == 1u) {
            seed ^= 0xB400u;
            charArrayElements[i / 8] ^=
                    1 << (i % 8);    // exor "1" with bit position i%8 for byte i/8 of the message
        }
    }

    (*env)->SetCharArrayRegion(env, array, 0, length, charArrayElements);

    // Return java array
    return array;
}







