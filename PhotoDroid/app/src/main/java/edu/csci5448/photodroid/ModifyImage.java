package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

//Opencv Libraries
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModifyImage extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Photo mainPhoto;
    private Bitmap loadedBitmap;//need to be able to access this inside filter methods
    //custom kernels for filters
    private Mat Sharpenkernel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Photo tempPhoto;
        Bitmap tempBitmap;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_image);
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

        //initialize all custom kernels
        initializeKernels();

        return;
    }


    public void onDiscardPhoto(View v){
        this.finish();
        return;
    }
    // from http://stackoverflow.com/questions/8560501/android-save-image-into-gallery
    public void onSavePhoto(View v){
        MediaStore.Images.Media.insertImage(getContentResolver(),loadedBitmap,"PhotoDroid-Photo","Modified-Photo");
        this.finish();


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
        Bitmap resultBitmap = Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);
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

        //Perform image sharpening with custom filter
        Imgproc.filter2D(imageMat, imageMat, -1, Sharpenkernel);

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, resultBitmap);

        //Redisplay edited image
        redisplay(resultBitmap);

        return;
    }

    //Contours effect: Contours can be explained simply as a curve joining all the continuous points (along the boundary), having same color or intensity.
    //http://docs.opencv.org/2.4/doc/tutorials/imgproc/shapedescriptors/find_contours/find_contours.html#find-contours
    public void onContoursSelected(View v){
        //Set kernel size of blur effect (can be increase/decreased):
        org.opencv.core.Size size = new Size(3,3);//filter levels
        int thresh = 100;
        Mat imageMatGray;
        //For filter
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Random random = new Random();
        Scalar color = new Scalar(random.nextInt(255), random.nextInt(255),random.nextInt(255));

        //Converting bitmap to Mat
        Mat imageMat = new Mat (getBitmap().getHeight(), getBitmap().getWidth(), CvType.CV_8UC3, new Scalar(4));
        Bitmap myBitmap32 = getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, imageMat);

        //initialize image gray
        imageMatGray = new Mat (getBitmap().getHeight(), getBitmap().getWidth(), CvType.CV_8UC3, new Scalar(4));
        Utils.bitmapToMat(myBitmap32, imageMatGray);

        //Convert image to gray and blur it
        Imgproc.cvtColor(imageMat, imageMatGray, Imgproc.COLOR_RGB2GRAY);
        Imgproc.blur(imageMatGray, imageMatGray, size);
        //Now find edges with Canny algorithm
        Imgproc.Canny(imageMatGray, imageMat, thresh, thresh*2);
        //Now find contours
        Imgproc.findContours(imageMat, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        //Now draw the contours
        Mat drawing = Mat.zeros(imageMatGray.size(), CvType.CV_8UC3);
        for(int i = 0; i< contours.size(); i++ ) {
            //Declare random numer between 0 and 255 to color pixel contour
            color.val[0] = random.nextInt(255);
            color.val[1] = random.nextInt(255);
            color.val[2] = random.nextInt(255);
            //Imgproc.drawContours(drawing, contours, i, color, 2, 8, hierarchy, 0, new Point() );
            Imgproc.drawContours(drawing, contours, i, color, 8);
        }

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(drawing.cols(),drawing.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(drawing, resultBitmap);

        //Redisplay edited image
        redisplay(resultBitmap);

        return;
    }

    //method to initialize kernels
    public void initializeKernels()
    {
        //Sharpen Kernel
        /*
            -1 -1 -1
            -1  9 -1
            -1 -1 -1
         */
        this.Sharpenkernel = new Mat(3, 3, CvType.CV_32F);//create 3x3 matrix
        //.put(row,col,value)
        this.Sharpenkernel.put(0,0,-1);
        this.Sharpenkernel.put(0,1,-1);
        this.Sharpenkernel.put(0,2,-1);
        this.Sharpenkernel.put(1,0,-1);
        this.Sharpenkernel.put(1,1,9);
        this.Sharpenkernel.put(1,2,-1);
        this.Sharpenkernel.put(2,0,-1);
        this.Sharpenkernel.put(2,1,-1);
        this.Sharpenkernel.put(2,2,-1);

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
