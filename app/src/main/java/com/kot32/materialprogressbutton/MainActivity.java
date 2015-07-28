package com.kot32.materialprogressbutton;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.kot32.materialprogressbutton.view.MaterialProgressButton;


public class MainActivity extends ActionBarActivity {

    private MaterialProgressButton materialProgressButton;
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    int progress = materialProgressButton.getProgress();
                    System.out.println("进度"+progress);
                    materialProgressButton.setProgress(++progress);
                    if (progress >= 100) {
                        return ;
                    }
                    mHandler.sendEmptyMessageDelayed(1, 50);
                    break;

            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialProgressButton = ((MaterialProgressButton) findViewById(R.id.material_progress_button));
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
