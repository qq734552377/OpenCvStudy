#include <jni.h>
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/core/mat.hpp"
#include "opencv2/features2d.hpp"
#include "opencv2/core/core.hpp"
#include <vector>
#include "opencv2/videoio/videoio.hpp"


using namespace cv;
using namespace std;
extern "C" {
JNIEXPORT jstring JNICALL Java_com_example_administrator_opencvstudy_JavaCameraActivity_nativeRgba(
        JNIEnv *env,
        jobject /* this */,
        jlong jrgba) {


    return NULL;
}


JNIEXPORT void JNICALL Java_com_example_administrator_opencvstudy_CameraHandleActivity_FindFeatures(JNIEnv*, jobject, jlong addrGray, jlong addrRgba)
{
    Mat& mGr  = *(Mat*)addrGray;
    Mat& mRgb = *(Mat*)addrRgba;
    vector<KeyPoint> v;

    Ptr<FeatureDetector> detector = FastFeatureDetector::create(50);
    detector->detect(mGr, v);
    for( unsigned int i = 0; i < v.size(); i++ )
    {
        const KeyPoint& kp = v[i];
        circle(mRgb, Point(kp.pt.x, kp.pt.y), 10, Scalar(255,0,0,255));
    }

}

}