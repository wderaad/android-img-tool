package edu.csci5448.photodroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AcceptRejectPhoto extends AppCompatActivity {
    private Photo cameraPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_reject_photo);
    }
}
