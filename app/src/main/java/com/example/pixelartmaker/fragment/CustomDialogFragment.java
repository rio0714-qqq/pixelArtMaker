package com.example.pixelartmaker.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.pixelartmaker.PixelArtMakeActivity;
import com.example.pixelartmaker.R;

public class CustomDialogFragment extends DialogFragment {

    private EditText input;
    private TextView chengeMessage;
    public static String inputPixel;
    CustomDialogListener listener;

    //
    public interface CustomDialogListener {
        void getInputPixel(String inputData);
    }

    public static CustomDialogFragment newInstance() {
        return new CustomDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CustomDialogFragment.CustomDialogListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity());

        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog);

        // 背景を透明にする
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ピクセル入力値の処理
        input = (EditText)(dialog.findViewById(R.id.inputPixel));

        // メッセージ
        chengeMessage = (TextView)(dialog.findViewById(R.id.message));

        // OKボタンの処理
        dialog.findViewById(R.id.positive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 入力値の処理
                inputPixel = input.getText().toString();

                // バリデーションチェック 数字2桁まで入力可
                if (inputPixel.matches("\\d{1,2}")) {
                    // 指定したピクセルでキャンバス作成
                    Intent intent = new Intent(getActivity().getApplication(), PixelArtMakeActivity.class);
                    intent.putExtra("pxInput", inputPixel);
                    startActivity(intent);
                    dismiss();
                } else {
                    // message書き換え
                    chengeMessage.setText("2桁までの半角数字で入力してください");
                    chengeMessage.setTextColor(Color.RED);
                }
            }
        });
        // Closeボタンの処理
        dialog.findViewById(R.id.negative_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }

}