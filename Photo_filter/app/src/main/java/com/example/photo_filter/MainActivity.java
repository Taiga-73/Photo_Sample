package com.example.photo_filter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageFrame = findViewById(R.id.cat);
        Button rotateButton = findViewById(R.id.rotate);
        Button monoButton = findViewById(R.id.mono);
        Button resetButton = findViewById(R.id.reset);
        Button inversionButton = findViewById(R.id.inversion);
        Button sepiaButton = findViewById(R.id.sepia);
        Button saveButton = findViewById(R.id.save);

        /* bitmap1は元画像 */
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.cat);

        rotateButton.setOnClickListener((OnClickListener)(new OnClickListener(){

            /* bitmap2は表示中のBitmap */
            Bitmap bitmap2 = Bitmap.createBitmap(bitmap1);

            public final void onClick(View it) {

                /* Matrixオブジェクト生成 */
                Matrix matrix = new Matrix();

                /* 画像のサイズを取得、90度回転 */
                int imageWidth = bitmap2.getWidth();
                int imageHeight = bitmap2.getHeight();
                matrix.setRotate(90, imageWidth/2, imageHeight/2);

                /* bitmap2（表示中の画像オブジェクト）に
                    回転行列で回転した画像オブジェクトbitmap3を生成 */
                Bitmap bitmap3 = Bitmap.createBitmap(bitmap2, 0, 0, imageWidth, imageHeight, matrix, true);

                /* bitmap3をImageViewで表示 */
                imageFrame.setImageBitmap(bitmap3);

                bitmap2 = bitmap3;
            }
        }));

        sepiaButton.setOnClickListener((OnClickListener)(new OnClickListener() {
            public void onClick(View it) {
                float[] value = new float[]{0.393F, 0.769F, 0.189F, 0.0F, 0.0F, 0.349F, 0.686F, 0.168F, 0.0F, 0.0F, 0.272F, 0.534F, 0.131F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
                ColorMatrix matrix = new ColorMatrix();
                matrix.set(value);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageFrame.setColorFilter((ColorFilter)filter);
            }
        }));

        inversionButton.setOnClickListener((OnClickListener)(new OnClickListener() {
            public void onClick(View it) {
                float[] value = new float[]{-1.0F, 0.0F, 0.0F, 0.0F, 255.0F, 0.0F, -1.0F, 0.0F, 0.0F, 255.0F, 0.0F, 0.0F, -1.0F, 0.0F, 255.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
                ColorMatrix matrix = new ColorMatrix();
                matrix.set(value);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageFrame.setColorFilter((ColorFilter)filter);
            }
        }));

        monoButton.setOnClickListener((OnClickListener)(new OnClickListener() {
            public void onClick(View it) {
                float[] value = new float[]{0.33F, 0.33F, 0.33F, 0.0F, 0.0F, 0.33F, 0.33F, 0.33F, 0.0F, 0.0F, 0.33F, 0.33F, 0.33F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
                ColorMatrix matrix = new ColorMatrix();
                matrix.set(value);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageFrame.setColorFilter((ColorFilter)filter);
            }
        }));

        resetButton.setOnClickListener((OnClickListener)(new OnClickListener() {
            public void onClick(View it) {
                imageFrame.setImageBitmap((Bitmap)bitmap1);
                ColorMatrix matrix = new ColorMatrix();
                matrix.reset();
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageFrame.setColorFilter((ColorFilter)filter);
                Bitmap bitmap2 = (Bitmap)bitmap1;
            }
        }));

        saveButton.setOnClickListener((OnClickListener)(new OnClickListener() {
            public void onClick(View it) {
                try {
                    Calendar c = Calendar.getInstance();
                    String s = c.get(Calendar.YEAR)
                            + "_" + (c.get(Calendar.MONTH)+1)
                            + "_" + c.get(Calendar.DAY_OF_MONTH)
                            + "_" + c.get(Calendar.HOUR_OF_DAY)
                            + "_" + c.get(Calendar.MINUTE)
                            + "_" + c.get(Calendar.SECOND)
                            + "_" + c.get(Calendar.MILLISECOND)
                            + ".png";
                    File extStrageDir =
                            Environment.getExternalStorageDirectory();
                    File file = new File(
                            extStrageDir.getAbsolutePath()
                                    + "/" + Environment.DIRECTORY_DCIM,
                            s);
                    FileOutputStream outStream = new FileOutputStream(file);
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void saveBitmap(Bitmap saveImage) throws IOException {

        final String SAVE_DIR = "/MyPhoto/";
        File file = new File(Environment.getExternalStorageDirectory().getPath() + SAVE_DIR);
        try{
            if(!file.exists()){
                file.mkdir();
            }
        }catch(SecurityException e){
            e.printStackTrace();
        }

        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = fileNameDate.format(mDate) + ".jpg";
        String AttachName = file.getAbsolutePath() + "/" + fileName;

        try {
            FileOutputStream out = new FileOutputStream(AttachName);
            saveImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        // save index
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put("_data", AttachName);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}