package com.example.linh.paint;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;



import es.dmoral.coloromatic.ColorOMaticDialog;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.OnColorSelectedListener;
import es.dmoral.coloromatic.colormode.ColorMode;

public class MainActivity extends AppCompatActivity {
    private Button buttonBg;
    private Button buttonPencil;
    private Button buttonErase;
    private Button buttonExport;
    private Button buttonChangeBg;
    private Button buttonNewBg;
    private File root;
    private static final int SELECTED_PICTURE = 1 ;
    private File imagePath;
    private LinearLayout menubar;
    ValueAnimator mAnimator;
    boolean changeBG = false;
    boolean menubariSVisiable = true;
    RelativeLayout relativeLayout;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout                                                                                                        ) findViewById(R.id.test);
//        drawingView = (SimpleDrawingView) findViewById(R.id.drawing);
        buttonErase = (Button) findViewById(R.id.buttonEarse);
        menubar= (LinearLayout) findViewById(R.id.menubar);
        TextView textView = new TextView(this);
        textView.setText("New text");
        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonPencil = (Button) findViewById(R.id.buttonPencil);
        buttonPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonExport = (Button) findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = new File(Environment.getExternalStorageDirectory() + "/data/screenshot.png");
                FileOutputStream fos;
                try
                {

                }
                catch (FileNotFoundException e)
                {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                catch (IOException e)
                {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
            }

        });
        buttonBg = (Button) findViewById(R.id.buttonBg);
        buttonBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menubariSVisiable)
                {
                    menubar.setVisibility(View.VISIBLE);
                    mAnimator.start();
                    menubariSVisiable = false;
                }
                else {
                    collapse();
                    menubariSVisiable = true;
                }

            }
        });
        menubar.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        menubar.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        menubar.setVisibility(View.GONE);

                        final int widthSpec =     View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        menubar.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0,
                                menubar.getMeasuredHeight());
                        return true;
                    }
                });
        buttonChangeBg = (Button) findViewById(R.id.buttonChangeBG);
        buttonChangeBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBG = true;
                displayDialogOpenBg();
            }
        });
        buttonNewBg = (Button) findViewById(R.id.buttonNewBG);
        buttonNewBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBG = false;
                displayDialogOpenBg();
                collapse();
            }
        });
    }
    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new     ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = menubar
                        .getLayoutParams();
                layoutParams.height = value;
                menubar.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void collapse() {
        int finalHeight = menubar.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                menubar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private void shareIt() {
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path,
                "android_drawing_app.png");
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (Exception e) {

        }

        try {
            fOut = new FileOutputStream(file);
        } catch (Exception e) {

        }


        Intent sharingIntent = new Intent();
        sharingIntent.setAction(Intent.ACTION_SEND);
        sharingIntent.setType("image/png");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));

    }

    private void displayDialogOpenBg() {

                Intent i  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,SELECTED_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();
                    String[]projection={MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePatch = cursor.getString(columnIndex);
                    if(changeBG)
                    {

                    }
                    else
                    {
                    }
                    cursor.close();
                }
        }
    }

}
