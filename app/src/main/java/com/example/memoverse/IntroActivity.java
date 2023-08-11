package com.example.memoverse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;
    private Button btnNext;
    private Button btnBack;
    private Button btnGetStarted;
    private Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
            return;
        }

        if (notePrefData()) {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_intro);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back); // Initialize the "Back" button
        btnGetStarted = findViewById(R.id.btn_get_started);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Selamat Datang", "Aplikasi catatan kami membantu Anda menyimpan dan mengatur ide, to-do list, dan informasi penting dengan mudah. Nikmati pengalaman pencatatan yang intuitif dan efisien.", R.drawable.img1));
        mList.add(new ScreenItem("Fitur Unggulan", "Aplikasi ini dilengkapi dengan berbagai fitur unggulan seperti pengelompokan catatan berdasarkan kategori, pengingat jadwal, sinkronisasi lintas perangkat, dan perlindungan kata sandi untuk menjaga kerahasiaan data Anda.", R.drawable.img2));
        mList.add(new ScreenItem("Organisasi Lebih Mudah", "Dengan tampilan yang bersih dan antarmuka yang mudah digunakan, Anda dapat dengan cepat mengakses dan mengelola catatan Anda. Jangan biarkan ide-ide brilian terlewatkan – gunakan aplikasi catatan kami untuk tetap terorganisir dalam segala hal.", R.drawable.img3));

        screenPager = findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // Hide "Back" button when the app is first opened
        btnBack.setVisibility(View.INVISIBLE);

        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == mList.size() - 1) {
                    btnGetStarted.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                    btnGetStarted.setAnimation(btnAnim);
                    btnBack.setVisibility(View.VISIBLE); // Show "Back" button on the last page
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                    btnGetStarted.setVisibility(View.INVISIBLE);

                    // Hide "Back" button on the first page
                    if (position == 0) {
                        btnBack.setVisibility(View.INVISIBLE);
                    } else {
                        btnBack.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = screenPager.getCurrentItem();
                if (position < mList.size() - 1) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = screenPager.getCurrentItem();
                if (position > 0) {
                    position--;
                    screenPager.setCurrentItem(position);
                }
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Intent loginIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else {
                    Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }

                savePrefsData();
                finish();
            }
        });
    }

    private boolean notePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend", false);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.apply();
    }
}
