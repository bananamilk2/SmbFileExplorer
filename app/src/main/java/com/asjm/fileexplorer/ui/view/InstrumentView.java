package com.asjm.fileexplorer.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.asjm.fileexplorer.R;
import com.asjm.lib.util.ALog;

public class InstrumentView extends View {

    private int mRadius; // 圆弧半径
    private int mBigSliceCount; // 大份数
    private int mScaleCountInOneBigScale; // 相邻两个大刻度之间的小刻度个数
    private int mScaleColor; // 刻度颜色
    private int mScaleTextSize; // 刻度字体大小
    private String mUnitText = ""; // 单位
    private int mUnitTextSize; // 单位字体大小
    private int mMinValue; // 最小值
    private int mMaxValue; // 最大值
    private int mRibbonWidth; // 色条宽

    private int mStartAngle; // 起始角度
    private int mSweepAngle; // 扫过角度

    private int mPointerRadius; // 三角形指针半径
    private int mCircleRadius; // 中心圆半径

    private float mRealTimeValue = 0.0f; // 实时值


    private int mNumScaleRadius; // 数字刻度半径

    private int mViewColor_green; // 字体颜色
    private int mViewColor_yellow; // 字体颜色
    private int mViewColor_orange; // 字体颜色
    private int mViewColor_red; // 字体颜色

    private int mViewWidth; // 控件宽度
    private int mViewHeight; // 控件高度
    private float mCenterX;//中心点圆坐标x
    private float mCenterY;//中心点圆坐标y

    private Paint mPaintScale;//圆盘上大小刻度画笔
    private Paint mPaintScaleText;//圆盘上刻度值画笔
    private Paint mPaintCirclePointer;//绘制中心圆，指针
    private Paint mPaintValue;//绘制实时值
    private Paint mPaintRibbon;//绘制色带

    private RectF mRectRibbon;//存储色带的矩形数据
    private Rect mRectScaleText;//存储刻度值的矩形数据
    private Path path;//绘制指针的路径

    private int mSmallScaleCount; // 小刻度总数
    private float mBigScaleAngle; // 相邻两个大刻度之间的角度
    private float mSmallScaleAngle; // 相邻两个小刻度之间的角度

    private String[] mGraduations; // 每个大刻度的刻度值
    private float initAngle;//指针实时角度

    private SweepGradient mSweepGradient;//设置渐变
    private int[] color = new int[7];//渐变颜色组

    public InstrumentView(Context context) {
        this(context, null);
    }

    public InstrumentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InstrumentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InstrumentView, defStyleAttr, 0);
        int color = typedArray.getColor(R.styleable.InstrumentView_background_color, 0);
        typedArray.recycle();
        init();
    }

    private void init() {
        mStartAngle = 175;
        mSweepAngle = 190;

        initPaint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        ALog.getInstance().d(widthMode + " " + widthSize + " " + heightMode + " " + heightSize);

        if (widthSize <= heightSize) {
            mViewHeight = mViewWidth = Math.min(widthSize, heightSize);
            mRadius = mViewWidth / 2 - 10;
        } else {
            mViewWidth = mViewHeight = Math.min(widthSize, heightSize);
            mRadius = mViewHeight / 2 - 10;
        }
        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private static final float SWEEPANGLE = 240;
    private final int divide = 21;
    private int divideScale = 3;

    private static int DEFAULT_BING_SCALE_RADIUS = 10;
    private static int DEFAULT_SMALL_SCALE_RADIUS = 6;
    private int bigScaleRadius = DEFAULT_BING_SCALE_RADIUS; // 大刻度半径
    private int smallScaleRadius = DEFAULT_SMALL_SCALE_RADIUS; // 小刻度半径

    private final int redColorBigScaleCount = 3;
    private int redColorBigScaleCountIndex = redColorBigScaleCount;

    private int index = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        ALog.getInstance().d(getWidth() + "  " + getHeight() + " " + -SWEEPANGLE / divide);
        RectF rectF = new RectF(getWidth() / 2 - mRadius, getHeight() / 2 - mRadius,
                getWidth() / 2 + mRadius, getHeight() / 2 + mRadius);
        canvas.drawArc(rectF, 90 + (360 - SWEEPANGLE) / 2, SWEEPANGLE, false, bigScaleWhitePaint);
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.rotate(-210);
        for (int i = 0; i <= divide; i++) {
            if (index % divideScale == 0) {
                bigScaleWhitePaint.setStrokeWidth(5);
                if (redColorBigScaleCountIndex > 0) {
                    bigScaleWhitePaint.setColor(Color.RED);
                    redColorBigScaleCountIndex--;
                } else {
                    bigScaleWhitePaint.setColor(Color.WHITE);
                }
                canvas.drawLine(mRadius - bigScaleRadius, 0, mRadius, 0, bigScaleWhitePaint);
            } else {
                bigScaleWhitePaint.setStrokeWidth(2);
                canvas.drawLine(mRadius - smallScaleRadius, 0, mRadius, 0, bigScaleWhitePaint);
            }
            index++;
            canvas.rotate(SWEEPANGLE / divide);
        }

        redColorBigScaleCountIndex = redColorBigScaleCount;
        index = 0;
        canvas.restore();
    }

    private Paint bigScaleWhitePaint;

    private void initPaint() {
        bigScaleWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bigScaleWhitePaint.setStyle(Paint.Style.STROKE);
        bigScaleWhitePaint.setStrokeWidth(2);
        bigScaleWhitePaint.setColor(Color.WHITE);
    }

    /**
     * 依圆心坐标，半径，扇形角度，计算出扇形终射线与圆弧交叉点的xy坐标
     */
    public float[] getCoordinatePoint(int radius, float cirAngle) {
        float[] point = new float[2];
        double arcAngle = Math.toRadians(cirAngle); //将角度转换为弧度
        if (cirAngle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (cirAngle > 90 && cirAngle < 180) {
            arcAngle = Math.PI * (180 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (cirAngle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (cirAngle > 180 && cirAngle < 270) {
            arcAngle = Math.PI * (cirAngle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (cirAngle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - cirAngle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }
        ALog.getInstance().d("radius=" + radius + ",cirAngle=" + cirAngle + ",point[0]=" + point[0] + ",point[1]=" + point[1]);
        return point;
    }

}
