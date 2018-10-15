package vlad.cvtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;


public class MainActivity extends AppCompatActivity
    implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static String tag = "MainActivity";
    static {
        if(OpenCVLoader.initDebug())
            Log.i(tag, "loaded");

        else Log.i(tag, "not loaded");
    }

    JavaCameraView camera;
    BaseLoaderCallback loader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

            switch (status){
                case BaseLoaderCallback.SUCCESS:{
                    camera.enableView();
                    break;
                }

                default:{
                    super.onManagerConnected(status);
                }
            }
        }
    };

    Mat image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera = (JavaCameraView)findViewById(R.id.camera);
        camera.setVisibility(SurfaceView.VISIBLE);
        camera.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(camera != null) {
            camera.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(camera != null) {
            camera.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(camera != null) {
            camera.disableView();
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
        return image;
    }
}
