package com.nuocngot.tvdpro;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout layout_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        layout_login = findViewById(R.id.layout_login);
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_login, "translationX", 200f, 0f);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        layout_login.setVisibility(View.VISIBLE);
        animator.start();
    }
}