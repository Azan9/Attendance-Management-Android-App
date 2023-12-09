package com.example.android.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.attendance.Adapters.SmFragmentPagerAdapter;
import com.example.android.attendance.Api.ApiClient;
import com.example.android.attendance.Models.ProfileResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    ViewPager viewPager;
    public static final String SHARED_PREFS = "MyPrefs";
    public static final String UserId = "userIdKey";
    public static final String Role = "roleKey";
    public static final String Token = "tokenKey";
    private String userRole, token;
    private SharedPreferences sharedPreferences;
    private int userId;

    Call<List<ProfileResponse>> profileResponseCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt(UserId, 0);
        userRole = sharedPreferences.getString(Role, null);
//        Toast.makeText(this, "Welcome, " + " USER_ID: " + userId, Toast.LENGTH_LONG).show();

        TabLayoutSetUp();


    }

    public void TabLayoutSetUp() {

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        drawer = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SmFragmentPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                HomeActivity.this));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        String tabTitles[] = new String[] { "Home", "Attendance", "Calendar", "Profile" };
        int[] tabIcon = {R.drawable.home, R.drawable.check, R.drawable.calender, R.drawable.profile};
        int[] tabIconSelected = {R.drawable.homeselected, R.drawable.checkselect, R.drawable.calenderselect, R.drawable.profileselect};


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

            // set the label text by getting the actual string value by its id
            tab_label.setText(tabTitles[i]);

            // set the home to be active at first
            if(i == 0) {
                tab_label.setTextColor(getColor(R.color.black));
                tab_icon.setImageResource(tabIconSelected[i]);
            } else {
                tab_icon.setImageResource(tabIcon[i]);
            }

            // finally publish this custom view to navigation tab
            tabLayout.getTabAt(i).setCustomView(tab);

            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    getNavUserDetails();
                }

                @Override
                public void onDrawerOpened(@NonNull View drawerView) {}

                @Override
                public void onDrawerClosed(@NonNull View drawerView) {}

                @Override
                public void onDrawerStateChanged(int newState) {}
            });
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                // getting the custom View
                View tabView = tab.getCustomView();

                // getting inflated children Views the icon and the label by their id
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                // change the label color, by getting the color resource value
                tab_label.setTextColor(getColor(R.color.black));
                // change the image Resource and call tab.getPosition() to get active tab index.
                tab_icon.setImageResource(tabIconSelected[tab.getPosition()]);
                getSupportActionBar().setTitle(tabTitles[tab.getPosition()]);
            }

            // Reset tabs when unselected
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                View tabView = tab.getCustomView();
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);

                // back to the black color
                tab_label.setTextColor(getResources().getColor(R.color.grey));
                // change the image Resource to and call tab.getPosition() to get active tab index.
                tab_icon.setImageResource(tabIcon[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
        MenuItem profileItem = (MenuItem) findViewById(R.id.nav_profile);
        if(navigationView.getMenu().findItem(R.id.nav_profile).isChecked()){
            onNavigationItemSelected(profileItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getNavUserDetails();
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            viewPager.setCurrentItem(3);
        }
        if (id == R.id.nav_settings){
            startActivity(new Intent(this, SettingsActivity.class));
        }
        if (id == R.id.nav_logout) {
            logout();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return true;
    }

    public void logout(){
        // calling method to edit values in shared prefs.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        token = sharedPreferences.getString(Token, null);

        Call<ResponseBody> logoutResponseCall = ApiClient.getUserService().userLogout("Token " + token);

        logoutResponseCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204 || response.isSuccessful()) {
                    // Clearing the data in shared prefs.
                    editor.clear();
                    // Applying empty data to shared prefs.
                    editor.apply();
                    // starting main activity after clearing values in shared preferences.
                    Intent i = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(getApplicationContext(), "Logged out Successfully.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "No Response from server", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"You cannot go back to the login page. Please logout first!", Toast.LENGTH_LONG).show();
    }

    public void getNavUserDetails() {
        if (userRole.equals("student")) {
            profileResponseCall = ApiClient.getUserService().studentProfileResponse(userId);
        }
        if (userRole.equals("teacher")) {
            profileResponseCall = ApiClient.getUserService().teacherProfileResponse(userId);
        }

        profileResponseCall.enqueue(new Callback<List<ProfileResponse>>() {
            @Override
            public void onResponse(Call<List<ProfileResponse>> call, Response<List<ProfileResponse>> response) {
                List<ProfileResponse> dataList;
                dataList = new ArrayList<>();
                if (response.isSuccessful()) {
                    List<ProfileResponse> models = response.body();
                    TextView username = (TextView) findViewById(R.id.nav_user_name);
                    TextView userEmail = (TextView) findViewById(R.id.nav_user_email);
                    ImageView profileImage = (ImageView) findViewById(R.id.nav_user_image);

                    if (models.isEmpty()) {

                    } else {
                        for (ProfileResponse model : models) {
                            dataList.add(model);
                        }
                        String imageURL = dataList.get(0).getProfile_image();
                        username.setText(dataList.get(0).getFull_name());
                        userEmail.setText(dataList.get(0).getEmail());
                        Picasso.get().load("http://192.168.1.01:8000" + imageURL).into(profileImage);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<ProfileResponse>> call, Throwable t) {

            }
        });
    }
}
