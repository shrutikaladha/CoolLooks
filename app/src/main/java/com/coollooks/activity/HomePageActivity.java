package com.coollooks.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.coollooks.R;
import com.coollooks.fragment.BookmarkFragment;
import com.coollooks.fragment.TodaysLookFragment;
import com.coollooks.fragment.WardrobeFragment;

public class HomePageActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:  TodaysLookFragment todaysLookFragment = new TodaysLookFragment();
                    return todaysLookFragment;

                case 1:  WardrobeFragment wardrobeFragment = new WardrobeFragment();
                    return wardrobeFragment;

                case 2: BookmarkFragment bookmarkFragment = new BookmarkFragment();
                    return bookmarkFragment;

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Today's Look";
                case 1:
                    return "Wardrobe";
                case 2:
                    return "Bookmarks";
                default:
                    return "Not assigned";
            }

        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        setStatusBarColor();
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(Build.VERSION.SDK_INT >= 21 )
            window.setStatusBarColor(this.getResources().getColor(R.color.theme_color));
    }

}
