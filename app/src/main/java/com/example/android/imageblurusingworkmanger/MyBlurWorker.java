package com.example.android.imageblurusingworkmanger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.imageblurusingworkmanger.utils.BlurWorkerUtil;

import org.greenrobot.eventbus.EventBus;

public class MyBlurWorker extends Worker {
    private Bitmap output;
    public static Uri outputUri;

    public MyBlurWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        try {

            Bitmap pic = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.azeem);
            output = BlurWorkerUtil.blurImage(pic,getApplicationContext());
            outputUri = BlurWorkerUtil.writeBitmapToFile(getApplicationContext(),output);

            if (outputUri != null){
                EventBus.getDefault().post(new EventImageUri(outputUri));
            }

            return Result.success();

        } catch (Throwable t) {
            t.getLocalizedMessage();
            Log.d("tag", "doWork: "+t.toString());
            return Result.failure();
        }

    }
}