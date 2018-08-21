package com.example.administrator.opencvstudy;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.opencvstudy.cameraRender.CalibrationResult;
import com.example.administrator.opencvstudy.cameraRender.CameraCalibrator;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class JavaCameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private JavaCameraView cameraView;
    private Mat rgba;
    private CameraCalibrator mCalibrator;
    private OnCameraFrameRender mOnCameraFrameRender;
    private int mWidth;
    private int mHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_java_camera);

        cameraView = findViewById(R.id.cameraView);
        cameraView.setCvCameraViewListener(this);



    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //定义Mat对象
//        rgba = new Mat(width, height, CvType.CV_8UC4);

        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
            mCalibrator = new CameraCalibrator(mWidth, mHeight);
            if (CalibrationResult.tryLoad(this, mCalibrator.getCameraMatrix(), mCalibrator.getDistortionCoefficients())) {
                mCalibrator.setCalibrated();
            }

//            mOnCameraFrameRender = new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
            mOnCameraFrameRender =
                    new OnCameraFrameRender(new ComparisonFrameRender(mCalibrator, mWidth, mHeight, getResources()));

//            final Resources res = getResources();
//            if (mCalibrator.getCornersBufferSize() < 2) {
//                (Toast.makeText(this, res.getString(R.string.more_samples), Toast.LENGTH_SHORT)).show();
//                return ;
//            }
//
//            mOnCameraFrameRender = new OnCameraFrameRender(new PreviewFrameRender());
//            new AsyncTask<Void, Void, Void>() {
//                private ProgressDialog calibrationProgress;
//
//                @Override
//                protected void onPreExecute() {
//                    calibrationProgress = new ProgressDialog(JavaCameraActivity.this);
//                    calibrationProgress.setTitle(res.getString(R.string.calibrating));
//                    calibrationProgress.setMessage(res.getString(R.string.please_wait));
//                    calibrationProgress.setCancelable(false);
//                    calibrationProgress.setIndeterminate(true);
//                    calibrationProgress.show();
//                }
//
//                @Override
//                protected Void doInBackground(Void... arg0) {
//                    mCalibrator.calibrate();
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void result) {
//                    calibrationProgress.dismiss();
//                    mCalibrator.clearCorners();
//                    mOnCameraFrameRender = new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
//                    String resultMessage = (mCalibrator.isCalibrated()) ?
//                            res.getString(R.string.calibration_successful)  + " " + mCalibrator.getAvgReprojectionError() :
//                            res.getString(R.string.calibration_unsuccessful);
//                    (Toast.makeText(JavaCameraActivity.this, resultMessage, Toast.LENGTH_SHORT)).show();
//
//                    if (mCalibrator.isCalibrated()) {
//                        CalibrationResult.save(JavaCameraActivity.this,
//                                mCalibrator.getCameraMatrix(), mCalibrator.getDistortionCoefficients());
//                    }
//                }
//            }.execute();

        }
    }

    @Override
    public void onCameraViewStopped() {
//        rgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        rgba = inputFrame.rgba();


        return mOnCameraFrameRender.render(inputFrame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
        } else {
            cameraView.enableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    static {
        System.loadLibrary("native-lib");
    }
    //NDK对每一帧数据进行操作
    public static native String nativeRgba(long jrgba);
}
