package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import in.sanitization.sanitization.Adapter.PagerOrderAdapter;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.LoadingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrders extends Fragment {
    ViewPager viewPager ;
    TabLayout tabLayout ;
    LoadingBar loadingBar ;
    Module module ;


    public MyOrders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_my_orders, container, false);
        ((HomeActivity) getActivity()).setTitle("My Orders");
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Recent Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Past Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled Orders"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)view.findViewById(R.id.pager);
        final PagerOrderAdapter adapter = new PagerOrderAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

      return view ;
    }

}
