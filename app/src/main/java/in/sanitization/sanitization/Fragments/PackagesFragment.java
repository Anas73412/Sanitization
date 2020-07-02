package in.sanitization.sanitization.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.sanitization.sanitization.Adapter.PackageAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.PackageModel;
import in.sanitization.sanitization.PackageDetails;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackagesFragment extends Fragment {

    PackageAdapter packageAdapter;
   Module module ;
    ArrayList<PackageModel> list;
    LoadingBar loadingBar ;
    RecyclerView rv_package ;


    public PackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_packages, container, false);
        ((HomeActivity) getActivity()).setTitle("Our Packages");
        list=new ArrayList<>();
        rv_package=view.findViewById(R.id.rv_package);
        module = new Module(getActivity());
        loadingBar = new LoadingBar(getActivity());
        if (ConnectivityReceiver.isConnected()) {
            getplans();
        } else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            startActivity(intent);
        }
        rv_package.setLayoutManager(new GridLayoutManager(getActivity(),2));
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
