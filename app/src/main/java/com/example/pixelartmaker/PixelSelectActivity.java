package com.example.pixelartmaker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.pixelartmaker.fragment.CustomDialogFragment;

public class PixelSelectActivity extends AppCompatActivity implements CustomDialogFragment.CustomDialogListener {

    public static View dialogView;

    // リストデータ
    String[] str = {
        "16 × 16", "24 × 24", "32 × 32", "48 × 48", "サイズを指定する"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_select);

        Toolbar toolbar = findViewById(R.id.toolbar_new_create);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ピクセル選択ダイアログ
        dialogView = LayoutInflater.from(PixelSelectActivity.this)
                .inflate(R.layout.dialog, null);
        ImageButton positive = (ImageButton) dialogView.findViewById(R.id.positive_button);
        ImageButton negative = (ImageButton) dialogView.findViewById(R.id.negative_button);

        //アダプター
        ArrayAdapter adapter = new ArrayAdapter(
                this,
                R.layout.list_item_pixel_select,
                str
        );

        //リストの表示
        ((ListView)findViewById(R.id.lv)).setAdapter(adapter);

        //クリック処理
        ((ListView)findViewById(R.id.lv)).setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListView listView = (ListView)parent;
                    Intent intent = new Intent(getApplication(), PixelArtMakeActivity.class);

                    // タッチイベント
                    switch (position) {
                        case 0:
                            intent.putExtra("16px", "16");
                            startActivity(intent);
                            break;
                        case 1:
                            intent.putExtra("24px", "24");
                            startActivity(intent);
                            break;
                        case 2:
                            intent.putExtra("32px", "32");
                            startActivity(intent);
                            break;
                        case 3:
                            intent.putExtra("48px", "48");
                            startActivity(intent);
                            break;
                        case 4:
                            CustomDialogFragment dialog = CustomDialogFragment.newInstance();
                            dialog.show(getSupportFragmentManager(), "dialog");
                            break;
                    }
                }
            }
        );
    }
    @Override
    public void getInputPixel(String inputData) {
        Log.d("inputPixel", "ピクセル取得");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuButton){

        boolean result = true;
        //選択されたメニューボタンのIDを取得
        int buttonId = menuButton.getItemId();

        switch(buttonId){
            //戻るボタンが押されたとき
            case android.R.id.home:
                finish();
                break;

            default:
                result = super.onOptionsItemSelected(menuButton);
                break;
        }
        return result;
    }
}