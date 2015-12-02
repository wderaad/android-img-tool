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
import org.opencv.core.Size;
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
        tempPhoto = new Photo(getBitmap(),(ImageView)findViewById(R.id.EditPhotoImageView));
        setPhoto(tempPhoto);
        getPhoto().setImageResource();//check photo was set correctly
        tempPhoto = null;//return to garbage collector

        return;
    }


    public void onDiscardPhoto(View v){
        this.finish();
        return;
    }

    public void onSavePhoto(View v){
        return;
    }

    //this method redisplays the edited bitmap
    public void redisplay(Bitmap resultBitmap)
    {
        Photo tempPhoto;

        //set new bitmap
        setBitmap(resultBitmap);

        Log.d(TAG, "Redisplaying edited image\n");
        tempPhoto = new Photo(getBitmap(),(ImageView)findViewById(R.id.EditPhotoImageView));
        setPhoto(tempPhoto);
        getPhoto().setImageResource();//check photo was set correctly
        tempPhoto = null;//return to garbage collector

        return;
    }

    //Blur effect
    public void onBlurSelected(View v){
        //Set kernel size of blur effect (can be increase/decreased):
        org.opencv.core.Size size = new Size(25,25);//filter levels
        int sigmaX = 4;//filter width

        //Converting bitmap to Mat
        Mat imageMat = new Mat (getBitmap().getHeight(), getBitmap().getWidth(), CvType.CV_8UC3, new Scalar(4));
        Bitmap myBitmap32 = getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, imageMat);


        //Do image processing (Blur) with opencv here
        Imgproc.GaussianBlur(imageMat, imageMat, size, sigmaX);

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);;
        Utils.matToBitmap(imageMat, resultBitmap);

        //Redisplay edited image
        redisplay(resultBitmap);

        return;
    }

    //Sharpen filter
    public void onSharpenSelected(View v){
        //Set kernel size of blur effect (can be increase/decreased):
        org.opencv.core.Size size = new Size(0,0);//gaussian smoothing levels
        int sigmaX = 10;//filter width
        Mat imageMatCopy;//for subtracting from original image

        //Converting bitmap to Mat
        Mat imageMat = new Mat (getBitmap().getHeight(), getBitmap().getWidth(), CvType.CV_8UC3);
        Bitmap myBitmap32 = getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, imageMat);

        //create own kernel to run over image
        Mat kernel = new Mat(3, 3, CvType.CV_32F);//create 3x3 matrix
        //.put(row,col,value)
        kernel.put(0,0,-1);
        kernel.put(0,1,-1);
        kernel.put(0,2,-1);
        kernel.put(1,0,-1);
        kernel.put(1,1,9);
        kernel.put(1,2,-1);
        kernel.put(2,0,-1);
        kernel.put(2,1,-1);
        kernel.put(2,2,-1);

        Imgproc.filter2D(imageMat, imageMat, -1, kernel);

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);;
        Utils.matToBitmap(imageMat, resultBitmap);

        //Redisplay edited image
        redisplay(resultBitmap);

        return;
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
