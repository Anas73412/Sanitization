package in.sanitization.sanitization.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import in.sanitization.sanitization.Adapter.CarausalAdapter;
import in.sanitization.sanitization.Adapter.FAQAdapter;
import in.sanitization.sanitization.Adapter.PackageAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.CustomSlider;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.Model.BannerModel;
import in.sanitization.sanitization.Model.FAQModel;
import in.sanitization.sanitization.Model.PackageModel;
import in.sanitization.sanitization.Model.Slider_model;
import in.sanitization.sanitization.PackageDetails;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    RecyclerView rv_faq;
    SliderLayout home_banner;
    TextView txt_contact,txt_message ,txt_about,txt_version,txt_view_more,txt_more_faq;
    Module module ;
    LoadingBar loadingBar ;
    String url = "contact us" ,app_link="";
    int version_code =0 ;
    FAQAdapter faqAdapter;
    ArrayList<Slider_model> carList;
    ArrayList<Slider_model> bannerList;
    ArrayList<BannerModel>banner_list;
    ArrayList<FAQModel>faq_list;
    ViewPager viewPager ,viewPager2;
    CarausalAdapter carausalAdapter;
    int current_position=0;
    Timer timer;
    public static  String gst ;
    PackageAdapter packageAdapter;
    ArrayList<PackageModel> list;
     RecyclerView rv_package ;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home, container, false);
       initViews(view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure want to exit?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //((MainActivity) getActivity()).finish();
                            getActivity().finishAffinity();


                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
       return view;
    }
    private void initViews(View view) {

        rv_faq=view.findViewById(R.id.rv_faq);
//        home_slider = view.findViewById(R.id.slider);
//        home_banner = view.findViewById(R.id.banner);
        txt_about= view.findViewById(R.id.txt_about);
        txt_contact = view.findViewById(R.id.txt_contact);
        txt_message = view.findViewById(R.id.txt_message);
        txt_version = view.findViewById(R.id.txt_version);
        txt_view_more = view.findViewById(R.id.view_more);
        txt_more_faq = view.findViewById(R.id.view_faq);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager2 = view.findViewById(R.id.viewPager2);
        txt_contact.setText(Html.fromHtml(url));
        loadingBar = new LoadingBar(getActivity());
        ((HomeActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        banner_list = new ArrayList<>();
        carList = new ArrayList<>();
        bannerList = new ArrayList<>();
        faq_list = new ArrayList<>();
        module = new Module(getActivity());
        list=new ArrayList<>();
        rv_package=view.findViewById(R.id.rv_package);
        txt_view_more.setOnClickListener(this);
        txt_more_faq.setOnClickListener(this);
if (ConnectivityReceiver.isConnected()) {
    getAppSettingData();
} else
{
    Intent intent = new Intent(getActivity(), NoInternetConnection.class);
    startActivity(intent);
}



        rv_package.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_package, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PackageModel model = list.get(position);
                Intent intent = new Intent(getActivity(), PackageDetails.class);
                intent.putExtra("plan_id",model.getPlan_id());
                intent.putExtra("plan_name",model.getPlan_name());
                intent.putExtra("plan_price",model.getPlan_price());
                intent.putExtra("plan_image",model.getPlan_image());
                intent.putExtra("plan_expiry",model.getPlan_expiry());
                intent.putExtra("plan_desc",model.getPlan_description());
                intent.putExtra("working_days",model.getPlan_no_of_working_days());
                intent.putExtra("plan_status",model.getPlan_status());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void makeGetSliderRequest() {

        HashMap<String ,String> params = new HashMap<>();
        JsonArrayRequest req = new JsonArrayRequest( BaseUrl.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response.get(i);
                                Slider_model model=new Slider_model();
                                model.setSlider_id(jsonObject.getString("slider_id"));
                                model.setSlider_title(jsonObject.getString("slider_title"));
                                model.setSlider_image(jsonObject.getString("slider_image"));
//                                model.setSlider_plan(jsonObject.getString("slider_plan"));
                                model.setStatus(jsonObject.getString("status"));
                                model.setIs_delete(jsonObject.getString("is_delete"));
                               carList.add(model);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
//                                url_maps.put("sub_cat", jsonObject.getString("slider_plan"));
                                url_maps.put("slider_image", BaseUrl.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
//                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
//                                home_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {

                                        if(name.get("sub_cat").equals("0"))
                                        {

                                        }
                                        else {
                                            //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
//                                            Bundle args = new Bundle();
//                                            android.app.Fragment fm = new Product_fragment();
//                                            args.putString("cat_id", sub_cat);
//                                            args.putString("title",name.get("slider_title"));
//                                            session_management.setCategoryId(sub_cat);
//                                            //Toast.makeText(getActivity(),""+sub_cat, Toast.LENGTH_LONG).show();
//                                            fm.setArguments(args);
//                                            FragmentManager fragmentManager = getFragmentManager();
//                                            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                                    .addToBackStack(null).commit();
                                        }
                                    }
                                });


                                carausalAdapter =new CarausalAdapter(carList,getActivity());
                                viewPager.setAdapter(carausalAdapter);
                                viewPager.setPadding(10,0,50,0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }
    private void makeGetBannerSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest( BaseUrl.GET_BANNERS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("banner", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                             Slider_model model=new Slider_model();
                                model.setSlider_id(jsonObject.getString("banner_id"));
                                model.setSlider_title(jsonObject.getString("banner_title"));
                                model.setSlider_image(jsonObject.getString("banner_image"));
//                                model.setSlider_plan(jsonObject.getString("banner_plan"));
                                model.setStatus(jsonObject.getString("status"));
                                model.setIs_delete(jsonObject.getString("is_delete"));
                                bannerList.add(model);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("banner_title", jsonObject.getString("banner_title"));
//                                url_maps.put("banner_plan", jsonObject.getString("banner_plan"));
                                url_maps.put("banner_image", BaseUrl.IMG_SLIDER_URL + jsonObject.getString("banner_image"));
                                url_maps.put("status",jsonObject.getString("status"));
                                url_maps.put("is_delete",jsonObject.getString("is_delete"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("banner_title"));
//                                textSliderView.getBundle().putString("extra", name.get("banner_plan"));
//                                home_banner.addSlider(textSliderView);
                                final String plan = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();


                                    }
                                });

                            }

                            carausalAdapter =new CarausalAdapter(bannerList,getActivity());
                            viewPager2.setAdapter(carausalAdapter);
                            viewPager2.setPadding(10,0,50,0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg=module.VolleyErrorMessage(error);
                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }
    private void getplans() {
        loadingBar.show();
        list.clear();
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseUrl.GET_PLANS, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                loadingBar.dismiss();

                if (response != null && response.length() > 0) {


                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<PackageModel>>() {
                    }.getType();
                    list = gson.fromJson(response.toString(), listType);
                    packageAdapter = new PackageAdapter(getActivity(),list);
                    rv_package.setAdapter(packageAdapter);
                    rv_package.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    packageAdapter.notifyDataSetChanged();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);

                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }
    private void getfaqs() {
        loadingBar.show();
        faq_list.clear();
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseUrl.FAQ, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("faqs", response.toString());
                loadingBar.dismiss();

                if (response != null && response.length() > 0) {


                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<FAQModel>>() {
                    }.getType();
                  faq_list = gson.fromJson(response.toString(), listType);
                    rv_faq.setLayoutManager(new LinearLayoutManager(getActivity()));
                    faqAdapter = new FAQAdapter(getActivity(),faq_list);
                    rv_faq.setAdapter(faqAdapter);
                    faqAdapter.notifyDataSetChanged();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);

                if(!msg.equals(""))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_LONG).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.view_more)
        {
            Fragment fm = new PackagesFragment();
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fm)
                    .addToBackStack(null).commit();
        }
        else  if (i == R.id.view_faq)
        {
            Fragment fm = new FaqFragment();
            FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame, fm)
                    .addToBackStack(null).commit();
        }
    }
    public void getAppSettingData()
    {
        loadingBar.show();
        String json_tag="json_app_tag";
        HashMap<String,String> map=new HashMap<>();

        CustomVolleyJsonRequest request=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.URL_UPDATER, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
                try
                {
                  boolean stat = response.getBoolean("responce");
                  if (stat)
                  {
                        JSONArray j_arr = response.getJSONArray("data");
                       JSONObject obj = j_arr.getJSONObject(0);

                        version_code=Integer.parseInt(obj.getString("version"));
                        app_link=obj.getString("app_link");
                        gst=obj.getString("gst");

                        if(getUpdaterInfo())
                        {
                            getplans();
                            makeGetSliderRequest();
                            autoSlide();
                            makeGetBannerSliderRequest();
                            getfaqs();

                        }
                        else
                        {
                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setCancelable(false);
                            builder.setMessage("The new version of app is available please update to get access.");
                            builder.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    String url = app_link;
                                    Intent in = new Intent(Intent.ACTION_VIEW);
                                    in.setData(Uri.parse(url));
                                    startActivity(in);
                                    getActivity().finish();
                                    //Toast.makeText(getActivity(),"updating",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    getActivity().finishAffinity();
                                }
                            });
                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }

}
                    } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                String msg=module.VolleyErrorMessage(error);
                if(!(msg.isEmpty() || msg.equals("")))
                {
                    Toast.makeText(getActivity(),""+msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(request,json_tag);
    }

    public boolean getUpdaterInfo()
    {
        boolean st=false;
        try
        {
            PackageInfo packageInfo=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            int ver_code=packageInfo.versionCode;
            if(ver_code == version_code)
            {
                st=true;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        Log.d("update_status", String.valueOf(st));
        return st;
    }

    public void autoSlide()
    {
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(current_position == Integer.MAX_VALUE)
                    current_position=0;
                viewPager.setCurrentItem(current_position++,true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        },250,2500);
    }
}
