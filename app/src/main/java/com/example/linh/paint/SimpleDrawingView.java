package com.example.linh.paint;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;

/**
 * Created by linh on 6/6/2016.
 */
public class SimpleDrawingView extends View {
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
                setupDrawing();
        File pathName = Environment.getExternalStorageDirectory() ;
        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeFile(pathName + "/Download/zzz");
        BitmapDrawable bd = new BitmapDrawable(res, bitmap);
        setBackground(bd);
    }

    private void setupDrawing(){

        }
    }



