package in.sanitization.sanitization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.payumoney.sdkui.ui.widgets.CirclePageIndicator;

public class IntroductionActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager viewPager;
    CirclePageIndicator pageIndicator;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Button btn_prev, btn_next;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        btn_prev = (Button) findViewById(R.id.btn_prev);
        btn_next= (Button) findViewById(R.id.btn_nxt);

        viewPager = (ViewPager) findViewById(R.id.pager);

        layouts = new int[]{
                R.layout.slider_slide_1,
                R.layout.slider_slide_2,
                R.layout.slider_slide_3};

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

//        String token=FirebaseInstanceId.getInstance().getToken();
//        if (token!=null)
//            session.setUserData(SessionManager.KEY_DEVICE_TOKEN,token);
        btn_prev.setEnabled(false);
       pageIndicator = findViewById(R.id.indicator);
       pageIndicator.setViewPager(viewPager);
       btn_prev.setOnClickListener(this);
       btn_next.setOnClickListener(this);
      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0)
                {
                    btn_prev.setEnabled(false);
                }
                else
                {
                    btn_prev.setEnabled(true);
                }
                if (position == layouts.length-1)
                {
                    startActivity(new Intent(IntroductionActivity.this,LoginActivity.class));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_prev)
        {

            btn_prev.setBackgroundColor(IntroductionActivity.this.getResources().getColor(R.color.red));
            btn_prev.setTextColor(IntroductionActivity.this.getResources().getColor(R.color.white));

            btn_next.setBackgroundColor(IntroductionActivity.this.getResources().getColor(R.color.white));
            btn_next.setTextColor(IntroductionActivity.this.getResources().getColor(R.color.red));
            if (viewPager.getCurrentItem()>0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
            else
            {
                btn_prev.setEnabled(false);
                btn_prev.setBackgroundColor(Color.LTGRAY);
                btn_prev.setTextColor(IntroductionActivity.this.getResources().getColor(R.color.black));
            }
        }
        else if (v.getId()==R.id.btn_nxt)
        {

            btn_next.setBackgroundColor(IntroductionActivity.this.getResources().getColor(R.color.red));
            btn_next.setTextColor(IntroductionActivity.this.getResources().getColor(R.color.white));
            btn_prev.setBackgroundColor(IntroductionActivity.this.getResources().getColor(R.color.white));
            btn_prev.setTextColor(IntroductionActivity.this.getResources().getColor(R.color.red));
            if (viewPager.getCurrentItem()==layouts.length-1)
            {
                startActivity(new Intent(IntroductionActivity.this,LoginActivity.class));
            }
            else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }

        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}

