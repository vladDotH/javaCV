package com.vlad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainActivity
        extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {


    static{
        if(!OpenCVLoader.initDebug()){
            Log.i("cv", "cv not loaded");
        }
        else{
            Log.i("cv", "cv loaded" );
        }
    }

    JavaCameraView cam;
    BaseLoaderCallback callback = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS : {
                    cam.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    Mat image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cam = findViewById(R.id.cam);
        cam.setVisibility(SurfaceView.VISIBLE);
        cam.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if( cam != null )
            cam.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( cam != null )
            cam.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            Log.i("cv", "cv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, callback );
        }
        else{
            Log.i("cv", "cv loaded" );
            callback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        image = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        image.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        image = inputFrame.rgba();

        MatOfPoint corners = new MatOfPoint();

        Log.i("cv", "corners created");

        Mat gray = new Mat(image.rows(), image.cols(), CvType.CV_8UC1);
        Imgproc.cvtColor(image, gray,Imgproc.COLOR_BGR2GRAY);

        Log.i("cv", "CVTcolor done");

        Imgproc.goodFeaturesToTrack(gray, corners, 50, 0.03, 6 );
        Log.i("cv", "corners found");

        for (Point pt : corners.toArray() ) {
            Imgproc.circle(image, pt, 3, new Scalar(128, 0, 255 ) );
        }
        Log.i("cv", "corners circled");

//        Imgproc.circle(image, new Point(30, 40 ), 3, new Scalar(128, 0, 255 ) );

        return image;
    }
}
