package com.example.pixelartmaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerActivity extends AppCompatActivity
        implements TextView.OnEditorActionListener,
        View.OnFocusChangeListener,
        SeekBar.OnSeekBarChangeListener {

    TextView txtColor;
    EditText edtRed, edtGreen, edtBlue;
    SeekBar sbrRed, sbrGreen, sbrBlue;
    public static int r, g, b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        // ビューを取得する
        txtColor = (TextView) this.findViewById(R.id.txtColor);
        edtRed = (EditText) this.findViewById(R.id.edtRed);
        edtGreen = (EditText) this.findViewById(R.id.edtGreen);
        edtBlue = (EditText) this.findViewById(R.id.edtBlue);
        sbrRed = (SeekBar) this.findViewById(R.id.sbrRed);
        sbrGreen = (SeekBar) this.findViewById(R.id.sbrGreen);
        sbrBlue = (SeekBar) this.findViewById(R.id.sbrBlue);
        edtRed.setOnEditorActionListener(this);
        edtGreen.setOnEditorActionListener(this);
        edtBlue.setOnEditorActionListener(this);
        edtRed.setOnFocusChangeListener(this);
        edtGreen.setOnFocusChangeListener(this);
        edtBlue.setOnFocusChangeListener(this);
        sbrRed.setOnSeekBarChangeListener(this);
        sbrGreen.setOnSeekBarChangeListener(this);
        sbrBlue.setOnSeekBarChangeListener(this);

        sbrRed.setProgress(r);
        sbrGreen.setProgress(g);
        sbrBlue.setProgress(b);

        Button ok = (Button)findViewById(R.id.OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> colorRGB = new ArrayList<Integer>();

                // 値保存処理
                colorRGB.add(r);
                colorRGB.add(g);
                colorRGB.add(b);

                Intent resIntent = new Intent(getApplication(), PixelArtMakeActivity.class);
                resIntent.putIntegerArrayListExtra("colorRGB", (ArrayList<Integer>) colorRGB);
                setResult(Activity.RESULT_OK, resIntent);

                //　PixelArtMakeActivityに戻る
                finish();
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        try {
            int value = Integer.parseInt(v.getText().toString());  // 数値に変換
            value = Math.max(0, Math.min(255, value));
            switch (v.getId()) {
                case R.id.edtRed:
                    sbrRed.setProgress(value); // sbrRedの値を変える
                    break;
                case R.id.edtGreen:
                    sbrGreen.setProgress(value); // sbrGreenの値を変える

                    break;
                case R.id.edtBlue:
                    sbrBlue.setProgress(value); // sbrBlueの値を変える
                    break;
                default:
                    return false;
            }
            Change();
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        try {
            int value = Integer.parseInt(((TextView) v).getText().toString());  // 数値に変換
            value = Math.max(0, Math.min(255, value));
            switch (v.getId()) {
                case R.id.edtRed:
                    sbrRed.setProgress(value); // sbrRedの値を変える
                    break;
                case R.id.edtGreen:
                    sbrGreen.setProgress(value); // sbrGreenの値を変える
                    break;
                case R.id.edtBlue:
                    sbrBlue.setProgress(value); // sbrBlueの値を変える
                    break;
                default:
                    return;
            }
            Change();
        } catch (Exception e) {

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sbrRed:
                edtRed.setText(String.format("%1$d", progress)); // 値が変わったら、edtRedのテキストも変える
                break;
            case R.id.sbrGreen:
                edtGreen.setText(String.format("%1$d", progress)); // 値が変わったら、edtGreenのテキストも変える
                break;
            case R.id.sbrBlue:
                edtBlue.setText(String.format("%1$d", progress));   // 値が変わったら、edtBlueのテキストも変える
                break;
            default:
                return;
        }
        Change();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void Change() {
        try {
            r = Integer.parseInt(edtRed.getText().toString());
            g = Integer.parseInt(edtGreen.getText().toString());
            b = Integer.parseInt(edtBlue.getText().toString());
            txtColor.setText(String.format("#%1$02X%2$02X%3$02X", r, g, b));
            txtColor.setBackgroundColor(Color.rgb(r, g, b));
        } catch (Exception ex) {
        }
    }
}