package com.example.android.imageblurusingworkmanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        EventBus.getDefault().register(this);
        final OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyBlurWorker.class)
                .build();

        button.setOnClickListener(view -> {
            WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent( EventImageUri event){
        Uri imageUri = event.getUri();
        imageView.setImageURI(imageUri);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}