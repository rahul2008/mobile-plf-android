#include "ssdp.h"
#include <jni.h>
#include "logger.h"
#include "parser.h"

void callback(SsdpStruct *);

static JavaVM* virutalMachine;

typedef struct {
	jclass     clazz;
	jmethodID  methodNotifyDiscovery;
	jobject    jSSDP;
} SsdpContext;

static SsdpContext context;

jint
JNI_OnLoad(JavaVM* vm, void* reserved) {
	virutalMachine = vm;
	LOGE("JNI_OnLoad called");
	return JNI_VERSION_1_2;
}

jint
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_openSocket( JNIEnv*  env, jobject  this) {
	LOGE("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_openSocket");
    return openSocket();
}

void
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_closeSocket( JNIEnv*  env, jobject  this) {
	LOGE("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_closeSocket");
    return closeSocket();
}

jint
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_startDiscovery( JNIEnv*  env, jobject  this) {
	LOGE("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_startDiscovery");
    return startDiscovery();
}

void
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_stopDiscovery( JNIEnv*  env, jobject  this) {
	LOGE("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_stopDiscovery");
    return stopDiscovery();
}

jint
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_sendBroadcastMX3( JNIEnv*  env, jobject  this) {
	//LOGI("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_sendBroadcastMX3");
    return sendBroadcastMX3();
}

jint
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_sendBroadcastMX5( JNIEnv*  env, jobject  this) {
	//LOGI("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_sendBroadcastMX5");
    return sendBroadcastMX5();
}

jint
Java_com_philips_cl_di_common_ssdp_lib_SsdpService_registerListener( JNIEnv  *env, jobject this) {
	LOGE("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_registerListener");
	context.clazz = (*env)->GetObjectClass(env, this);
	context.methodNotifyDiscovery = (*env)->GetMethodID( env, context.clazz, "ssdpCallback", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V" );
	context.jSSDP			= (*env)->NewGlobalRef(env,this);
	
	if (0 == context.methodNotifyDiscovery) {
		LOGE("Java_com_philips_cl_di_common_ssdp_lib_SsdpService_registerListener: callback not found");
		return -1;
	}
	
	registerCallback(&callback);
	return 0;
}

void callback(SsdpStruct * ssdpStruct) {
	JNIEnv *env;
	(*virutalMachine)->AttachCurrentThread( virutalMachine, &env, 0 );
    (*env)->CallVoidMethod(env, context.jSSDP, context.methodNotifyDiscovery,
								(*env)->NewStringUTF(env, (*ssdpStruct).NTS),
								(*env)->NewStringUTF(env, (*ssdpStruct).USN),
								(*env)->NewStringUTF(env, (*ssdpStruct).LOCATION),
    							(*env)->NewStringUTF(env, (*ssdpStruct).BOOT_ID));
	(*virutalMachine)->DetachCurrentThread(virutalMachine);
}

