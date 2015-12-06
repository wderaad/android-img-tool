package edu.csci5448.photodroid;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by wjderaad on 12/2/15.
 */
public class PhotoController {
    private PhotoStack photoStack;
    private Photo currentPhoto;
    private static final String TAG = "PhotoController: ";



    public PhotoController(){
        photoStack = new PhotoStack();
        currentPhoto = new Photo();
    }

    public PhotoController(Photo photo){
        photoStack = new PhotoStack();
        currentPhoto = photo;
    }

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    public Bitmap applyFilter(String type){ //TODO

        return Filter.filterPhoto(currentPhoto.getCurrentBitmap(), type);
    }

    public void setCurrentPhoto(Photo currentPhoto) {
        if(!this.currentPhoto.equals(currentPhoto)){ //Don't add duplicates to the stack
            photoStack.push(this.currentPhoto);
            Log.d(TAG, "setCurrentPhoto: pushed last photo \n");

        }
        this.currentPhoto = currentPhoto;
        Log.d(TAG, "setCurrentPhoto: complete \n");

    }

    public void rejectPhoto(){
        try{
            this.currentPhoto = photoStack.pop();
            Log.d(TAG, "rejectPhoto: complete \n");
        }catch (Exception e){
            Log.w(TAG, "rejectPhoto: "+e.toString()+"\n");
        }

    }

}
