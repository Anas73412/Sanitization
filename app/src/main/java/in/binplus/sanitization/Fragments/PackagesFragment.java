package in.binplus.sanitization.Fragments;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import in.binplus.sanitization.Adapter.PackageAdapter;
import in.binplus.sanitization.AppController;
import in.binplus.sanitization.Config.BaseUrl;
import in.binplus.sanitization.Config.Module;
import in.binplus.sanitization.CustomSlider;
import in.binplus.sanitization.Model.BannerModel;
import in.binplus.sanitization.Model.PackageModel;
import in.binplus.sanitization.PackageDetails;
import in.binplus.sanitization.R;
import in.binplus.sanitization.util.CustomVolleyJsonArrayRequest;
import in.binplus.sanitization.util.CustomVolleyJsonRequest;
import in.binplus.sanitization.util.LoadingBar;
import in.binplus.sanitization.util.RecyclerTouchListener;

import static in.binplus.sanitization.AppController.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackagesFragment extends Fragment {

    PackageAdapter packageAdapter;
    ArrayList<PackageModel> list;
    ArrayList<BannerModel>banner_list;
    RecyclerView rv_package;
    SliderLayout home_slider ,home_banner;

    Module module ;
    LoadingBar loadingBar ;

    public PackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_packages, container, false);
        initViews(view);


    rv_package.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_package, new RecyclerTouchListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            PackageModel model = list.get(position);
            Intent intent = new Intent(getActivity(), PackageDetails.class);
            intent.putExtra("plan_id",model.getPlan_id());
            intent.putExtra("plan_name",model.getPlan_name());
            intent.putExtra("plan_price",model.getPlan_price());
            intent.putExtra("plan_image",model.getPlan_image());
            intent.putExtra("plan_status",model.getPlan_status());
            intent.putExtra("plan_product",model.getProduct_name());
            startActivity(intent);
        }

        @Override
        public void onLongItemClick(View view, int position) {

        }
    }));
        return view;
    }

    private void initViews(View view) {
        rv_package=view.findViewById(R.id.rv_package);
        home_slider = view.findViewById(R.id.slider);
        home_banner = view.findViewById(R.id.banner);
        loadingBar = new LoadingBar(getActivity());
        list=new ArrayList<>();
        banner_list = new ArrayList<>();

        module = new Module(getActivity());

        rv_package.setLayoutManager(new LinearLayoutManager(getActivity()));

        makeGetSliderRequest();
        getplans();
        makeGetBannerSliderRequest();
    }

    private void makeGetSliderRequest() {

        HashMap<String ,String>params = new HashMap<>();
        JsonArrayRequest req = new JsonArrayRequest( BaseUrl.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("sliders", response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
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
    private void getplans() {
//       loadingBar.show();
        list.clear();
        Map<String, String> params = new HashMap<String, String>();

       CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseUrl.GET_PLANS, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("plans", response.toString());


                if (response != null && response.length() > 0) {


                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<PackageModel>>() {
                        }.getType();
                        list = gson.fromJson(response.toString(), listType);
                      packageAdapter = new PackageAdapter(getActivity(),list);
                        rv_package.setAdapter(packageAdapter);
                     packageAdapter.notifyDataSetChanged();

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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,"plans");

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
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("banner_title", jsonObject.getString("banner_title"));
                                url_maps.put("banner_plan", jsonObject.getString("banner_plan"));
                                url_maps.put("banner_image", BaseUrl.IMG_SLIDER_URL + jsonObject.getString("banner_image"));
                                url_maps.put("status",jsonObject.getString("status"));
                                url_maps.put("is_delete",jsonObject.getString("is_delete"));


                                //   Toast.makeText(context,""+modelList.get(position).getProduct_image(),Toast.LENGTH_LONG).show();

                                listarray.add(url_maps);
                            }
                            for (final HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType( BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("banner_title"));
                                textSliderView.getBundle().putString("extra", name.get("banner_plan"));
                                home_banner.addSlider(textSliderView);
                                final String plan = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();


                                    }
                                });

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
}
