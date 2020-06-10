package in.sanitization.sanitization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import in.sanitization.sanitization.Fragments.ABoutUsFragment;
import in.sanitization.sanitization.Fragments.ContactFragment;
import in.sanitization.sanitization.Fragments.HomeFragment;
import in.sanitization.sanitization.Fragments.PrivacyFragment;
import in.sanitization.sanitization.Fragments.TermsFragment;
import in.sanitization.sanitization.util.Session_management;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    Session_management session_management;
    Activity ctx=HomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
       drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
   session_management=new Session_management(ctx);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        HomeFragment fm=new HomeFragment();
        final Bundle bundle=new Bundle();
        addFragment(fm,bundle);


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    android.app.Fragment fr = getFragmentManager().findFragmentById(R.id.contentPanel);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);
                    if (fm_name.contentEquals("HomeFragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        toggle.setDrawerIndicatorEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        toggle.syncState();

                    } else if (fm_name.contentEquals("PackagesFragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                               Fragment fm = new HomeFragment();
                             FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
                super.onBackPressed();
//        AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
//        builder.setTitle("Confirmation");
//        builder.setMessage("Are you sure want to exit?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    finishAffinity();
//                }
//
//            }
//        })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        dialog.dismiss();
//                    }
//                });
//        AlertDialog dialog=builder.create();
//        dialog.show();
    }



    public void addFragment(Fragment fm, Bundle args)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fm.setArguments(args);
        fragmentManager.beginTransaction()
                .add( R.id.frame,fm)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.nav_home) {
            fm = new HomeFragment();
        }
        else if (id == R.id.nav_contact)
        {
            fm = new ContactFragment();
        }
        else if (id == R.id.nav_about)
        {
            fm = new ABoutUsFragment();
        }
        else if (id == R.id.nav_privacy)
        {
            fm = new PrivacyFragment();
        }
        else if (id == R.id.nav_terms)
        {
            fm = new TermsFragment();
        }

        else if (id == R.id.nav_logout)
        {
            androidx.appcompat.app.AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
            dialog.setTitle("Logout");
            dialog.setMessage("Sure to Logout ?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   session_management.logoutSession();
                    Intent intent = new Intent(HomeActivity.this , LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialog.cancel();

                }
            });
            dialog.show();
        }
        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fm)
                    .addToBackStack(null).commit();

        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
