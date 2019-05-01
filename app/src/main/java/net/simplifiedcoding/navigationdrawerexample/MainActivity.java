package net.simplifiedcoding.navigationdrawerexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager sm;
    NavigationView navigationView;
    TextView tv_emails,tv_usernames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = new SessionManager(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


       // drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
       // drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar_main);
       // drawerFragment.setDrawerListener(this);
        //21
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        tv_usernames=(TextView)headerView.findViewById(R.id.tv_usernames);
        tv_emails=(TextView)headerView.findViewById(R.id.tv_emails);
        //tv_emails.setText(sm.getUserName2());
        //tv_usernames.setText(sm.getUserName()+" " +sm.getUserName1());


        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_menu1);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Do Activity menu item stuff here
                return false;

            default:
                break;
        }

        return false;
    }*/

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
                fragment = new Fragment_setting();
                break;
            case R.id.nav_menu9:
                Intent i9=new Intent(getApplicationContext(),Activity_Myaccount.class);
                startActivity(i9);
                break;
            case R.id.nav_menu2:
                Intent i2=new Intent(getApplicationContext(),Activity_Chat.class);
                startActivity(i2);
                break;

            case R.id.nav_menu3:
                Intent i3=new Intent(getApplicationContext(),Activity_Credit.class);
                startActivity(i3);
                break;

            case R.id.nav_menu4:
                Intent i4=new Intent(getApplicationContext(),Activiry_setting.class);
                startActivity(i4);
                break;

            case R.id.nav_menu5:
                Intent i5=new Intent(getApplicationContext(),Activity_About.class);
                startActivity(i5);
                break;

            case R.id.nav_menu6:
                sm.logoutUser();
                Intent i1=new Intent(getApplicationContext(),Activity_Login.class);
                startActivity(i1);
                finish();
                break;
            case R.id.nav_menu7:
                Intent i7=new Intent(getApplicationContext(),Activity_help.class);
                startActivity(i7);
                break;

            default:
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());

        //make this method blank
        return true;
    }


}
