package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

public class EditPhoto extends AppCompatActivity {
    private Photo mainPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bitmap loadedBitmap;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);
        // load bitmap from previous activity, if it fails, finish
        try {
            InputStream bitmapIn = openFileInput("bitmap.png");
            loadedBitmap= BitmapFactory.decodeStream(bitmapIn);

        }
        catch(Exception e){
            loadedBitmap=null;
            this.finish();
        }
        mainPhoto = new Photo(loadedBitmap,(ImageView)findViewById(R.id.EditPhotoImageView));
        mainPhoto.setImageResource();
    }


    public void onDiscardPhoto(View v){
        this.finish();
    }

    public void onSavePhoto(View v){


    }
}
