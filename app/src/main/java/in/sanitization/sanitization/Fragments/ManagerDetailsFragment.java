package in.sanitization.sanitization.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerDetailsFragment extends Fragment implements View.OnClickListener{
    ImageView iv_pic,iv_whatsapp,iv_call;
    TextView tv_name,tv_mobile,tv_email,tv_gen,tv_adhar,tv_code;
    String arr="",type="",mobile="";
    JSONArray jsonArray=null;
    Module module;
    LinearLayout lin_gen,lin_adhar,lin_code ;

    String title="";
    public ManagerDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_manager_details, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        iv_call=v.findViewById(R.id.iv_call);
        iv_whatsapp=v.findViewById(R.id.iv_whatsapp);
        iv_pic=v.findViewById(R.id.iv_pic);
        tv_mobile=v.findViewById(R.id.tv_mobile);
        tv_name=v.findViewById(R.id.tv_name);
        tv_email=v.findViewById(R.id.tv_email);
        tv_gen=v.findViewById(R.id.tv_gen);
        tv_adhar=v.findViewById(R.id.tv_adhar);
        tv_code=v.findViewById(R.id.tv_code);
        lin_gen=v.findViewById(R.id.lin_gen);
        lin_adhar=v.findViewById(R.id.lin_adhar);
        lin_code=v.findViewById(R.id.lin_code);
        module=new Module(getActivity());

        arr=getArguments().getString("arr");
        type=getArguments().getString("type");

         iv_call.setOnClickListener(this);
         iv_whatsapp.setOnClickListener(this);
        try {
            jsonArray = new JSONArray(arr.toString());

            if (type.equalsIgnoreCase("area")) {
                title="Area Manager";
                lin_code.setVisibility(View.GONE);
                lin_adhar.setVisibility(View.GONE);
                lin_gen.setVisibility(View.VISIBLE);
                String picString = jsonArray.getJSONObject(0).getString("user_photo").toString();
                if (picString == null || picString.isEmpty()) {
                } else {
                    Glide.with(getActivity())
                            .load(BaseUrl.IMG_AREA_URL + picString)
                            .placeholder(R.drawable.logo)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(iv_pic);
                    mobile = jsonArray.getJSONObject(0).getString("user_mobile").toString();
                    tv_name.setText("" + jsonArray.getJSONObject(0).getString("user_name").toString());
                    tv_email.setText("" + jsonArray.getJSONObject(0).getString("user_email").toString());
                    tv_mobile.setText("" + mobile);
                    tv_gen.setText("" + jsonArray.getJSONObject(0).getString("user_gender").toString());
                }

            }
            else if (type.equalsIgnoreCase("dis")) {
                title="District Manager";
                lin_code.setVisibility(View.GONE);
                lin_adhar.setVisibility(View.GONE);
                lin_gen.setVisibility(View.VISIBLE);
                String picString = jsonArray.getJSONObject(0).getString("user_photo").toString();
                if (picString == null || picString.isEmpty()) {
                } else {
                    Glide.with(getActivity())
                            .load(BaseUrl.IMG_DISTRICT_URL +picString)
                            .placeholder(R.drawable.logo)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(iv_pic);
                    mobile = jsonArray.getJSONObject(0).getString("user_mobile").toString();
                    tv_name.setText("" + jsonArray.getJSONObject(0).getString("user_name").toString());
                    tv_email.setText("" + jsonArray.getJSONObject(0).getString("user_email").toString());
                    tv_mobile.setText("" + mobile);
                    tv_gen.setText("" + jsonArray.getJSONObject(0).getString("user_gender").toString());
                }

            } else if (type.equalsIgnoreCase("worker")) {

                title="Worker";
                lin_code.setVisibility(View.VISIBLE);
                lin_adhar.setVisibility(View.VISIBLE);
                lin_gen.setVisibility(View.GONE);
                JSONObject worker = jsonArray.getJSONObject(0);
                JSONArray w_ar= new JSONArray(worker.getString("photo"));
                if (w_ar.length()==0) {
                }
                else {

                    Glide.with(getActivity())
                            .load( BaseUrl.IMG_WORKER_URL +w_ar.get(0))
                            .placeholder( R.drawable.logo)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(iv_pic);
                    mobile = jsonArray.getJSONObject(0).getString("mobile").toString();
                    tv_name.setText("" + jsonArray.getJSONObject(0).getString("name").toString());
                    tv_email.setText("" + jsonArray.getJSONObject(0).getString("email").toString());
                    tv_mobile.setText("" + jsonArray.getJSONObject(0).getString("mobile").toString());
                    tv_adhar.setText("" + jsonArray.getJSONObject(0).getString("adhaar_no").toString());
                    tv_code.setText("" + jsonArray.getJSONObject(0).getString("worker_code").toString());

                }

            }


            ((HomeActivity) getActivity()).setTitle(title);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }




    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.iv_call)
        {
         module.callOnNumber(mobile);
        }
        else if(v.getId() == R.id.iv_whatsapp)
        {

            String message="Hello";
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=+91"+ mobile +"&text=" + URLEncoder.encode(message, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    getActivity().startActivity(i);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
