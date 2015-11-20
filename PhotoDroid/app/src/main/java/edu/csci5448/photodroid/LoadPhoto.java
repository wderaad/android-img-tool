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
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.net.URI;

// Code adapted from http://javatechig.com/android/writing-image-picker-using-intent-in-android
public class LoadPhoto extends AppCompatActivity {
    private final int SELECT_PHOTO=1;
    private Bitmap selectedImage;
    private ImageView selectedView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_photo);
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, SELECT_PHOTO);
        selectedView=(ImageView) findViewById(R.id.SelectPhotoImageView);
        //selectedView.setRotation(90);
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
            Matrix rotate90 = new Matrix();
            rotate90.postRotate(90);
            Uri imageURI= galIntent.getData();
            try {
                selectedImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageURI));
                // Rotate to fit screen
                if(selectedImage.getWidth() > selectedImage.getHeight()) {
                    selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), rotate90, true);
                }
                selectedView.setImageBitmap(selectedImage);

            }
            catch(Exception e) {

            }
     }

}




