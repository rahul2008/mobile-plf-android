
cmake_minimum_required(VERSION 3.4.1)


SET(SHARED_LIB_INCLUDE_DIR
    src/main/cpp/client_sha256.h
    src/main/cpp/ps_hmac.h
    src/main/cpp/PhilipsHmacSha256.h
   )

include_directories(${SHARED_LIB_INCLUDE_DIR})

SET(SHARED_LIB_SRC_DIR
       src/main/cpp/client_enig.c
       src/main/cpp/client_sha256.c
       src/main/cpp/ps_hmac.c
       src/main/cpp/PhilipsHmacSha256.c
    )

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Wall -UNDEBUG")



add_library( # Sets the name of the library.
             ps

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.

             ${SHARED_LIB_SRC_DIR}
              )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       ps
                         android
                         app-glue
                       ${log-lib} )



