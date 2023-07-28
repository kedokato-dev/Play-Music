package com.example.myapplication.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class RoundedImageView extends ImageView {
    public RoundedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if(drawable == null){
            return;
        }
        if(getWidth() == 0 || getHeight() == 0){
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        Bitmap rounddedBitMap = getRoundedCropedBitMap(bitmap, getWidth());
        canvas.drawBitmap(rounddedBitMap, 0, 0, null);

    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public static Bitmap getRoundedCropedBitMap(Bitmap bitmap, int radius){
        if(bitmap.getWidth() != radius || bitmap.getHeight() != radius){
            bitmap = Bitmap.createScaledBitmap(bitmap, radius, radius,false);
        }
        Bitmap output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();

        canvas.drawCircle(radius/2, radius/2, radius/2,paint);

        Rect rect = new Rect(0,0,radius,radius);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,rect, rect, paint );
        return output;
    }
}
