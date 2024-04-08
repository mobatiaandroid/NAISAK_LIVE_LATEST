package com.nas.naisak.activity.trips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.nas.naisak.R;
import com.nas.naisak.activity.trips.adapter.TripsImagePagerAdapter;

import java.util.ArrayList;

public class TripImagesViewActivity extends AppCompatActivity {
    Context mContext;
    ImageView back;
    Intent intent;
    ArrayList<String> mPhotosModelArrayList;
    Bundle extras;
    ViewPager bannerImageViewPager;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_images_view);
        mContext = this;
        initUI();
    }

    private void initUI() {
        back = (ImageView) findViewById(R.id.back);
        bannerImageViewPager = (ViewPager) findViewById(R.id.bannerImageViewPager);
        extras = getIntent().getExtras();
        if (extras != null) {
            mPhotosModelArrayList = (ArrayList<String>) extras.getSerializable("photo_array");
            pos = extras.getInt("pos");
        }

        back.setOnClickListener(v -> finish());
        bannerImageViewPager.setAdapter(new TripsImagePagerAdapter(mContext, mPhotosModelArrayList, "portrait"));
        bannerImageViewPager.setCurrentItem(pos);
        bannerImageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                headermanager.setTitle((position + 1) + " Of " + mPhotosModelArrayList.size());
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}