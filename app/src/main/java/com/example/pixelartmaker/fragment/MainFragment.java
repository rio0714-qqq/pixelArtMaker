package com.example.pixelartmaker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pixelartmaker.R;

public class MainFragment extends Fragment {
    public static MainFragment newInstance(String str){
        // Fragemnt01 インスタンス生成
        MainFragment fragment = new MainFragment ();
        // Bundle にパラメータを設定
        Bundle barg = new Bundle();
        barg.putString("Message", str);
        fragment.setArguments(barg);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button b = view.findViewById(R.id.button_first);
        b.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
//                actionBar.hide();
            }
        });

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}