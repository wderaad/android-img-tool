package edu.csci5448.photodroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.InputStream;

// spinner code adapted from http://developer.android.com/guide/topics/ui/controls/spinner.html
public class ModifyImage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private PhotoController controller;
    private Spinner FilterSpin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bitmap bitmap;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_image);
        // load bitmap from previous activity, if it fails, finish
        try {
            InputStream bitmapIn = openFileInput("bitmap.png");
            bitmap = BitmapFactory.decodeStream(bitmapIn);
            //set bitmap for object
            controller = new PhotoController(new Photo(
                    bitmap,(ImageView)findViewById(R.id.EditPhotoImageView)));
        }
        catch(Exception e){
            controller = new PhotoController();
            this.finish();
        }
        controller.getCurrentPhoto().setImageResource();

        // Set up the spinner:
        FilterSpin= (Spinner)findViewById(R.id.filterselect);
        ArrayAdapter<CharSequence> filteradapter = ArrayAdapter.createFromResource(this, R.array.filter_array, android.R.layout.simple_spinner_dropdown_item);
        filteradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FilterSpin.setAdapter(filteradapter);
        FilterSpin.setOnItemSelectedListener(this);
    }


    public void onDiscardPhoto(View v){
        this.finish();
    }

    public void onRemoveFilter(View v){
        controller.rejectPhoto();
        redisplay();
    }


    // from http://stackoverflow.com/questions/8560501/android-save-image-into-gallery
    public void onSavePhoto(View v){
        MediaStore.Images.Media.insertImage(getContentResolver(),
                controller.getCurrentPhoto().getCurrentBitmap(), "PhotoDroid-Photo", "Modified-Photo");
        this.finish();
    }

    //this method redisplays the edited bitmap
    public void redisplay()
    {
        Log.d(TAG, "Redisplaying edited image\n");
        this.controller.getCurrentPhoto().setImageResource();
    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        String temp=(String)FilterSpin.getSelectedItem();
        controller.setCurrentPhoto(new Photo(
                controller.applyFilter(temp),(ImageView)findViewById(R.id.EditPhotoImageView)));

        //Redisplay edited image
        FilterSpin.setSelection(0);
        redisplay();
    }
    public void onNothingSelected(AdapterView<?> parent){

    }
}
