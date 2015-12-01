package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

//Opencv Libraries
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;

public class EditPhoto extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Photo mainPhoto;
    private Bitmap loadedBitmap;//need to be able to access this inside filter methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Photo tempPhoto;
        Bitmap tempBitmap;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        // load bitmap from previous activity, if it fails, finish
        try {
            InputStream bitmapIn = openFileInput("bitmap.png");
            tempBitmap = BitmapFactory.decodeStream(bitmapIn);
            //set bitmap for object
            setBitmap(tempBitmap);
        }
        catch(Exception e){
            setBitmap(null);
            this.finish();
        }
        Log.w(TAG, "height: " + getBitmap().getHeight());
        tempPhoto = new Photo(getBitmap(),(ImageView)findViewById(R.id.EditPhotoImageView));
        setPhoto(tempPhoto);
        getPhoto().setImageResource();
    }


    public void onDiscardPhoto(View v){
        this.finish();
    }

    public void onSavePhoto(View v){

    }

    public void onBlurSelected(View v){
        //Converting bitmap to Mat
        Mat ImageMat = new Mat (getBitmap().getHeight(), getBitmap().getWidth(), CvType.CV_8U, new Scalar(4));
        Bitmap myBitmap32 = getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, ImageMat);

        //STUB--Do image processing with opencv here

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(ImageMat.cols(),  ImageMat.rows(),Bitmap.Config.ARGB_8888);;
        Utils.matToBitmap(ImageMat, resultBitmap);

        //Set object bitmap to the result
        setBitmap(resultBitmap);

        //Stub--probably need to return to the image display here
    }

    //getter for mainPhoto
    public Photo getPhoto(){
        return this.mainPhoto;
    }

    //Setter for Photo
    private void setPhoto(Photo tempPhoto)
    {
        //Call copy constructor
        this.mainPhoto = new Photo(tempPhoto);
        return;
    }

    //Getter for loadedBitmap
    public Bitmap getBitmap()
    {
        return this.loadedBitmap;
    }

    //Setter for loadedBitmap
    private void setBitmap(Bitmap tempBitmap)
    {
        this.loadedBitmap = tempBitmap;
        return;
    }
}
