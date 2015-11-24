package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by -Derek- on 11/24/2015.
 */
public class Photo {
    private Bitmap currentPhoto;
    private ImageView frame;
    public Photo(Bitmap desiredPhoto, ImageView desiredFrame){
        currentPhoto=desiredPhoto;
        frame = desiredFrame;


    }
    public void setImageResource(){
        frame.setImageBitmap(currentPhoto);

    }
    public void filterPhoto(String str){


    }
}
