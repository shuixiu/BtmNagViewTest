package com.test.sixpro.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.test.sixpro.R;
import com.test.sixpro.utils.HomeButtomData;

public class MainActivity extends AppCompatActivity {

    private FrameLayout home_container;
    private Fragment[] mFragments;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            onTabItemSelected(item.getItemId());
            return true;
        }
    };


    private void onTabItemSelected(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.navigation_home:
                fragment = mFragments[0];
                break;
            case R.id.navigation_dashboard:
                fragment = mFragments[1];
                break;

            case R.id.navigation_notifications:
                fragment = mFragments[2];
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragments = HomeButtomData.getFragments("BottomNavigationView");

        home_container = (FrameLayout) findViewById(R.id.home_container);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        onTabItemSelected(R.id.navigation_home);

    }

}
