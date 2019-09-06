package com.misfits.mental.findflix;

/**
 * Created by Mahe on 7/16/2016.
 */
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Mahe on 6/9/2016.
 */
public class TriangleImageView extends ImageView {

    static int widt,heig;

    public TriangleImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        widt = metrics.widthPixels;
        heig = metrics.heightPixels;
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

        int w = getWidth(), h = getHeight();

        Bitmap roundBitmap = getRoundedCroppedBitmap(bitmap, w);
        canvas.drawBitmap(roundBitmap, 0, 0, null);

    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, widt, heig,
                    false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(),
                finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(Color.rgb(240,0,16),48);
        paint.setColorFilter(filter);
        paint.setAntiAlias(true);
        //paint.setColorFilter();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());


        Point point1_draw = new Point((int)(widt*0.72), 0);
        Point point2_draw = new Point(widt, 0);
        Point point3_draw = new Point(widt, heig);
        Point point4_draw = new Point(0,heig);
        Point point5_draw = new Point(0, (int)(heig*0.72));

        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point4_draw.x, point4_draw.y);
        path.lineTo(point5_draw.x, point5_draw.y);
        path.lineTo(point1_draw.x, point1_draw.y);
        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

}