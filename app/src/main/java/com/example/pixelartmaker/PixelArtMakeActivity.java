package com.example.pixelartmaker;

import static com.example.pixelartmaker.ColorPickerActivity.b;
import static com.example.pixelartmaker.ColorPickerActivity.g;
import static com.example.pixelartmaker.ColorPickerActivity.r;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;


public class PixelArtMakeActivity extends AppCompatActivity implements View.OnClickListener {
    SquareView squareView;
    private int clickCount;
    private static final int CLICK_TIMES = 2;
    public static String colorR;
    public static String colorG;
    public static String colorB;
    public static boolean forFlg;
    private  boolean gridClear;

    View pal1;
    View pal2;
//    public Button pal1;
//    public Button pal2;
    public GradientDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_art_make);

        squareView = this.findViewById(R.id.squareView);

        squareView.gridClear(false);
        gridClear = false;

        pal1 = (Button)findViewById(R.id.pal1);
        pal2 = (Button)findViewById(R.id.pal2);

        pal1.setOnClickListener((View.OnClickListener) this);
        pal2.setOnClickListener((View.OnClickListener) this);


//        pal1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                clickCount++;
//
//                if (clickCount == 1) {
//                    // 選択マークをつける
//                    drawable = (GradientDrawable)pal1.getBackground();
//                    drawable.mutate();
//                    drawable.setStroke(10, Color.RED);
//
//                    // パレットの色で描画できるようにする
//                }
//
//                // ボタンを2回押すとカラーピッカーActivityに移動
//                if (clickCount >= CLICK_TIMES) {
//                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
//                    intent.putExtra("colorR", String.valueOf(r));
//                    intent.putExtra("colorG", String.valueOf(g));
//                    intent.putExtra("colorB", String.valueOf(b));
//
//                    resultLauncher.launch(intent);
//                    clickCount = 0;
////                    squireView.delete();
//                }
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pal1:
                squareView.setPathColor(Color.RED);
                clickCount++;

                if (clickCount == 1) {
                    // 選択マークをつける
                    drawable = (GradientDrawable)pal1.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // パレットの色で描画できるようにする
                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (clickCount >= CLICK_TIMES) {
                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    clickCount = 0;
                    SquareView.count = 0;
//                    squireView.delete();
                }
                break;

            case R.id.pal2:
//                if (gridClear) {
//                    squareView.gridClear(false);
//                    gridClear = false;
//                } else {
//                    squareView.gridClear(true);
//                    gridClear = true;
//                }

                try {
                    squareView.saveAsPngImage(squareView.masterBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                squareView.gridClearFlg = false;
//                devCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                devBitmap.eraseColor(Color.TRANSPARENT);

//                squareView.setPathColor(Color.RED);
//                clickCount++;
//
//                if (clickCount == 1) {
//                    // 選択マークをつける
//                    drawable = (GradientDrawable)pal2.getBackground();
//                    drawable.mutate();
//                    drawable.setStroke(10, Color.RED);
//
//                    // パレットの色で描画できるようにする
//                }
//
//                // ボタンを2回押すとカラーピッカーActivityに移動
//                if (clickCount >= CLICK_TIMES) {
//                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
//                    intent.putExtra("colorR", String.valueOf(r));
//                    intent.putExtra("colorG", String.valueOf(g));
//                    intent.putExtra("colorB", String.valueOf(b));
//
//                    resultLauncher.launch(intent);
//                    clickCount = 0;
////                    squireView.delete();
//                }
                break;
            default:
                break;
        }
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if(result.getResultCode() == RESULT_OK){//SubActivityのsetResultでRESULT_OKされていれば処理を行います
            //データを受け取ったあとの処理
            Intent resIntent = result.getData();
//            color = resIntent.getStringExtra("color", "");
            colorR = resIntent.getStringExtra("colorR");
            colorG = resIntent.getStringExtra("colorG");
            colorB = resIntent.getStringExtra("colorB");
            Log.i("colorR", "" + colorR);
            Log.i("colorG", "" + colorG);
            Log.i("colorB", "" + colorB);

            // forフラグ
            forFlg = true;
        }
    });
}
