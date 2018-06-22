#include <stdio.h>
#include <jni.h>
/* In-place string obfuscation */

JNIEXPORT jcharArray JNICALL Java_com_philips_platform_appinfra_aikm_GroomLib_ailGroom(
        JNIEnv *env, jobject obj,
        jcharArray testData, jint testValue) {
    // Array to fill with data
    jcharArray array;

    // Init  java byte array

    jint length = (*env)->GetArrayLength(env, testData);
    array = (*env)->NewCharArray(env, length);
    jchar *charArrayElements = (*env)->GetCharArrayElements(env, testData, 0);

    // Set byte array region with the size of the SendData CommStruct.
    // Now we can send the data back.
    unsigned int i, lsb;
    for (i = 0; i < length * 8; i++)            // Iterating over bits
    {
        lsb = testValue & 1u;                // Finding least significant bit
        testValue >>= 1u;                    // Right shifting
        if (lsb == 1u) {
            unsigned int maxValue = 0x1FCD ^0xABCD;
            testValue ^= maxValue;
            charArrayElements[i / 8] ^=
                    1 << (i % 8);    // exor "1" with bit position i%8 for byte i/8 of the message
        }
    }

    (*env)->SetCharArrayRegion(env, array, 0, length, charArrayElements);

    // Return java array
    return array;
}







