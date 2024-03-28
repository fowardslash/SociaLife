package com.example.projectmain;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.projectmain.Refactoring.SingletonColorChange.ColorManager;

public class AppCreditsActivity extends AppCompatActivity {
    LinearLayout btnQuang, btnDuong, btnThanh, btnGit, all;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_credits);
        btnQuang = findViewById(R.id.quang);
        btnDuong = findViewById(R.id.duong);
        btnThanh = findViewById(R.id.thanh);
        btnGit  = findViewById(R.id.github);
        all = findViewById(R.id.wrapAllcre);
        String urlThanh = "https://github.com/trduyTh4nh";
        String urlQuang = "https://github.com/fowardslash";
        String urlDuong = "https://github.com/sugar2507";
        String urlProj = "https://github.com/trduyTh4nh/SociaLife";
        btnQuang.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(urlQuang));
            startActivity(i);
        });
        btnThanh.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(urlThanh));
            startActivity(i);
        });
        btnDuong.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(urlDuong));
            startActivity(i);
        });
        btnGit.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(urlProj));
            startActivity(i);
        });
        findViewById(R.id.btn_exit).setOnClickListener(v -> {
            finish();
        });

        ColorManager colorManager = ColorManager.getInstance();
        all.setBackgroundDrawable(colorManager.getBackgroundDrawable());
    }
}