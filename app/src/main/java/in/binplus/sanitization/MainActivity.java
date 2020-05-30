package in.binplus.sanitization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;

import in.binplus.sanitization.Adapter.ViewPagerAdpater;
import in.binplus.sanitization.Fragments.FindFragment;
import in.binplus.sanitization.Fragments.PackagesFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        viewPager=findViewById(R.id.viewPager);
        tabs=findViewById(R.id.tabs);
        setupViewPager();

        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        ViewPagerAdpater adapter = new ViewPagerAdpater(getSupportFragmentManager());
        adapter.addFragment(new PackagesFragment(), "Packages");//DiscoverFragment
        adapter.addFragment(new FindFragment(), "Services");//RecommendationFragment
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
