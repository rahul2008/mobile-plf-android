LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := parser
LOCAL_SRC_FILES := parser.c
include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := ssdp
LOCAL_SRC_FILES := ssdp.c

include $(BUILD_STATIC_LIBRARY)

# second lib, which will depend on and include the first one
#
include $(CLEAR_VARS)

LOCAL_LDLIBS := -llog
LOCAL_CFLAGS := \
	-DHAVE_SIGNAL_H \
	-DDIRECT_BUILD_NO_PTHREAD_CANCEL=1 \
	-Wall

LOCAL_MODULE    := ssdpJNI
LOCAL_SRC_FILES := ssdpJNI.c

LOCAL_STATIC_LIBRARIES := ssdp parser

include $(BUILD_SHARED_LIBRARY)
