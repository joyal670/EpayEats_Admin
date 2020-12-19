package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.epayeats.epayeatsadmin.Fragments.Business_Fragment;
import com.epayeats.epayeatsadmin.Fragments.Dashboard_Fragment;
import com.epayeats.epayeatsadmin.Fragments.DeliveryBoy_Fragment;
import com.epayeats.epayeatsadmin.Fragments.FoodCatagory_Fragment;
import com.epayeats.epayeatsadmin.Fragments.LocalAdmin_Fragment;
import com.epayeats.epayeatsadmin.Fragments.Logout_Fragment;
import com.epayeats.epayeatsadmin.Fragments.Restaurant_Fragment;
import com.epayeats.epayeatsadmin.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToogle;
    String currentFragment = "other";
    TextView headerEmail;

    SharedPreferences sharedPreferences;
    String a1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            sharedPreferences = getSharedPreferences("data", 0);
            a1 = sharedPreferences.getString("useremail", "");

            mDrawerLayout = findViewById(R.id.drawerLayout);
            mDrawerToogle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
            mDrawerToogle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
            mDrawerLayout.addDrawerListener(mDrawerToogle);
            mDrawerToogle.syncState();

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);

            headerEmail.setText(a1);

            Dashboard_Fragment fragment = new Dashboard_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Dashboard");
            fragmentTransaction.commit();

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        try {
            int id = item.getItemId();
            if (id == R.id.dashboard) {
                Dashboard_Fragment fragment = new Dashboard_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Dashboard");
                fragmentTransaction.commit();
            } else if (id == R.id.business) {
                currentFragment = "other";
                Business_Fragment fragment = new Business_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Business");
                fragmentTransaction.commit();
            } else if (id == R.id.localadmin) {
                currentFragment = "other";
                LocalAdmin_Fragment fragment = new LocalAdmin_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Local Admin");
                fragmentTransaction.commit();
            } else if (id == R.id.deliveryboy) {
                currentFragment = "other";
                DeliveryBoy_Fragment fragment = new DeliveryBoy_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Delivery Boy");
                fragmentTransaction.commit();
            }
            else if (id == R.id.restaurants) {
                currentFragment = "other";
                Restaurant_Fragment fragment = new Restaurant_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Restaurant");
                fragmentTransaction.commit();
            }
            else if (id == R.id.foodcatagory) {
                currentFragment = "other";
                FoodCatagory_Fragment fragment = new FoodCatagory_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Food Catagory");
                fragmentTransaction.commit();
            } else if (id == R.id.logout) {
                currentFragment = "other";
                Logout_Fragment fragment = new Logout_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment, "Logout");
                fragmentTransaction.commit();
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (currentFragment != "home")
            {
                Dashboard_Fragment fragment = new Dashboard_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment,"Dashboard");
                fragmentTransaction.commit();
            } else {
                super.onBackPressed();
            }
        }
    }
}