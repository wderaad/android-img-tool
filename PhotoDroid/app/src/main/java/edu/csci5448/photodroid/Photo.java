package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by -Derek- on 11/24/2015.
 */
public class Photo {
    private Bitmap currentBitmap;
    private ImageView frame;

    public Photo()
    {
        //Call constructor with null variables
        this(null,null);
    }

    public Photo(Bitmap desiredPhoto, ImageView desiredFrame){
        setCurrentBitmap(desiredPhoto);
        setFrame(desiredFrame);
    }

    //copy constructor
    public Photo(Photo photo){
        //Set all properties exactly the same
        this(photo.getCurrentBitmap(),photo.getFrame());
    }

    public void setImageResource(){
        this.getFrame().setImageBitmap(this.getCurrentBitmap());
    }

    //getters and setters for photo
    public Bitmap getCurrentBitmap()
    {
        return this.currentBitmap;
    }

    private void setCurrentBitmap(Bitmap bitmap)
    {
        this.currentBitmap = bitmap;
    }

    public ImageView getFrame()
    {
        return this.frame;
    }

    private void setFrame(ImageView iview){
        this.frame = iview;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Photo)) {
            return false;
        }

        Photo that = (Photo) other;

        // Custom equality check here.
        return this.currentBitmap.equals(that.currentBitmap)
                && this.frame.equals(that.frame);
    }

}
