
#include <stdio.h>
#include <sys/time.h>
#include "systemclock.h"

JNIEXPORT jint JNICALL Java_net_orfjackal_thematrixtime_SystemClock_getTimeOfDay(JNIEnv *env, jclass cls, jlongArray jSecUsec) {
	struct timeval time;
	jsize len = env->GetArrayLength(jSecUsec);
	if (len != 2) {
		return -10;
	}
	jlong *secUsec = env->GetLongArrayElements(jSecUsec, 0);
	jint retval = gettimeofday(&time, 0);
	secUsec[0] = time.tv_sec;
	secUsec[1] = time.tv_usec;
	env->ReleaseLongArrayElements(jSecUsec, secUsec, 0);
	return retval;
}

JNIEXPORT jint JNICALL Java_net_orfjackal_thematrixtime_SystemClock_setTimeOfDay(JNIEnv *env, jclass cls, jlongArray jSecUsec) {
	struct timeval time;
	jsize len = env->GetArrayLength(jSecUsec);
	if (len != 2) {
		return -10;
	}
	jlong *secUsec = env->GetLongArrayElements(jSecUsec, 0);
	time.tv_sec = secUsec[0];
	time.tv_usec = secUsec[1];
	jint retval = settimeofday(&time, 0);
	env->ReleaseLongArrayElements(jSecUsec, secUsec, 0);
	return retval;
}

/*
JNIEXPORT void JNICALL Java_net_orfjackal_thematrixtime_SystemClock_adjustTimeBy(JNIEnv * env, jclass clazz, jlong milliseconds) {
	struct timeval currentTime;
	if (gettimeofday(&currentTime, 0) == 0) {
		long sec = milliseconds / 1000;
		long usec = (milliseconds % 1000) * 1000;
		currentTime.tv_sec = currentTime.tv_sec + sec;
		currentTime.tv_usec = currentTime.tv_usec + usec;
		settimeofday(&currentTime, 0);
	}
}
*/
