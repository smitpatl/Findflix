package com.misfits.mental.findflix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Mahe on 7/16/2016.
 */
public class ThumbImageView extends ImageView {

    static int widt,heig;

    public ThumbImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        Log.d("I", heig + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, 300);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, 300, bitmap.getHeight()*300/bitmap.getWidth(),
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(Color.rgb(240, 0, 16),48);
        //paint.setColorFilter(filter);
        paint.setAntiAlias(true);
        //paint.setColorFilter();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());


        Point point1_draw = new Point(0, 0);
        Point point2_draw = new Point(finalBitmap.getWidth(), 0);
        Point point3_draw = new Point(finalBitmap.getWidth(), (int)(finalBitmap.getHeight()*0.8));
        Point point4_draw = new Point(0,finalBitmap.getHeight());
        Point point5_draw = new Point(0,0);

        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point4_draw.x, point4_draw.y);
        path.lineTo(point5_draw.x, point5_draw.y);
        path.lineTo(point1_draw.x, point1_draw.y);
        path.close();
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(finalBitmap, rect, rect, paint);
        Paint paintTopRight = new Paint();
        // canvas.drawPaint(paintTopRight);  // don't do that
        // Use Color.parseColor to define HTML colors
        paintTopRight.setColor(Color.argb(48,240,0,16));
        paintTopRight.setAntiAlias(true);
        Path secpath = new Path();
        Point secpoint1 = new Point(0,(int)(finalBitmap.getHeight()*0.5));
        Point secpoint2 = new Point(finalBitmap.getWidth(),(int)(finalBitmap.getHeight()*0.75));
        Point secpoint3 = new Point(finalBitmap.getWidth(),finalBitmap.getHeight());
        Point secpoint4 = new Point(0,finalBitmap.getHeight());
        secpath.moveTo(secpoint1.x, secpoint1.y);
        secpath.lineTo(secpoint2.x, secpoint2.y);
        secpath.lineTo(secpoint3.x, secpoint3.y);
        secpath.lineTo(secpoint4.x, secpoint4.y);
        secpath.lineTo(secpoint1.x, secpoint1.y);
        secpath.close();
        canvas.drawPath(secpath,paintTopRight);
        Paint paintTopLeft = new Paint();
        // canvas.drawPaint(paintTopRight);  // don't do that
        // Use Color.parseColor to define HTML colors
        paintTopLeft.setColor(Color.argb(144,240,0,16));
        paintTopLeft.setAntiAlias(true);
        Path thipath = new Path();
        Point thipoint1 = new Point(0,(int)(finalBitmap.getHeight()*0.7));
        Point thipoint2 = new Point(finalBitmap.getWidth(),(int)(finalBitmap.getHeight()*0.95));
        Point thipoint3 = new Point(finalBitmap.getWidth(),finalBitmap.getHeight());
        Point thipoint4 = new Point(0,finalBitmap.getHeight());
        thipath.moveTo(thipoint1.x, thipoint1.y);
        thipath.lineTo(thipoint2.x, thipoint2.y);
        thipath.lineTo(thipoint3.x, thipoint3.y);
        thipath.lineTo(thipoint4.x, thipoint4.y);
        thipath.lineTo(secpoint1.x, secpoint1.y);
        thipath.close();
        canvas.drawPath(thipath,paintTopLeft);
        Paint paintTop = new Paint();
        // canvas.drawPaint(paintTopRight);  // don't do that
        // Use Color.parseColor to define HTML colors
        paintTop.setColor(Color.argb(255,255,255,255));
        paintTop.setAntiAlias(true);
        Path foupath = new Path();
        Point foupoint1 = new Point(0,finalBitmap.getHeight());
        Point foupoint2 = new Point(finalBitmap.getWidth(),(int)(finalBitmap.getHeight()*0.80));
        Point foupoint3 = new Point(finalBitmap.getWidth(),finalBitmap.getHeight());
        foupath.moveTo(foupoint1.x, foupoint1.y);
        foupath.lineTo(foupoint2.x, foupoint2.y);
        foupath.lineTo(foupoint3.x, foupoint3.y);
        foupath.lineTo(foupoint1.x, foupoint1.y);
        foupath.close();
        canvas.drawPath(foupath,paintTop);

        return output;
    }

}