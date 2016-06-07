package com.example.linh.paint;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
    private LinearLayout menubg;
    private LinearLayout menuText;
    ValueAnimator mAnimator;
    ValueAnimator mAnimatorb;
    boolean changeBG = false;
    boolean menubariSVisiable = true;
    RelativeLayout relativeLayout;
    ImageView imageView;
    Button butonAddText;
    EditText textAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout ) findViewById(R.id.test);
        butonAddText = (Button) findViewById(R.id.buttonAddText);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonErase = (Button) findViewById(R.id.buttonEarse);
        menubg = (LinearLayout) findViewById(R.id.menubar);
        menuText = (LinearLayout) findViewById(R.id.menuText);
        TextView textView = new TextView(this);
        textView.setText("New text");
        textAdd = (EditText) findViewById(R.id.textAdd);
        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        butonAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = new TextView(MainActivity.this);
                relativeLayout.addView(tv);
                tv.setText(textAdd.getText().toString());
                collapseAddText();
            }
        });
        buttonPencil = (Button) findViewById(R.id.buttonPencil);
        buttonPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuText.setVisibility(View.VISIBLE);
                mAnimatorb.start();

            }
        });
        buttonExport = (Button) findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }

        });
        buttonBg = (Button) findViewById(R.id.buttonBg);
        buttonBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menubariSVisiable)
                {
                    menubg.setVisibility(View.VISIBLE);
                    mAnimator.start();
                    menubariSVisiable = false;
                }
                else {
                    collapse();
                    menubariSVisiable = true;
                }

            }
        });
        menubg.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        menubg.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        menubg.setVisibility(View.GONE);

                        final int widthSpec =     View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        menubg.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(0,
                                menubg.getMeasuredHeight());
                        return true;
                    }
                });
        menuText.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        menuText.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        menuText.setVisibility(View.GONE);

                        final int widthSpec =     View.MeasureSpec.makeMeasureSpec(
                                0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec
                                .makeMeasureSpec(0,
                                        View.MeasureSpec.UNSPECIFIED);
                        menuText.measure(widthSpec, heightSpec);

                        mAnimatorb = slideAnimatorb(0,
                                menuText.getMeasuredHeight());
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

                ViewGroup.LayoutParams layoutParams = menubg
                        .getLayoutParams();
                layoutParams.height = value;
                menubg.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private ValueAnimator slideAnimatorb(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new     ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = menuText
                        .getLayoutParams();
                layoutParams.height = value;
                menuText.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
    private void collapseAddText()
    {
        int finalHeight = menuText.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                menuText.setVisibility(View.GONE);
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
    private void collapse() {
        int finalHeight = menubg.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                menubg.setVisibility(View.GONE);
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
        relativeLayout.setDrawingCacheEnabled(true);
        Bitmap b = relativeLayout.getDrawingCache();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        String path = Environment.getExternalStorageDirectory().toString();
        OutputStream fOut = null;
        File file = new File(path,
                "android_drawing_app.png");

        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
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
                        Bitmap bmp = BitmapFactory.decodeFile(filePatch);
                        imageView.setImageBitmap(bmp);
                    }
                    else
                    {
                        Bitmap bmp = BitmapFactory.decodeFile(filePatch);
                        imageView.setImageBitmap(bmp);
                    }
                    cursor.close();
                }
        }
    }

}
