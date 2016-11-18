#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

#include "PhilipsHmacSha256.h"
#include "client_sha256.h"
#include <android/log.h>

#define APPNAME "WhiteBoxSignInAPIDemo"

JNIEXPORT jbyteArray JNICALL Java_com_philips_platform_appinfra_whiteboxapi_GenerateHmacLib_pshmac(JNIEnv *env, jclass thisClass, jbyteArray keyJNI, jbyteArray messageJNI)
{
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "####### JNI Called 1");
	// Convert the incoming JNI jbytearrays to jboolean[] (mapped to unsigned char in C)
    	unsigned char *key = (*env)->GetByteArrayElements(env, keyJNI, NULL);
    	if (NULL == key) return NULL;
    	unsigned char *message = (*env)->GetByteArrayElements(env, messageJNI, NULL);
    	if (NULL == message) return NULL;
    	jsize msg_length = (*env)->GetArrayLength(env, messageJNI);

    	// Setup inner and outer H state. Convert unsigned char sequence
      // to unsigned int sequence

      sha256_state_t inner;
      init_sha256(&inner);
      inner->encrypted = 1;
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "####### 2");
      for (int i = 0; i < 8; i ++)
        {
          unsigned int temp = 0;
          for (int j = 0 ; j < 4; j ++)
            {
              temp = (temp << 8) | key[4*i + j];
            }
          inner->H[i] = temp;
        }
      inner->processed_length=64;
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "####### 3");

      sha256_state_t outer;
      init_sha256(&outer);
      outer->encrypted = 1;
      for (int i = 0; i < 8; i ++)
        {
          unsigned int temp = 0;
          for (int j = 0 ; j < 4; j ++)
            {
    					temp = (temp << 8) | key[32+(4*i) + j];
            }
          outer->H[i] = temp;
        }
      outer->processed_length=64;

      process_sha256(inner,message,msg_length);
      finish_sha256(inner);

    	// Release resources
    	(*env)->ReleaseByteArrayElements(env, keyJNI, key, 0);
    	(*env)->ReleaseByteArrayElements(env, messageJNI, message, 0);
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "####### 4");
      unsigned char *iresult;
      int iresult_len;
      extract_data_sha256(inner,&iresult,&iresult_len);

      process_sha256(outer,iresult,iresult_len);
      finish_sha256(outer);

      extract_data_sha256(outer,&iresult,&iresult_len);
        __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "####### ");
    	// Create a jbyteArray containing the signature
    	jbyteArray resultJNI = (*env)->NewByteArray(env, 32);
    	if (NULL == resultJNI) return NULL;
    		(*env)->SetByteArrayRegion(env, resultJNI, 0 , 32, iresult);

    	return resultJNI;
}
