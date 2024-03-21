package com.nuocngot.tvdpro.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nuocngot.tvdpro.R;

public class SplashActivity extends AppCompatActivity {
    private TextView textViewSpalsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        textViewSpalsh = findViewById(R.id.textViewSpalsh);
        ObjectAnimator animator = ObjectAnimator.ofFloat(textViewSpalsh, "translationY", 200f, 0f);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        textViewSpalsh.setVisibility(View.VISIBLE);
        animator.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}