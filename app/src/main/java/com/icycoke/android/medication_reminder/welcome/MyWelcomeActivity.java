package com.icycoke.android.medication_reminder.welcome;

import androidx.fragment.app.Fragment;

import com.icycoke.android.medication_reminder.R;
import com.icycoke.android.medication_reminder.welcome.fragment.WelcomeHomeFragment;
import com.icycoke.android.medication_reminder.welcome.fragment.WelcomeLocationFragment;
import com.icycoke.android.medication_reminder.welcome.fragment.WelcomePeriodicFragment;
import com.stephentuso.welcome.FragmentWelcomePage;
import com.stephentuso.welcome.WelcomeConfiguration;

public class MyWelcomeActivity extends com.stephentuso.welcome.WelcomeActivity {
    @Override
    protected WelcomeConfiguration configuration() {
        return new WelcomeConfiguration.Builder(this)
                .defaultTitleTypefacePath("Montserrat-Bold.ttf")
                .defaultHeaderTypefacePath("Montserrat-Bold.ttf")
                .page(new FragmentWelcomePage() {
                            @Override
                            protected Fragment fragment() {
                                return new WelcomeHomeFragment();
                            }
                        }.background(R.color.welcome_orange)
                )
                .page(new FragmentWelcomePage() {
                            @Override
                            protected Fragment fragment() {
                                return new WelcomePeriodicFragment();
                            }
                        }.background(R.color.welcome_red)
                )
                .page(new FragmentWelcomePage() {
                            @Override
                            protected Fragment fragment() {
                                return new WelcomeLocationFragment();
                            }
                        }.background(R.color.welcome_purple)
                )
                .swipeToDismiss(true)
                .exitAnimation(android.R.anim.fade_out)
                .build();
    }
}
