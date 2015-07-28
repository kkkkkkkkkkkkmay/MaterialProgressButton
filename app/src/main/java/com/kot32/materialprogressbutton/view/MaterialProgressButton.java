package com.kot32.materialprogressbutton.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ProgressBar;

import com.kot32.materialprogressbutton.R;

/**
 * Created by kot32 on 15/7/28.
 */
public class MaterialProgressButton extends ProgressBar {

    //默认属性值
    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final String DEFAULT_TEXT = "确定";
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    //文字画笔
    private Paint mTextPaint = new Paint();
    //波纹画笔
    private Paint mRipplePaint = new Paint();

    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private String mText = DEFAULT_TEXT;
    //初始的背景颜色
    private int sBackground;
    //波纹半径
    private float rippleRadius = 0;
    private float rippleBorder = 0;

    //起始点位置
    private PointF startPosition = null;

    //按钮文字是否显示进度
    private boolean isShowProgress = false;
    //开始进度的回调
    private onProgressChanged IProgress;
    //当前进度(父类变量可能会造成BUG)
    private int currentProgress = 0;

    public MaterialProgressButton(Context context) {
        super(context);
        init();
    }

    public MaterialProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyledAttributes(attrs);
        init();
    }

    private void init() {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        sBackground = ((ColorDrawable) getBackground()).getColor();
        mRipplePaint.setColor(makePressColor(sBackground, 255));
        rippleBorder = Math.max(getWidth(), getHeight());//超出边界就不再绘制

        postInvalidate();
    }

    private void obtainStyledAttributes(AttributeSet attrs) {
        // 初始化属性
        final TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.MaterialProgressButton);

        mTextColor = attributes
                .getColor(
                        R.styleable.MaterialProgressButton_material_progress_button_text_color,
                        DEFAULT_TEXT_COLOR);
        mTextSize = (int) attributes.getDimension(
                R.styleable.MaterialProgressButton_material_progress_button_text_size,
                mTextSize);
        mText = attributes.getString(R.styleable.MaterialProgressButton_material_progress_button_text);
        isShowProgress = attributes.getBoolean(R.styleable.MaterialProgressButton_material_progress_button_is_show_progress, false);
        attributes.recycle();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //测量字体的宽度和高度
        float textWidth = mTextPaint.measureText(mText);
        float textHeight = (mTextPaint.descent() + mTextPaint.ascent()) / 2;
        //画居中的文字
        if (!isShowProgress) {
            canvas.drawText(mText, (getWidth() - textWidth) / 2 + 5, (getHeight() - textHeight) / 2 + 10, mTextPaint);
        } else {
            if (startPosition != null) {
                canvas.drawText(currentProgress + "%", (getWidth() - textWidth) / 2 + 5, (getHeight() - textHeight) / 2 + 10, mTextPaint);
            } else {
                canvas.drawText(mText, (getWidth() - textWidth) / 2 + 5, (getHeight() - textHeight) / 2 + 10, mTextPaint);
            }
        }

        //画圆,根据目前进度计算目前半径
        if (startPosition != null) {
            rippleRadius = rippleBorder * ((currentProgress * 1.0f) / getMax());
            canvas.drawCircle(startPosition.x, startPosition.y, rippleRadius, mRipplePaint);
            if (rippleRadius >= rippleBorder) {
                restButton();
                IProgress.onFinish();
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundDrawable(new ColorDrawable(makePressColor(sBackground, 255)));
                break;
            case MotionEvent.ACTION_CANCEL:
                setBackgroundDrawable(new ColorDrawable(sBackground));
                break;
            case MotionEvent.ACTION_UP:
                //开始进度
                setBackgroundDrawable(new ColorDrawable(sBackground));
                IProgress.onStart();
                //边界值为起始点距离四个点之间的最大距离
                startPosition = new PointF();
                startPosition.x = event.getX();
                startPosition.y = event.getY();

                float borderA = (float) getDistance(startPosition, new PointF(0, 0));
                float borderB = (float) getDistance(startPosition, new PointF(getWidth(), 0));
                float borderC = (float) getDistance(startPosition, new PointF(0, getHeight()));
                float borderD = (float) getDistance(startPosition, new PointF(getWidth(), getHeight()));
                rippleBorder = getMaxNumber(borderA, borderB, borderC, borderD);//超出边界就不再绘制
                break;
        }

        return true;
    }

    private int makePressColor(int color, int alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        r = (r - 30 < 0) ? 0 : r - 30;
        g = (g - 30 < 0) ? 0 : g - 30;
        b = (b - 30 < 0) ? 0 : b - 30;
        return Color.argb(alpha, r, g, b);
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

    private double getDistance(PointF pA, PointF pB) {
        double _x = Math.abs(pA.x - pB.x);
        double _y = Math.abs(pA.y - pB.y);
        return Math.sqrt(_x * _x + _y * _y);
    }

    private float getMaxNumber(float... args) {
        float max = 0;
        for (float i : args)
            if (i >= max) max = i;
        return max;
    }

    public void setIProgress(onProgressChanged IStart) {
        this.IProgress = IStart;
    }

    public interface onProgressChanged {
        void onStart();

        void onFinish();
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (progress >= getMax()) {
            currentProgress = getMax();
        }
        if (progress < 0) {
            currentProgress = 0;
        }
        currentProgress = progress;
        postInvalidate();
    }

    @Override
    public synchronized int getProgress() {
        return currentProgress;
    }

    void restButton() {
        setBackgroundDrawable(new ColorDrawable(sBackground));
        startPosition = null;
        rippleRadius = 0;
        currentProgress = 0;
    }

    public void setTitle(String title) {
        mText = title;
    }

    public boolean isShowProgress() {
        return isShowProgress;
    }

    public void setIsShowProgress(boolean isShowProgress) {
        this.isShowProgress = isShowProgress;
    }
}

