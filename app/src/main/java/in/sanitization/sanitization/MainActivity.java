package in.sanitization.sanitization;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;
import com.google.android.material.tabs.TabLayout;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import in.sanitization.sanitization.Adapter.ViewPagerAdpater;
import in.sanitization.sanitization.Fragments.FindFragment;
import in.sanitization.sanitization.Fragments.HomeFragment;
import in.sanitization.sanitization.Fragments.PackagesFragment;

public class MainActivity extends AppCompatActivity implements OnBubbleClickListener {

    TabLayout tabs;
    BubbleTabBar bubbleTabBar ;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.pppp));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.serviuce));
        spaceNavigationView.setCentreButtonSelectable(true);
        spaceNavigationView.setCentreButtonSelected();
        HomeFragment fm=new HomeFragment();
        final Bundle bundle=new Bundle();
        addFragment(fm,bundle);
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {

              HomeFragment fm=new HomeFragment();

                loadFragment(fm,bundle);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                Fragment fm=null;
                if(itemIndex == 0)
                {
                    fm=new PackagesFragment();
                }
                else if(itemIndex==1)
                {
                    fm=new FindFragment();
                }
                Bundle bundle=new Bundle();
                loadFragment(fm,bundle);

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
//

            }
        });

    initViews();
    }

    private void initViews() {
        viewPager=findViewById(R.id.viewPager);
        tabs=findViewById(R.id.tabs);
        bubbleTabBar=findViewById(R.id.bubbleTabBar);
        setupViewPager();
        bubbleTabBar.addBubbleListener(this);
        tabs.setupWithViewPager(viewPager);
        bubbleTabBar.setupBubbleTabBar(viewPager);

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

    @Override
    public void onBubbleClick(int i) {
        int id = bubbleTabBar.getId();
        Toast.makeText(MainActivity.this,""+id,Toast.LENGTH_LONG).show();
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
    public void loadFragment(Fragment fm,Bundle args)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fm.setArguments(args);
        fragmentManager.beginTransaction()
                .replace( R.id.frame,fm)
                .addToBackStack(null)
                .commit();
    }
}
