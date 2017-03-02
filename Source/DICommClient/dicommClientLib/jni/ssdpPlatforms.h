#ifndef MyRemoteR2_ssdpPlatforms_h
#define MyRemoteR2_ssdpPlatforms_h

//APPLE MACOSX / IOS
#if defined (__APPLE__) && defined(__MACH__)  || defined (TARGET_IPHONE_SIMULATOR) || defined (TARGET_OS_IPHONE)
#define SSDP_APPLE
//ANDROID
#elif defined (ANDROID)
#define SSDP_ANDROID
#endif

#endif
