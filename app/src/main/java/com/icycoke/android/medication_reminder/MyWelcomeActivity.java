package com.icycoke.android.medication_reminder;

import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.WelcomeConfiguration;

public class MyWelcomeActivity extends com.stephentuso.welcome.WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat-Bold.ttf")
                .defaultHeaderTypefacePath("Montserrat-Bold.ttf")

                .page(new BasicPage(R.drawable.ic_launcher_foreground,
                        getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_intro))
                        .background(R.color.colorPrimary)
                )

                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
}
