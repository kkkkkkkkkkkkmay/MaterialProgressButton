package com.kot32.materialprogressbutton.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.kot32.materialprogressbutton.R;
import com.kot32.materialprogressbutton.view.MaterialProgressButton;


public class MainActivity extends ActionBarActivity {


    private MaterialProgressButton materialProgressButton;
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    int progress = materialProgressButton.getProgress();
                    materialProgressButton.setProgress(++progress);
                    if (progress >= 100) {
                        return;
                    }
                    if (progress == 30 || progress == 75 || progress == 90) {
                        mHandler.sendEmptyMessageDelayed(1, 2000);
                    } else
                        mHandler.sendEmptyMessageDelayed(1, 5);
                    break;

            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialProgressButton = ((MaterialProgressButton) findViewById(R.id.material_progress_button));
        materialProgressButton.setIsStartBreathAnimator(true);
        materialProgressButton.setIProgress(new MaterialProgressButton.onProgressChanged() {
            @Override
            public void onStart() {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onFinish() {
                materialProgressButton.setTitle("完成");
            }
        });
        materialProgressButton.setIndeterminate(false);
    }

}
