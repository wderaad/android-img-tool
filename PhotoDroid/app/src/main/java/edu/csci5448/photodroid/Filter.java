package edu.csci5448.photodroid;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wjderaad on 12/2/15.
 */
public final class Filter {

    private Filter(){

    }

    public static Bitmap filterPhoto(Bitmap photo, String type){
        Bitmap filteredPhoto;
        switch (type){
            case "Blur":
                filteredPhoto = blur(photo);
                break;
            case "Sharpen":
                filteredPhoto = sharpen(photo);
                break;
            case "Countours":
                filteredPhoto = contour(photo);
                break;
            default:
                filteredPhoto = photo;
                break;
        }

        return filteredPhoto;
    }

    private static Bitmap blur(Bitmap photo){
        //Set kernel size of blur effect (can be increase/decreased):
        org.opencv.core.Size size = new Size(25,25);//filter levels
        int sigmaX = 4;//filter width

        //Converting bitmap to Mat
        Mat imageMat = new Mat (photo.getHeight(), photo.getWidth(), CvType.CV_8UC3, new Scalar(4));
        Bitmap myBitmap32 = photo.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, imageMat);


        //Do image processing (Blur) with opencv here
        Imgproc.GaussianBlur(imageMat, imageMat, size, sigmaX);

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, resultBitmap);

        //Return edited image
        return resultBitmap;
    }

    private static Bitmap sharpen(Bitmap photo){
        //Set kernel size of sharpen effect (can be increase/decreased):
        org.opencv.core.Size size = new Size(0,0);//gaussian smoothing levels
        int sigmaX = 10;//filter width

        //Converting Bitmap to Mat
        Mat source = new Mat (photo.getHeight(), photo.getWidth(), CvType.CV_8UC3);
        Mat dest = new Mat(source.rows(),source.cols(),source.type());

        Bitmap myBitmap32 = photo.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, source);

        //Perform image sharpening with gaussian
        Imgproc.GaussianBlur(source, dest, size, sigmaX);
        Core.addWeighted(source, 1.5, dest, -0.5, 0, dest);

        //Convert Mat back to bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(dest.cols(),dest.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dest, resultBitmap);

        //Return edited image
        return resultBitmap;
    }
    private static Bitmap contour(Bitmap photo){
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
        Mat imageMat = new Mat (photo.getHeight(), photo.getWidth(), CvType.CV_8UC3, new Scalar(4));
        Bitmap myBitmap32 = photo.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(myBitmap32, imageMat);

        //initialize image gray
        imageMatGray = new Mat (photo.getHeight(), photo.getWidth(), CvType.CV_8UC3, new Scalar(4));
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
        return resultBitmap;
    }
}
