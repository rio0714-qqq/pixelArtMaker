package com.example.pixelartmaker;

import static com.example.pixelartmaker.ColorPickerActivity.b;
import static com.example.pixelartmaker.ColorPickerActivity.g;
import static com.example.pixelartmaker.ColorPickerActivity.r;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelartmaker.fragment.CustomDialogFragment;

import java.io.IOException;
import java.util.ArrayList;


public class PixelArtMakeActivity extends AppCompatActivity implements View.OnClickListener {
    SquareView squareView;

    // ピクセル指定
    String px16;
    String px24;
    String px32;
    String px48;

    // クリック数カウント
    public int pal0ClickCount;
    public int pal1ClickCount;
    public int pal2ClickCount;
    public int pal3ClickCount;
    public int pal4ClickCount;
    public int pal5ClickCount;
    public int pal6ClickCount;
    public int pal7ClickCount;
    public int pal8ClickCount;
    public int pal9ClickCount;
    public int pal10ClickCount;
    public int pal11ClickCount;

    private static final int CLICK_TIMES = 2;

    // 取得した色のRGB
    public static ArrayList<Integer> colorRGB;
    // パレットのRGB
    ArrayList<Integer> pal0ColorRGB;
    ArrayList<Integer> pal1ColorRGB;
    ArrayList<Integer> pal2ColorRGB;
    ArrayList<Integer> pal3ColorRGB;
    ArrayList<Integer> pal4ColorRGB;
    ArrayList<Integer> pal5ColorRGB;
    ArrayList<Integer> pal6ColorRGB;
    ArrayList<Integer> pal7ColorRGB;
    ArrayList<Integer> pal8ColorRGB;
    ArrayList<Integer> pal9ColorRGB;
    ArrayList<Integer> pal10ColorRGB;
    ArrayList<Integer> pal11ColorRGB;

    // パレットフラグ
    boolean pal[] = {false, false, false, false,
            false, false, false, false,
            false, false, false, false};

    // パレット色フラグ
    boolean palColor[] = {false, false, false, false,
            false, false, false, false,
            false, false, false, false};


    // パレット
    public View pal0, pal1, pal2, pal3, pal4, pal5, pal6, pal7, pal8, pal9, pal10, pal11;

    public GradientDrawable drawable;

    public Button gridButton;
    public Button saveButton;
    public Button homeButton;

    private  boolean gridClear;

    public static boolean forFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_art_make);

        // ピクセル選択画面から画面表示するピクセル数を取得
        px16 = null;
        px24 = null;
        px32 = null;
        px48 = null;

        Intent intent = getIntent();
        px16 = intent.getStringExtra("16px");
        px24 = intent.getStringExtra("24px");
        px32 = intent.getStringExtra("32px");
        px48 = intent.getStringExtra("48px");
