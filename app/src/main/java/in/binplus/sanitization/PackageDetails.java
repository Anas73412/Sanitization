package in.binplus.sanitization;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.binplus.sanitization.Config.BaseUrl;
import in.binplus.sanitization.Config.Module;
import in.binplus.sanitization.util.LoadingBar;

public class PackageDetails extends AppCompatActivity implements View.OnClickListener {
    TextView pckg_name ,pckg_price,pkg_product ,txt_title;
    ImageView pckg_img ,img_back;
    Button btn_buy ;
    LoadingBar loadingBar ;
    String img="",title= "",price="",product="",status="",id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);
        initViews();
    }

    private void initViews() {
        pckg_img = findViewById(R.id.product_img);
        pckg_name=findViewById(R.id.package_name);
       pckg_price = findViewById(R.id.package_price);
       pkg_product = findViewById(R.id.description);
       pkg_product = findViewById(R.id.description);
       btn_buy = findViewById(R.id.buy_now);
       txt_title= findViewById(R.id.tv_title);
       img_back = findViewById(R.id.iv_back);
        loadingBar = new LoadingBar(PackageDetails.this);
        btn_buy.setOnClickListener(this);
        img_back.setOnClickListener(this);

        img = getIntent().getStringExtra("plan_image");
        price = getIntent().getStringExtra("plan_price");
      product= getIntent().getStringExtra("plan_product");
        title = getIntent().getStringExtra("plan_name");
        status = getIntent().getStringExtra("plan_status");
        id = getIntent().getStringExtra("plan_id");

        Glide.with(PackageDetails.this)
                .load(BaseUrl.IMG_PLAN_URL+img )
                .placeholder(R.drawable.logo)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(pckg_img);
      pckg_price.setText(price);
      pckg_name.setText(title);
      pkg_product.setText(product);
      txt_title.setText(title);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back)
        {
            finish();
        }
        else if (id == R.id.buy_now)
        {

        }
    }
}
