package edu.csci5448.photodroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.net.URI;

// Code adapted from http://javatechig.com/android/writing-image-picker-using-intent-in-android
public class LoadPhoto extends AppCompatActivity {

    private Photo currentPhoto;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_photo);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

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
    @Override
    // Rotate code from http://stackoverflow.com/questions/9015372/how-to-rotate-a-bitmap-90-degrees
    protected void onActivityResult(int requestCode, int resultCode, Intent galIntent){
            Bitmap selectedImage;
            if (resultCode == RESULT_OK) {
                Matrix rotate90 = new Matrix();
                rotate90.postRotate(90);
                Uri imageURI = galIntent.getData();
                try {
                    selectedImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURI));

                    //If the selected image is > (2048x2048) then scale the picture down
                    if ((selectedImage.getHeight() > 2048) || (selectedImage.getWidth() > 2048))
                    {
                        Log.w(TAG, "Resizing image!\n");
                        int newSize = (int) (selectedImage.getHeight() * (512.0 / selectedImage.getWidth()));
                        selectedImage = Bitmap.createScaledBitmap(selectedImage, 512, newSize, true);
                    }

                    // Rotate to fit screen
                    if (selectedImage.getWidth() > selectedImage.getHeight()) {
                        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), rotate90, true);
                    }

                    //Display image to screen
                    currentPhoto = new Photo(selectedImage,(ImageView) findViewById(R.id.SelectPhotoImageView));
                    currentPhoto.setImageResource();

                } catch (Exception e) {
                    //output error to log
                    Log.e(TAG, "exception", e);
                }
            }
        else{
            Log.w(TAG, "exiting!\n");
            this.finish(); // Photo not selected, go to home page
        }
     }

}




