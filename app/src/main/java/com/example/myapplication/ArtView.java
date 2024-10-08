package com.example.myapplication;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class ArtView extends View {

    private Canvas canvas;
    private Paint paint;
    private Path path;
    private Bitmap bitmap;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap,0,0,paint);
        canvas.drawPath(path,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                path.lineTo(x,y);
                break;
                case MotionEvent.ACTION_MOVE:
                    path.lineTo(x,y);
                    break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path,paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    public void clear(){
        canvas.drawColor(0xffffffff, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    private final int PAINT_COLOR_DEF = 0xFF8E5DD5;
    public ArtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(PAINT_COLOR_DEF);
        paint.setStrokeWidth(30);
    }
}
