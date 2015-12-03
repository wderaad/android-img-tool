package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;


public class ModifyImage extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private PhotoController controller = new PhotoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bitmap bitmap;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_image);
        // load bitmap from previous activity, if it fails, finish
        try {
            InputStream bitmapIn = openFileInput("bitmap.png");
            bitmap = BitmapFactory.decodeStream(bitmapIn);
            //set bitmap for object
            controller.setCurrentPhoto(new Photo(
                    bitmap,(ImageView)findViewById(R.id.EditPhotoImageView)));
        }
        catch(Exception e){
            controller.setCurrentPhoto(null);
            this.finish();
        }
        controller.getCurrentPhoto().setImageResource();
    }


    public void onDiscardPhoto(View v){
        this.finish();
    }
    // from http://stackoverflow.com/questions/8560501/android-save-image-into-gallery
    public void onSavePhoto(View v){
        MediaStore.Images.Media.insertImage(getContentResolver(),
                controller.getCurrentPhoto().getCurrentBitmap(),"PhotoDroid-Photo","Modified-Photo");
        this.finish();
    }

    //this method redisplays the edited bitmap
    public void redisplay()
    {
        Log.d(TAG, "Redisplaying edited image\n");
        this.controller.getCurrentPhoto().setImageResource();
    }

    //Blur effect
    public void onBlurSelected(View v){
        controller.setCurrentPhoto(new Photo(
                controller.applyFilter("blur"), (ImageView) findViewById(R.id.EditPhotoImageView)));

        //Redisplay edited image
        redisplay();
    }

    //Sharpen filter
    public void onSharpenSelected(View v){
        controller.setCurrentPhoto(new Photo(
                controller.applyFilter("sharpen"),(ImageView)findViewById(R.id.EditPhotoImageView)));

        //Redisplay edited image
        redisplay();
    }

    //Contours effect: Contours can be explained simply as a curve joining all the continuous points (along the boundary), having same color or intensity.
    //http://docs.opencv.org/2.4/doc/tutorials/imgproc/shapedescriptors/find_contours/find_contours.html#find-contours
    public void onContoursSelected(View v){
        controller.setCurrentPhoto(new Photo(
                controller.applyFilter("contour"),(ImageView)findViewById(R.id.EditPhotoImageView)));

        //Redisplay edited image
        redisplay();

    }
}
