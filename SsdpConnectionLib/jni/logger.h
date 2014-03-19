#ifndef LOGGER_HEADER
#define LOGGER_HEADER

#include "ssdpPlatforms.h"

#if defined (SSDP_APPLE)
//TODO: Define LOGI and LOGE for iOS so all can cross-compile without printf
#define LOGI (FORMAT, ...) printf (FORMAT, __VA_ARGS__)
#define LOGE (FORMAT, ...) printf (FORMAT, __VA_ARGS__)
#define LOGD (FORMAT, ...) printf (FORMAT, __VA_ARGS__)
#define LOGV (FORMAT, ...) printf (FORMAT, __VA_ARGS__)

#elif defined (SSDP_ANDROID)
#include <android/log.h>
#define  LOG_TAG    "ssdpJNI"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__) 
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE,LOG_TAG,__VA_ARGS__)
#endif

#endif
