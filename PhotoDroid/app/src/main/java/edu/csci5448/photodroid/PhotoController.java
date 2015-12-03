package edu.csci5448.photodroid;
import android.graphics.Bitmap;

/**
 * Created by wjderaad on 12/2/15.
 */
public class PhotoController {
    private PhotoStack photoStack;
    private Photo currentPhoto;

    public PhotoController(){

    }

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    public void clearStack(){
        photoStack.clear();
    }

    public Bitmap applyFilter(String type){ //TODO

        return Filter.filterPhoto(currentPhoto.getCurrentBitmap(), type);

    }

    public void updateView(){

    }

    public void rejectPhoto(){

    }

    public void acceptPhoto(){

    }

}
