#ifndef PHOTOEDIT_PICWARP_H
#define PHOTOEDIT_PICWARP_H
#include <jni.h>

jint initArray(JNIEnv *env, jobject obj);

jintArray warpPhotoFromC(JNIEnv* env, jobject obj, jintArray imagearr, jint height, jint width,
                         jdouble r, jdouble orig_x, jdouble orig_y, jdouble cur_x, jdouble cur_y);



JNINativeMethod gMethods[] = {
        {"initArray","()I", (void *) initArray},
        {"warpPhotoFromC","([IIIDDDDD)[I",(void *) warpPhotoFromC}

};

#endif //PHOTOEDIT_PICWARP_H
