package com.example.cityguide.LocationOwner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.transition.Fade;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.cityguide.Databases.SessionManager;
import com.example.cityguide.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.HashMap;

public class RetailerDashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_dashboard);

        Fade fade = null;
        fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);


        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RetailerDashboardFragment()).commit();
        bottomMenu();


    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_dashboard:
                    fragment = new RetailerDashboardFragment();
                    break;
                    case R.id.bottom_nav_manage:
                        fragment = new RetailerManageFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new RetailerProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });


    }
}