package in.sanitization.sanitization;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sanitization.sanitization.Adapter.NotificationAdapter;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Fragments.ABoutUsFragment;
import in.sanitization.sanitization.Fragments.ContactFragment;
import in.sanitization.sanitization.Fragments.EditProfileFragment;
import in.sanitization.sanitization.Fragments.HelpActivity;
import in.sanitization.sanitization.Fragments.HomeFragment;
import in.sanitization.sanitization.Fragments.MyOrders;
import in.sanitization.sanitization.Fragments.NotificationFragment;
import in.sanitization.sanitization.Fragments.PrivacyFragment;
import in.sanitization.sanitization.Fragments.TermsFragment;
import in.sanitization.sanitization.Model.NotificationModel;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.NotificationHandler;
import in.sanitization.sanitization.util.Session_management;

import static in.sanitization.sanitization.Config.Constants.KEY_MOBILE;
import static in.sanitization.sanitization.Config.Constants.KEY_NAME;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    Navigation header ;
    TextView txt_name,totalNtificationCount;
    ImageView img_edit ;
    Session_management session_management;
    Activity ctx=HomeActivity.this;
    Module module;
    NotificationHandler notificationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        module=new Module(ctx);
        notificationHandler=new NotificationHandler(ctx);
       drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
   session_management=new Session_management(ctx);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        toggle.syncState();

//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.drawer_icon, getTheme());
//        toggle.setHomeAsUpIndicator(drawable);
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawer.isDrawerVisible(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.openDrawer(GravityCompat.START);
//                }
//            }
//        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        txt_name = header.findViewById(R.id.profile_user_name);
       img_edit = header.findViewById(R.id.edit_profile);
       ImageView iv_icon = header.findViewById(R.id.icon);
        txt_name.setText(session_management.getUserDetails().get(KEY_NAME).toUpperCase()+"\n"+session_management.getUserDetails().get(KEY_MOBILE));


//        if(session_management.getUserDetails().get(KEY_))
        navigationView.setNavigationItemSelectedListener(this);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment fm=new ResetPasswordFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame, fm)
                        .addToBackStack(null).commit();
            }
        });

        if(savedInstanceState ==null)
        {
            HomeFragment fm=new HomeFragment();
            final Bundle bundle=new Bundle();
            addFragment(fm,bundle);
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Fragment fr = getSupportFragmentManager().findFragmentById(R.id.frame);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);
                    if (fm_name.contentEquals("HomeFragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        toggle.setDrawerIndicatorEnabled(true);
                        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        toggle.syncState();

                    }
                     else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        toggle.setDrawerIndicatorEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
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
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
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
        else if (id == R.id.nav_profile)
        {
            fm = new EditProfileFragment();
        }
        else if (id == R.id.nav_orders)

        {
            fm = new MyOrders();
        }
        else if (id == R.id.nav_enquire)
        {
            fm = new HelpActivity();
//           Intent intent = new Intent(ctx,HelpActivity.class);
//           startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.home,menu);
        final MenuItem notif_item=menu.findItem(R.id.action_notification);
        notif_item.setVisible(true);
         View count=notif_item.getActionView();
        count.setOnClickListener(new View.OnClickListener() {
        @Override
          public void onClick(View v) {
menu.performIdentifierAction(notif_item.getItemId(),0);

         }
        });
        totalNtificationCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);
//        totalNtificationCount.setText("" + db_cart.getCartCount());
        totalNtificationCount.setText("");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_notification) {
//             notificationHandler.clearNotifications();
            Fragment fm=new NotificationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fm)
                    .addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }


    public void setNotificationCounter(int tot)
    {
        if (tot <= 0) {
            if (totalNtificationCount.getVisibility() == View.VISIBLE)
                totalNtificationCount.setVisibility(View.GONE);

        } else {
            if (totalNtificationCount.getVisibility() == View.GONE) {
                totalNtificationCount.setVisibility(View.VISIBLE);
            }
            totalNtificationCount.setText("" + tot);
        }
    }

    public void setUserName()
    {
        txt_name.setText(session_management.getUserDetails().get(KEY_NAME).toUpperCase()+"\n"+session_management.getUserDetails().get(KEY_MOBILE));

    }
}
