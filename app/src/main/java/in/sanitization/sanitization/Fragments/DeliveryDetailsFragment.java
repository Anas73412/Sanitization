package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDetailsFragment extends Fragment implements View.OnClickListener{

    TextView tv_rev_name,tv_rev_mobile,tv_rev_pincode,tv_rev_address,tvItems,tvprice,tvMrp,tvDiscount,tvSubTotal;
    RelativeLayout rel_order;
    String loc_id="",rev_name,rev_mobile,rev_address,rev_state,rev_city,rev_pincode,rev_soc_id,plan_id,plan_name,mrp,price;
    public DeliveryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_delivery_details_fragment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        tv_rev_name=v.findViewById(R.id.tv_rev_name);
        tv_rev_mobile=v.findViewById(R.id.tv_rev_mobile);
        tv_rev_pincode=v.findViewById(R.id.tv_rev_pincode);
        tv_rev_address=v.findViewById(R.id.tv_rev_address);
        tvItems=v.findViewById(R.id.tvItems);
        tvprice=v.findViewById(R.id.tvprice);
        tvMrp=v.findViewById(R.id.tvMrp);
        tvDiscount=v.findViewById(R.id.tvDiscount);
        tvSubTotal=v.findViewById(R.id.tvSubTotal);
        rel_order=v.findViewById(R.id.rel_order);
        loc_id=getArguments().getString("loc_id");
        rev_name=getArguments().getString("name");
        rev_mobile=getArguments().getString("mobile");
        rev_soc_id=getArguments().getString("socity_id");
        rev_pincode=getArguments().getString("pincode");
        rev_address=getArguments().getString("address");
        rev_state=getArguments().getString("state");
        rev_city=getArguments().getString("city");
        plan_id=getArguments().getString("plan_id");
        plan_name=getArguments().getString("plan_name");
        mrp=getArguments().getString("mrp");
        price=getArguments().getString("price");
        ((SubscriptionActivity)getActivity()).setTitle("Order Details");
        rel_order.setOnClickListener(this);

        tv_rev_name.setText(rev_name);
        tv_rev_mobile.setText(rev_mobile);
        tv_rev_pincode.setText(rev_pincode);
        String address=rev_address+"\n "+rev_state+" ("+rev_pincode+")";
        tv_rev_address.setText(address);

        tvItems.setText("1");
        tvprice.setText(getResources().getString(R.string.currency)+" "+price);
        tvMrp.setText(getResources().getString(R.string.currency)+" "+mrp);
        tvSubTotal.setText(getResources().getString(R.string.currency)+" "+price);

        Double dmrp=Double.parseDouble(mrp);
        Double dprice=Double.parseDouble(price);
        int dis=(int)(dmrp-dprice);
        if(dis<=0)
        {
            tvDiscount.setText("No Discount");
        }
        else
        {
            tvDiscount.setText("-"+getResources().getString(R.string.currency)+" "+dis);
        }



    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_order)
        {
            Bundle bundle = new Bundle();
            Fragment fm=new ConfirmOrderFragment();
            loadFragment(fm,bundle);


        }
    }

    public void loadFragment(Fragment fm,Bundle args)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fm.setArguments(args);
        fragmentManager.beginTransaction()
                .replace( R.id.content_frame,fm)
                .addToBackStack(null)
                .commit();
    }



}