//        pxInput = Integer.parseInt(intent.getStringExtra(CustomDialogFragment.inputPixel));

        if (px16 != null) {
            SquareView.numberOfCell = 16;
        } else if (px24 != null) {
            SquareView.numberOfCell = 24;
        } else if (px32 != null) {
            SquareView.numberOfCell = 32;
        } else if (px48 != null) {
            SquareView.numberOfCell = 48;
        } else {
            SquareView.numberOfCell = Integer.parseInt(CustomDialogFragment.inputPixel);
        }

        squareView = this.findViewById(R.id.squareView);
        squareView.gridClear(false);
        gridClear = false;

        // パレットボタン
        pal0 = (Button)findViewById(R.id.pal0);
        pal1 = (Button)findViewById(R.id.pal1);
        pal2 = (Button)findViewById(R.id.pal2);
        pal3 = (Button)findViewById(R.id.pal3);
        pal4 = (Button)findViewById(R.id.pal4);
        pal5 = (Button)findViewById(R.id.pal5);
        pal6 = (Button)findViewById(R.id.pal6);
        pal7 = (Button)findViewById(R.id.pal7);
        pal8 = (Button)findViewById(R.id.pal8);
        pal9 = (Button)findViewById(R.id.pal9);
        pal10 = (Button)findViewById(R.id.pal10);
        pal11 = (Button)findViewById(R.id.pal11);
        // ガイド線
        gridButton = (Button)findViewById(R.id.grid_button);
        // セーブボタン
        saveButton = (Button)findViewById(R.id.save_button);
        // ホームボタン
        homeButton = (Button)findViewById(R.id.home_button);


        // ClickListener設定
        pal0.setOnClickListener((View.OnClickListener) this);
        pal1.setOnClickListener((View.OnClickListener) this);
        pal2.setOnClickListener((View.OnClickListener) this);
        pal3.setOnClickListener((View.OnClickListener) this);
        pal4.setOnClickListener((View.OnClickListener) this);
        pal5.setOnClickListener((View.OnClickListener) this);
        pal6.setOnClickListener((View.OnClickListener) this);
        pal7.setOnClickListener((View.OnClickListener) this);
        pal8.setOnClickListener((View.OnClickListener) this);
        pal9.setOnClickListener((View.OnClickListener) this);
        pal10.setOnClickListener((View.OnClickListener) this);
        pal11.setOnClickListener((View.OnClickListener) this);
        gridButton.setOnClickListener((View.OnClickListener) this);
        saveButton.setOnClickListener((View.OnClickListener) this);
        homeButton.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        px16 = null;
        px24 = null;
        px32 = null;
        px48 = null;
    }


    // 画面遷移後の処理
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
        if(result.getResultCode() == RESULT_OK){//SubActivityのsetResultでRESULT_OKされていれば処理を行います
            //データを受け取ったあとの処理
            Intent resIntent = result.getData();

            colorRGB = resIntent.getIntegerArrayListExtra("colorRGB");

            // パレットの色を変える パレットの場所で場合分け
            if (palColor[0]) {
                pal0ColorRGB = new ArrayList<>();
                pal0ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal0.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                        pal0ColorRGB.get(1),
                        pal0ColorRGB.get(2)));
                palColor[0] = false;
            } else if (palColor[1]) {
                pal1ColorRGB = new ArrayList<>();
                pal1ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal1.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                        pal1ColorRGB.get(1),
                        pal1ColorRGB.get(2)));
                palColor[1] = false;
            } else if (palColor[2]) {
                pal2ColorRGB = new ArrayList<>();
                pal2ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal2.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                        pal2ColorRGB.get(1),
                        pal2ColorRGB.get(2)));
                palColor[2] = false;
            } else if (palColor[3]) {
                pal3ColorRGB = new ArrayList<>();
                pal3ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal3.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                        pal3ColorRGB.get(1),
                        pal3ColorRGB.get(2)));
                palColor[3] = false;
            } else if (palColor[4]) {
                pal4ColorRGB = new ArrayList<>();
                pal4ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal4.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                        pal4ColorRGB.get(1),
                        pal4ColorRGB.get(2)));
                palColor[4] = false;
            } else if (palColor[5]) {
                pal5ColorRGB = new ArrayList<>();
                pal5ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal5.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                        pal5ColorRGB.get(1),
                        pal5ColorRGB.get(2)));
                palColor[5] = false;
            } else if (palColor[6]) {
                pal6ColorRGB = new ArrayList<>();
                pal6ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal6.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                        pal6ColorRGB.get(1),
                        pal6ColorRGB.get(2)));
                palColor[6] = false;
            } else if (palColor[7]) {
                pal7ColorRGB = new ArrayList<>();
                pal7ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal7.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                        pal7ColorRGB.get(1),
                        pal7ColorRGB.get(2)));
                palColor[7] = false;
            } else if (palColor[8]) {
                pal8ColorRGB = new ArrayList<>();
                pal8ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal8.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                        pal8ColorRGB.get(1),
                        pal8ColorRGB.get(2)));
                palColor[8] = false;
            } else if (palColor[9]) {
                pal9ColorRGB = new ArrayList<>();
                pal9ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal9.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                        pal9ColorRGB.get(1),
                        pal9ColorRGB.get(2)));
                palColor[9] = false;
            } else if (palColor[10]) {
                pal10ColorRGB = new ArrayList<>();
                pal10ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal10.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                        pal10ColorRGB.get(1),
                        pal10ColorRGB.get(2)));
                palColor[10] = false;
            } else if (palColor[11]) {
                pal11ColorRGB = new ArrayList<>();
                pal11ColorRGB.addAll(colorRGB);
                drawable = (GradientDrawable)pal11.getBackground();
                drawable.mutate();
                drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                        pal11ColorRGB.get(1),
                        pal11ColorRGB.get(2)));
                palColor[11] = false;
            }
            // forフラグ
            forFlg = true;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pal0:
                squareView.setPathColor(Color.RED);
                pal0ClickCount++;

                // ボタン一回押し
                if (pal0ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal0ColorRGB != null
                        && pal0ColorRGB.get(0) != null
                            && pal0ColorRGB.get(1) != null
                                && pal0ColorRGB.get(2) != null) {
                        r = pal0ColorRGB.get(0);
                        g = pal0ColorRGB.get(1);
                        b = pal0ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal0.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal0ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal0ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal0アクティブ
                    pal[0] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal0ClickCount >= CLICK_TIMES) {

                    // palColor0アクティブ
                    palColor[0] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal0ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal1:
                squareView.setPathColor(Color.RED);
                pal1ClickCount++;

                // ボタン一回押し
                if (pal1ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal1ColorRGB != null
                        && pal1ColorRGB.get(0) != null
                            && pal1ColorRGB.get(1) != null
                                && pal1ColorRGB.get(2) != null) {
                        r = pal1ColorRGB.get(0);
                        g = pal1ColorRGB.get(1);
                        b = pal1ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal1.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal1ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal1ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal1アクティブ
                    pal[1] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal1ClickCount >= CLICK_TIMES) {

                    // palColor1アクティブ
                    palColor[1] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal1ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal2:
                squareView.setPathColor(Color.RED);
                pal2ClickCount++;

                // ボタン一回押し
                if (pal2ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal2ColorRGB != null
                        && pal2ColorRGB.get(0) != null
                            && pal2ColorRGB.get(1) != null
                                && pal2ColorRGB.get(2) != null) {
                        r = pal2ColorRGB.get(0);
                        g = pal2ColorRGB.get(1);
                        b = pal2ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal2.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal2ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal2ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal2アクティブ
                    pal[2] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal2ClickCount >= CLICK_TIMES) {

                    // palColor2アクティブ
                    palColor[2] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal2ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal3:
                squareView.setPathColor(Color.RED);
                pal3ClickCount++;

                // ボタン一回押し
                if (pal3ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal3ColorRGB != null
                        && pal3ColorRGB.get(0) != null
                            && pal3ColorRGB.get(1) != null
                                && pal3ColorRGB.get(2) != null) {
                        r = pal3ColorRGB.get(0);
                        g = pal3ColorRGB.get(1);
                        b = pal3ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal3.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal3ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal3ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal3アクティブ
                    pal[3] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal3ClickCount >= CLICK_TIMES) {

                    // palColor3アクティブ
                    palColor[3] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal3ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal4:
                squareView.setPathColor(Color.RED);
                pal4ClickCount++;

                // ボタン一回押し
                if (pal4ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal4ColorRGB != null
                        && pal4ColorRGB.get(0) != null
                            && pal4ColorRGB.get(1) != null
                                && pal4ColorRGB.get(2) != null) {
                        r = pal4ColorRGB.get(0);
                        g = pal4ColorRGB.get(1);
                        b = pal4ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal4.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal4ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal4ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal4アクティブ
                    pal[4] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal4ClickCount >= CLICK_TIMES) {

                    // palColor4アクティブ
                    palColor[4] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal4ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal5:
                squareView.setPathColor(Color.RED);
                pal5ClickCount++;

                // ボタン一回押し
                if (pal5ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal5ColorRGB != null
                        && pal5ColorRGB.get(0) != null
                            && pal5ColorRGB.get(1) != null
                                && pal5ColorRGB.get(2) != null) {
                        r = pal5ColorRGB.get(0);
                        g = pal5ColorRGB.get(1);
                        b = pal5ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal5.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal5ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal5ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal5アクティブ
                    pal[5] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal5ClickCount >= CLICK_TIMES) {

                    // palColor5アクティブ
                    palColor[5] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal5ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal6:
                squareView.setPathColor(Color.RED);
                pal6ClickCount++;

                // ボタン一回押し
                if (pal6ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal6ColorRGB != null
                        && pal6ColorRGB.get(0) != null
                            && pal6ColorRGB.get(1) != null
                                && pal6ColorRGB.get(2) != null) {
                        r = pal6ColorRGB.get(0);
                        g = pal6ColorRGB.get(1);
                        b = pal6ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal6.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal6ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal6ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal6アクティブ
                    pal[6] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal6ClickCount >= CLICK_TIMES) {

                    // palColor6アクティブ
                    palColor[6] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal6ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal7:
                squareView.setPathColor(Color.RED);
                pal7ClickCount++;

                // ボタン一回押し
                if (pal7ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal7ColorRGB != null
                        && pal7ColorRGB.get(0) != null
                            && pal7ColorRGB.get(1) != null
                                && pal7ColorRGB.get(2) != null) {
                        r = pal7ColorRGB.get(0);
                        g = pal7ColorRGB.get(1);
                        b = pal7ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal7.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal7ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal7ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal7アクティブ
                    pal[7] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal7ClickCount >= CLICK_TIMES) {

                    // palColor7アクティブ
                    palColor[7] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal7ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal8:
                squareView.setPathColor(Color.RED);
                pal8ClickCount++;

                // ボタン一回押し
                if (pal8ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal8ColorRGB != null
                        && pal8ColorRGB.get(0) != null
                            && pal8ColorRGB.get(1) != null
                                && pal8ColorRGB.get(2) != null) {
                        r = pal8ColorRGB.get(0);
                        g = pal8ColorRGB.get(1);
                        b = pal8ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal8.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal8ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal8ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal8アクティブ
                    pal[8] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal8ClickCount >= CLICK_TIMES) {

                    // palColor8アクティブ
                    palColor[8] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal8ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal9:
                squareView.setPathColor(Color.RED);
                pal9ClickCount++;

                // ボタン一回押し
                if (pal9ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal9ColorRGB != null
                        && pal9ColorRGB.get(0) != null
                            && pal9ColorRGB.get(1) != null
                                && pal9ColorRGB.get(2) != null) {
                        r = pal9ColorRGB.get(0);
                        g = pal9ColorRGB.get(1);
                        b = pal9ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal10ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal9.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal9ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal9ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[10] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal9アクティブ
                    pal[9] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal9ClickCount >= CLICK_TIMES) {

                    // palColor9アクティブ
                    palColor[9] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal9ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal10:
                squareView.setPathColor(Color.RED);
                pal10ClickCount++;

                // ボタン一回押し
                if (pal10ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal10ColorRGB != null
                        && pal10ColorRGB.get(0) != null
                            && pal10ColorRGB.get(1) != null
                                && pal10ColorRGB.get(2) != null) {
                        r = pal10ColorRGB.get(0);
                        g = pal10ColorRGB.get(1);
                        b = pal10ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal11ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal10.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal10ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal10ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[11]) {
                        pal11.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal11.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal11ColorRGB.get(0),
                                pal11ColorRGB.get(1),
                                pal11ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal10アクティブ
                    pal[10] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal10ClickCount >= CLICK_TIMES) {

                    // palColor10アクティブ
                    palColor[10] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal10ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            case R.id.pal11:
                squareView.setPathColor(Color.RED);
                pal11ClickCount++;

                // ボタン一回押し
                if (pal11ClickCount == 1) {

                    // パレット保存用rgbに値格納
                    if (pal11ColorRGB != null
                        && pal11ColorRGB.get(0) != null
                            && pal11ColorRGB.get(1) != null
                                && pal11ColorRGB.get(2) != null) {
                        r = pal11ColorRGB.get(0);
                        g = pal11ColorRGB.get(1);
                        b = pal11ColorRGB.get(2);
                    }

                    // 他ボタンのタッチカウント初期化
                    pal0ClickCount = 0;
                    pal1ClickCount = 0;
                    pal2ClickCount = 0;
                    pal3ClickCount = 0;
                    pal4ClickCount = 0;
                    pal5ClickCount = 0;
                    pal6ClickCount = 0;
                    pal7ClickCount = 0;
                    pal8ClickCount = 0;
                    pal9ClickCount = 0;
                    pal10ClickCount = 0;

                    // 選択マークをつける
                    drawable = (GradientDrawable)pal11.getBackground();
                    drawable.mutate();
                    drawable.setStroke(10, Color.RED);

                    // 描画する色にパレットの色をセット
                    if (colorRGB != null && pal11ColorRGB != null) {
                        colorRGB.clear();
                        colorRGB.addAll(pal11ColorRGB);
                    }

                    // 他ボタンの選択マークを外す
                    if (pal[0]) {
                        pal0.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal0.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal0ColorRGB.get(0),
                                pal0ColorRGB.get(1),
                                pal0ColorRGB.get(2)));
                        pal[0] = false;
                    } else if (pal[1]) {
                        pal1.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal1.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal1ColorRGB.get(0),
                                pal1ColorRGB.get(1),
                                pal1ColorRGB.get(2)));
                        pal[1] = false;
                    } else if (pal[2]) {
                        pal2.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal2.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal2ColorRGB.get(0),
                                pal2ColorRGB.get(1),
                                pal2ColorRGB.get(2)));
                        pal[2] = false;
                    } else if (pal[3]) {
                        pal3.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal3.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal3ColorRGB.get(0),
                                pal3ColorRGB.get(1),
                                pal3ColorRGB.get(2)));
                        pal[3] = false;
                    } else if (pal[4]) {
                        pal4.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal4.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal4ColorRGB.get(0),
                                pal4ColorRGB.get(1),
                                pal4ColorRGB.get(2)));
                        pal[4] = false;
                    } else if (pal[5]) {
                        pal5.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal5.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal5ColorRGB.get(0),
                                pal5ColorRGB.get(1),
                                pal5ColorRGB.get(2)));
                        pal[5] = false;
                    } else if (pal[6]) {
                        pal6.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal6.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal6ColorRGB.get(0),
                                pal6ColorRGB.get(1),
                                pal6ColorRGB.get(2)));
                        pal[6] = false;
                    } else if (pal[7]) {
                        pal7.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal7.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal7ColorRGB.get(0),
                                pal7ColorRGB.get(1),
                                pal7ColorRGB.get(2)));
                        pal[7] = false;
                    } else if (pal[8]) {
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        pal8.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal8.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal8ColorRGB.get(0),
                                pal8ColorRGB.get(1),
                                pal8ColorRGB.get(2)));
                        pal[8] = false;
                    } else if (pal[9]) {
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        pal9.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal9.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal9ColorRGB.get(0),
                                pal9ColorRGB.get(1),
                                pal9ColorRGB.get(2)));
                        pal[9] = false;
                    } else if (pal[10]) {
                        pal10.setBackgroundResource(R.drawable.button_color_palette);
                        drawable = (GradientDrawable)pal10.getBackground();
                        drawable.mutate();
                        drawable.setColor(Color.rgb(pal10ColorRGB.get(0),
                                pal10ColorRGB.get(1),
                                pal10ColorRGB.get(2)));
                        pal[11] = false;
                    }

                    // pal11アクティブ
                    pal[11] = true;

                }

                // ボタンを2回押すとカラーピッカーActivityに移動
                if (pal11ClickCount >= CLICK_TIMES) {

                    // palColor11アクティブ
                    palColor[11] = true;

                    Intent intent = new Intent(getApplication(), ColorPickerActivity.class);
                    intent.putExtra("colorR", String.valueOf(r));
                    intent.putExtra("colorG", String.valueOf(g));
                    intent.putExtra("colorB", String.valueOf(b));

                    resultLauncher.launch(intent);
                    pal11ClickCount = 0;
                    SquareView.count = 0;

                }
                break;

            // グリッド線操作
            case R.id.grid_button:
                if (gridClear) {
                    // ボタンテキスト : ガイド線を消す
                    gridButton.setText("ガイド線を消す");
                    squareView.gridClear(false);
                    gridClear = false;
                } else {
                    // ボタンテキスト : ガイド線をつける
                    gridButton.setText("ガイド線をつける");
                    squareView.gridClear(true);
                    gridClear = true;
                }
                break;

            // セーブボタン操作
            case R.id.save_button:
                try {
                    squareView.saveAsPngImage(squareView.masterBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            // ホーム画面に戻る
            case R.id.home_button:
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
