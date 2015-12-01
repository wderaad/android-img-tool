package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by -Derek- on 11/24/2015.
 */
public class Photo {
    private Bitmap currentPhoto;
    private ImageView frame;

    public Photo()
    {
        //Call constructor with methods
        this(null,null);
    }

    public Photo(Bitmap desiredPhoto, ImageView desiredFrame){
        setCurrentPhoto(desiredPhoto);
        setFrame(desiredFrame);
    }

    //copy constructor
    public Photo(Photo photo){
        //Set all properties exactly the same
        this(photo.getCurrentPhoto(),photo.getFrame());
    }

    public void setImageResource(){
        this.getFrame().setImageBitmap(this.getCurrentPhoto());
    }

    //getters and setters for photo
    public Bitmap getCurrentPhoto()
    {
        return this.currentPhoto;
    }

    private void setCurrentPhoto(Bitmap bitmap)
    {
        this.currentPhoto = bitmap;
    }

    public ImageView getFrame()
    {
        return this.frame;
    }

    private void setFrame(ImageView iview){
        this.frame = iview;
    }
}
