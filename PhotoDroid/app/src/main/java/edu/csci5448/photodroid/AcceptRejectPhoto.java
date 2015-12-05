package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.InputStream;

public class AcceptRejectPhoto extends AppCompatActivity {
    private Bitmap takenPhoto;
    private ImageView acceptScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_photo);
        try {
            InputStream bitmapIn = openFileInput("bitmap.png");
            takenPhoto = BitmapFactory.decodeStream(bitmapIn);
            acceptScreen= (ImageView)findViewById(R.id.AcceptRejectImageView);
            acceptScreen.setImageBitmap(takenPhoto);

        }
        catch(Exception e){
            this.finish();
        }
    }
}
