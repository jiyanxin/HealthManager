package com.example.jiyanxin.loginui.viewitem;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.jiyanxin.loginui.R;

import java.util.ArrayList;

/**
 * Created by JIYANXIN on 2017/4/14.
 */

public class LineGraphicView extends View {

    private  int CIRCLE_SIZE;

    public static enum Linestyle
    {
        Line, Curve
    }

    public static enum CircleSize{
        Big,Mid,Min
    }

    public  Linestyle mStyle = Linestyle.Line;

    private Paint mPaint;
    public int color_x;
    public int color_y;
    public int color_paint;
    public float width_x;
    public float width_y;
    public float width_paint;

    private Context mContext;
    private Resources res;
    private DisplayMetrics dm;

    /**
     * data
     */

    private int canvasHeight;
    private int canvasWidth;
    private int bheight = 0;
    private int blwidh;
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private int minValue=0;
    private int maxValue;
    private double k;
    /**
     * Y轴间距值
     */
    private int averageValue;
    private int marginTop = 40;
    private int marginBottom = 80;

    /**
     * 曲线上总点数
     */
    public Point[] mPoints;

    /**
     * 纵坐标值(坐标点，其长度代表坐标点个数)
     */
    private ArrayList<Double> yRawData;
    /**
     * 横坐标值
     */
    private ArrayList<String> xRawDatas;  //x坐标的标注点
    private ArrayList<Integer> xList = new ArrayList<>();// 记录每个x的值
    private int spacingHeight;

    public LineGraphicView(Context context)
    {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView()
    {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (isMeasure)
        {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            if (bheight == 0)
                bheight = (int) (canvasHeight - marginBottom);
            blwidh = dip2px(30);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mPaint.setColor(color_x);
        mPaint.setStrokeWidth(width_x);
        drawAllXLine(canvas);
        // 画直线（纵向）
        mPaint.setColor(color_y);
        mPaint.setStrokeWidth(width_y);
        drawAllYLine(canvas);
        // 点的操作设置
        mPoints = getPoints();

        mPaint.setColor(color_paint);
        mPaint.setStrokeWidth(width_paint);
        mPaint.setStrokeWidth(dip2px(2.5f));
        mPaint.setStyle(Paint.Style.STROKE);
        if (mStyle == Linestyle.Curve)
        {
            drawScrollLine(canvas);
        }
        else
        {
            drawLine(canvas);
        }

        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mPoints.length; i++)
        {
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 2, mPaint);
        }
    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
        for (int i = 0; i < spacingHeight + 1; i++)
        {
            canvas.drawLine(blwidh, bheight - (bheight / spacingHeight) * i + marginTop, blwidh + (canvasWidth - blwidh) / xRawDatas.size() * (xRawDatas.size()-1) ,
                    bheight - (bheight / spacingHeight) * i + marginTop, mPaint);// Y坐标
            drawText(String.valueOf(averageValue * i+minValue), blwidh / 4, bheight - (bheight / spacingHeight) * i + marginTop,canvas);
        }
    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas)
    {
        int width = (canvasWidth - blwidh) / xRawDatas.size() *(xRawDatas.size()-1);
        for(int i = 0; i < yRawData.size(); i++) {
            //xList.add(blwidh + (canvasWidth - blwidh) / yRawData.size() * i);
            xList.add(blwidh + width / (yRawData.size()-1) * i);
        }
        for (int i = 0; i < xRawDatas.size(); i++)
        {
            canvas.drawLine(blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i, marginTop, blwidh
                    + (canvasWidth - blwidh) / xRawDatas.size() * i, bheight + marginTop, mPaint);
            drawText(xRawDatas.get(i),blwidh + (canvasWidth - blwidh) / xRawDatas.size() * i,bheight +80,canvas);
        }
    }

    /**
     * 画曲线
     * @param canvas
     */
    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 画直线
     * @param canvas
     */
    private void drawLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    /**
     * 画坐标指示
     * @param text
     * @param x
     * @param y
     * @param canvas
     */
    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(res.getColor(R.color.color_999999));
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    private Point[] getPoints()
    {
        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++)
        {
            double temp;
            temp = yRawData.get(i);
            temp = temp -  minValue;

            int ph = bheight - (int) (bheight * (temp / (maxValue-minValue)));
            // int ph = bheight - (int) (bheight * (yRawData.get(i) / (maxValue-minValue)));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(ArrayList<Double> yRawData, ArrayList<String> xRawData,int minValue, int maxValue, int averageValue)
    {
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        this.spacingHeight = ( maxValue - minValue ) / averageValue;
        this.minValue = minValue;
        this.k = maxValue / ( maxValue - minValue );

    }

    public void setMstyle(Linestyle mStyle)
    {
        this.mStyle = mStyle;
    }

    public void setmPaintColor(int color){
        color_paint=color;
    }

    public void setmPaintColorX(int color){
        color_x=color;
    }

    public void setmPaintColorY(int color){
        color_y=color;
    }

    public void setmPaintWidth(float widthP,float widthX,float widthY){
        width_paint = widthP;
        width_x = widthX;
        width_y = widthY;
    }

    public void setCircleSize(CircleSize circleSize){
        if(circleSize == CircleSize.Big){
            CIRCLE_SIZE = 30;
        }else if(circleSize == CircleSize.Mid){
            CIRCLE_SIZE = 20;
        }else if(circleSize == CircleSize.Min){
            CIRCLE_SIZE = 10;
        }
    }

    public void setTotalvalue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setPjvalue(int averageValue)
    {
        this.averageValue = averageValue;
    }

    public void setMargint(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public void setBheight(int bheight)
    {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }

}


