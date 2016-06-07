package com.example.linh.paint;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.coloromatic.ColorOMaticDialog;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.OnColorSelectedListener;
import es.dmoral.coloromatic.colormode.ColorMode;

public class MainActivity extends AppCompatActivity {
    private Button buttonBg;
    private Button buttonPencil;
    private Button buttonErase;
    private Button buttonExport;
    private SimpleDrawingView drawingView;
    private File root;
    private ListView listviewFolder;
    private TextView textFolder;
    private Button buttonParentFolder;
    private File curFolder;
    private List<String> fileList = new ArrayList<String>();
    private static final int SELECTED_PICTURE = 1 ;
    private ShareActionProvider mShareActionProvider;
    private File imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        setContentView(R.layout.activity_main);
        drawingView = (SimpleDrawingView) findViewById(R.id.drawing);
        buttonErase = (Button) findViewById(R.id.buttonEarse);
        buttonErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawingView.setErase(true);
            }
        });
        buttonPencil = (Button) findViewById(R.id.buttonPencil);
        buttonPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorOMaticDialog.Builder()
                        .initialColor(Color.WHITE)
                        .colorMode(ColorMode.ARGB) // RGB, ARGB, HVS
                        .indicatorMode(IndicatorMode.HEX) // HEX or DECIMAL; Note that using HSV with IndicatorMode.HEX is not recommended
                        .onColorSelected(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(@ColorInt int i) {
                                drawingView.setErase(false);
                                drawingView.setColor(i);
                            }
                        })
                        .showColorIndicator(true) // Default false, choose to show text indicator showing the current color in HEX or DEC (see images) or not
                        .create()
                        .show(getSupportFragmentManager(), "ColorOMaticDialog");

            }
        });
        buttonExport = (Button) findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setDrawingCacheEnabled(true);
                imagePath = new File(Environment.getExternalStorageDirectory() + "/data/screenshot.png");
                FileOutputStream fos;
                try
                {
                    Bitmap bitmap = drawingView.getDrawingCache();
                    fos = new FileOutputStream(imagePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    shareIt();

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
               displayDialogOpenBg();
            }
        });
    }

    private void shareIt() {
        drawingView.setDrawingCacheEnabled(true);
        drawingView.invalidate();
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
        if (drawingView.getDrawingCache() == null) {
        }
        drawingView.getDrawingCache()
                .compress(Bitmap.CompressFormat.JPEG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {

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
                    drawingView.startNew();
                    drawingView.setupDrawing(filePatch);
                    cursor.close();
                }
        }
    }

    private void listDir(File f) {
        if(f.equals(root))
        {
            buttonParentFolder.setEnabled(false);
        }
        else
        {
            buttonParentFolder.setEnabled(true);
        }

        curFolder = f;
        textFolder.setText(f.getPath());
        File[] files  = f.listFiles();
        fileList.clear();
        for(File file : files)
        {
            fileList.add(file.getPath());
        }
        ArrayAdapter<String> direciryList = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fileList);
        listviewFolder.setAdapter(direciryList);
    }


}
