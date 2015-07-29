# MaterialProgressButton
波纹表示目前进度的ProgressBar组件

样式像是一个按钮，按下后触发onStart并产生波纹，当波纹波及整个按钮后，触发onFinish，可以用作登录或下载等涉及到进度的按钮
可以通过xml 或者代码控制是否在按钮上显示当前进度

效果图如下：

只用波纹，不用数字表示目前进度：

![Demo](http://image17-c.poco.cn/mypoco/myphoto/20150729/09/17425403720150729092535049.gif?520x530_110
)

加上数字显示进度功能：

![Demo](http://image17-c.poco.cn/mypoco/myphoto/20150729/09/17425403720150729092610013.gif?664x506_110
)

此外，还有当进度卡住时的自动动画功能

![Demo](http://image17-c.poco.cn/mypoco/myphoto/20150729/09/17425403720150729092633023.gif?300x444_110
)

使用：

        <com.kot32.materialprogressbutton.view.MaterialProgressButton
        android:id="@+id/material_progress_button"
        xmlns:materialbutton="http://schemas.android.com/apk/res-auto"
        android:layout_width="100dp"
        android:layout_height="60dp"
        android:layout_centerHorizontal="true"
        android:layout_centerVertical="true"
        android:background="@android:color/holo_blue_dark"
        materialbutton:material_progress_button_text="登录"
        materialbutton:material_progress_button_text_size="25sp"
        materialbutton:material_progress_button_is_show_progress="true"
        />
        
        
 Java代码：
 
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
 	//是否加入动画materialProgressButton.setIsStartBreathAnimator(true);
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

 
 


