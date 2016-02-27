LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := libexpat
LOCAL_CFLAGS := -DHAVE_MEMMOVE
LOCAL_SRC_FILES := libs/$(TARGET_ARCH_ABI)/libexpat.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libstrophe
LOCAL_SRC_FILES := libs/$(TARGET_ARCH_ABI)/libstrophe.a
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := libmessenger
LOCAL_SRC_FILES := libs/$(TARGET_ARCH_ABI)/libmessenger.a
LOCAL_STATIC_LIBRARIES := libstrophe libexpat
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := messengerq

LOCAL_C_INCLUDES := $(COMPONENTS_PATH)/include/
LOCAL_SRC_FILES := messengerq_jni.cpp
LOCAL_LDLIBS := -llog
LOCAL_STATIC_LIBRARIES := libexpat libmessenger libstrophe
ifeq ($(strip $(NDK_DEBUG)),1)
	LOCAL_CFLAGS += -D_DEBUG
endif

include $(BUILD_SHARED_LIBRARY)

