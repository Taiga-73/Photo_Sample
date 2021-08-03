package com.example.filter_sample;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.rotate).setOnClickListener(this);
        findViewById(R.id.mono).setOnClickListener(this);
        findViewById(R.id.inversion).setOnClickListener(this);
        findViewById(R.id.sepia).setOnClickListener(this);
        findViewById(R.id.reset).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        ImageView imageFrame = findViewById(R.id.cat);
        /* bitmap1は元画像 */
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        /* bitmap2は表示中のBitmap */
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1);
        if (v != null) {
            switch (v.getId()) {
                case R.id.rotate:
                    Matrix rotateMatrix = new Matrix();

                    /* 画像のサイズを取得、90*i度回転 */
                    int imageWidth = bitmap2.getWidth();
                    int imageHeight = bitmap2.getHeight();
                    int rotateDegrees = 90;
                    rotateMatrix.setRotate(rotateDegrees, imageWidth/2, imageHeight/2);

                /* bitmap2（表示中の画像オブジェクト）に
                    回転行列で回転した画像オブジェクトbitmap3を生成 */
                    Bitmap bitmap3 = Bitmap.createBitmap(bitmap2, 0, 0, imageWidth, imageHeight, rotateMatrix, true);

                    /* bitmap3をImageViewで表示 */
                    imageFrame.setImageBitmap(bitmap3);

                    bitmap2 = bitmap3;
                    break;

                case R.id.mono:
                    float[] monoValue = new float[]{0.33F, 0.33F, 0.33F, 0.0F, 0.0F, 0.33F, 0.33F, 0.33F, 0.0F, 0.0F, 0.33F, 0.33F, 0.33F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
                    ColorMatrix monoMatrix = new ColorMatrix();
                    monoMatrix.set(monoValue);
                    ColorMatrixColorFilter monoFilter = new ColorMatrixColorFilter(monoMatrix);
                    imageFrame.setColorFilter((ColorFilter)monoFilter);
                    break;

                case R.id.sepia:
                    float[] sepiaValue = new float[]{0.393F, 0.769F, 0.189F, 0.0F, 0.0F, 0.349F, 0.686F, 0.168F, 0.0F, 0.0F, 0.272F, 0.534F, 0.131F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
                    ColorMatrix sepiaMatrix = new ColorMatrix();
                    sepiaMatrix.set(sepiaValue);
                    ColorMatrixColorFilter sepiaFilter = new ColorMatrixColorFilter(sepiaMatrix);
                    imageFrame.setColorFilter((ColorFilter)sepiaFilter);
                    break;

                case R.id.inversion:
                    float[] inversionValue = new float[]{-1.0F, 0.0F, 0.0F, 0.0F, 255.0F, 0.0F, -1.0F, 0.0F, 0.0F, 255.0F, 0.0F, 0.0F, -1.0F, 0.0F, 255.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
                    ColorMatrix inversionMatrix = new ColorMatrix();
                    inversionMatrix.set(inversionValue);
                    ColorMatrixColorFilter inversionFilter = new ColorMatrixColorFilter(inversionMatrix);
                    imageFrame.setColorFilter((ColorFilter)inversionFilter);
                    break;

                case R.id.reset:
                    imageFrame.setImageBitmap((Bitmap)bitmap1);
                    ColorMatrix resetMatrix = new ColorMatrix();
                    resetMatrix.reset();
                    ColorMatrixColorFilter resetFilter = new ColorMatrixColorFilter(resetMatrix);
                    imageFrame.setColorFilter((ColorFilter)resetFilter);
                    bitmap2 = (Bitmap)bitmap1;
                    break;

                case R.id.save:
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
                        bitmap2.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }

        }
        }
}