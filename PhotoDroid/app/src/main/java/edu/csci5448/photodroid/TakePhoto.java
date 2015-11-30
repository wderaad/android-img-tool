package edu.csci5448.photodroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.hardware.Camera;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Some code adapted from http://developer.android.com/guide/topics/media/camera.html#custom-camera
@SuppressWarnings("deprecation")
public class TakePhoto extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button imageButton;//for taking photos
    //For saving images
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private boolean finished = false;

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

        //Initialize button listener for taking photo
        addListenerOnButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Release camera resources
        Log.w(TAG, "Releasing Camera!\n");
        mCamera.stopPreview();//required step
        mCamera.release();
        mCamera= null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null) {
            Log.w(TAG, "Releasing Camera!\n");
            mCamera.stopPreview();
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
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


    //To retrieve photo data http://www.mkyong.com/android/android-imagebutton-selector-example/
    public void addListenerOnButton() {

        imageButton = (Button) findViewById(R.id.button_capture);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.w(TAG, "Taking Photo!\n");
                // get an image from the camera
                mCamera.takePicture(null, null, mPicture);
                //I suggest popping up a button around here to see if user wants to keep or delete image
            }
        });
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            //Returns generic name for image
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                Log.w(TAG, "Saving!\n");
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.w(TAG, "Saved!\n");
                finished = true;

                if(finished)
                {
                    //return to parent
                    finish();
                }

            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "PhotoDroid");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.d("PhotoDroid", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +"PhotoDroidImg"+ timeStamp + ".jpg");
            Log.w(TAG, "Created Media file" + mediaFile + "\n");
        }
        else {
            Log.w(TAG, "Returning null... not good!\n");
            return null;
        }

        return mediaFile;
    }
}
