LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := pshmac

LOCAL_CFLAGS := -w
LOCAL_LDLIBS := -llog

LOCAL_SRC_FILES := src/client.c \
				   src/client_enig.c \
				   src/client_sha256.c \
				   src/ps_hmac.c \
				   src/fileio.c \
				   src/PhilipsHmacSha256.c

include $(BUILD_SHARED_LIBRARY)