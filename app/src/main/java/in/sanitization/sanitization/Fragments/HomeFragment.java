package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
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

import in.sanitization.sanitization.Adapter.CarausalAdapter;
import in.sanitization.sanitization.Adapter.FAQAdapter;
import in.sanitization.sanitization.Adapter.PackageAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.CustomSlider;
import in.sanitization.sanitization.Model.BannerModel;
import in.sanitization.sanitization.Model.FAQModel;
import in.sanitization.sanitization.Model.PackageModel;
import in.sanitization.sanitization.Model.Slider_model;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.LoadingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    RecyclerView rv_faq;
    SliderLayout home_slider ,home_banner;
    TextView txt_contact,txt_message ,txt_about,txt_version;
    Module module ;
    LoadingBar loadingBar ;
    String url = "contact us";
    FAQAdapter faqAdapter;
    ArrayList<Slider_model> carList;
    ArrayList<Slider_model> bannerList;
    ArrayList<BannerModel>banner_list;
    ArrayList<FAQModel>faq_list;
    ViewPager viewPager ,viewPager2;
    CarausalAdapter carausalAdapter;

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
       return view;
    }
    private void initViews(View view) {

        rv_faq=view.findViewById(R.id.rv_faq);
        home_slider = view.findViewById(R.id.slider);
//        home_banner = view.findViewById(R.id.banner);
        txt_about= view.findViewById(R.id.txt_about);
        txt_contact = view.findViewById(R.id.txt_contact);
        txt_message = view.findViewById(R.id.txt_message);
        txt_version = view.findViewById(R.id.txt_version);
        viewPager = view.findViewById(R.id.viewPager);
        viewPager2 = view.findViewById(R.id.viewPager2);
        txt_contact.setText(Html.fromHtml(url));
        loadingBar = new LoadingBar(getActivity());

        banner_list = new ArrayList<>();
        carList = new ArrayList<>();
        bannerList = new ArrayList<>();
        faq_list = new ArrayList<>();
        module = new Module(getActivity());
        list=new ArrayList<>();
        rv_package=view.findViewById(R.id.rv_package);



        rv_faq.setLayoutManager(new LinearLayoutManager(getActivity()));
        faq_list.add(new FAQModel("How is it beneficial",""));
        faq_list.add(new FAQModel("Cost for Sanitization",""));
        faq_list.add(new FAQModel("Duration for the process",""));
        faq_list.add(new FAQModel("What are the precautions to be taken",""));
        faqAdapter = new FAQAdapter(getActivity(),faq_list);
        rv_faq.setAdapter(faqAdapter);

        getplans();
        makeGetSliderRequest();
        makeGetBannerSliderRequest();

    }

    private void makeGetSliderRequest() {

        HashMap<String ,String> params = new HashMap<>();
        JsonArrayRequest req = new JsonArrayRequest( BaseUrl.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("sliders", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response.get(i);
                                Slider_model model=new Slider_model();
                                model.setSlider_id(jsonObject.getString("slider_id"));
                                model.setSlider_title(jsonObject.getString("slider_title"));
                                model.setSlider_image(jsonObject.getString("slider_image"));
                                model.setSlider_plan(jsonObject.getString("slider_plan"));
                                model.setStatus(jsonObject.getString("status"));
                                model.setIs_delete(jsonObject.getString("is_delete"));
                               carList.add(model);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("slider_plan"));
                                url_maps.put("slider_image", BaseUrl.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                home_slider.addSlider(textSliderView);
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

                                Log.e("assad",""+carList.size());
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
                                model.setSlider_plan(jsonObject.getString("banner_plan"));
                                model.setStatus(jsonObject.getString("status"));
                                model.setIs_delete(jsonObject.getString("is_delete"));
                                bannerList.add(model);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("banner_title", jsonObject.getString("banner_title"));
                                url_maps.put("banner_plan", jsonObject.getString("banner_plan"));
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
                                textSliderView.getBundle().putString("extra", name.get("banner_plan"));
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

                            Log.e("bannnerlist",""+bannerList.size());
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
                Log.d("plans", response.toString());
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
}
