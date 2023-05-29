APP_ABI := armeabi-v7a,arm64-v8a,x86,x86_64
APP_STL := c++_shared
APP_CPPFLAGS += -std=c++17 -DAAPLUS_NO_VSOP87

APP_PLATFORM := android-23
APP_BUILD_SCRIPT := Android.mk
