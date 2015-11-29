package edu.csci5448.photodroid;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.hardware.Camera;
import android.widget.FrameLayout;

// Some code adapted from http://developer.android.com/guide/topics/media/camera.html#custom-camera
public class
        TakePhoto extends AppCompatActivity {
        private Camera mCamera;
        private CameraPreview mPreview;
        private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        try {
            mCamera = Camera.open();
        }
        catch(Exception e) {
        }
        mCamera.setDisplayOrientation(90);
        mPreview= new CameraPreview(this,mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCamera.release();
        mCamera= null;

    }
    @Override
    protected void onResume(){
        super.onResume();
        try {
            mCamera = Camera.open();
        }
        catch(Exception e) {
            //output error to log
            Log.e(TAG, "exception", e);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
