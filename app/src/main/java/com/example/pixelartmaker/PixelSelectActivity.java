package com.example.pixelartmaker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pixelartmaker.fragment.MainFragment;

public class PixelSelectActivity extends AppCompatActivity {

    //データ
    String[] str = {
            "", "クマ", "キリン", "ゾウ", "パンダ", "ペンギン", "コアラ", "キリン",
            "カンガルー", "サル",
            "ヒョウ",
            "ゴリラ",
            "カバ",
            "カピバラ",
            "リス",
            "チンパンジー",
            "ワニ",
            "ハムスター",
            "ヒツジ",
            "ネコ",
            //16 24　32 48 64 96 128 160 192
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_select);

        Toolbar toolbar = findViewById(R.id.toolbar_new_create);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //クリックしたときのプログラムを書く
                        Log.i("ボタン押したね","yehyehyehyeh");
                        Intent intent = new Intent(getApplication(), PixelArtMakeActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        Log.i("テスト", "tesuto");
        finish();
        return super.onSupportNavigateUp();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
////        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
////        return NavigationUI.navigateUp(navController, appBarConfiguration)
////                || super.onSupportNavigateUp();
//    }
}
