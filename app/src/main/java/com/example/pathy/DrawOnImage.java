package com.example.pathy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;

import androidx.core.content.ContextCompat;

public class DrawOnImage
{
    static Drawable drawOnImage(Context con)
    {
        Drawable d = ContextCompat.getDrawable(con, R.drawable.union_floor_2);
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(d.getBounds().width()/2, d.getBounds().height()/2, 1000, paint);
        d.draw(canvas);
        return d;
    }
}
