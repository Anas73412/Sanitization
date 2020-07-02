package in.sanitization.sanitization.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.sanitization.sanitization.Adapter.SocietyAdapter;
import in.sanitization.sanitization.Adapter.Socity_adapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.Model.Socity_model;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.SubscriptionActivity;
import in.sanitization.sanitization.networkconnectivity.NoInternetConnection;
import in.sanitization.sanitization.util.ConnectivityReceiver;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.CustomVolleyJsonRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.RecyclerTouchListener;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

/**
 * A simple {@link Fragment} subclass.
 */
public class SocietyFragment extends Fragment {
    private static String TAG = SocietyFragment.class.getSimpleName();

    private AutoCompleteTextView et_search;
    private RecyclerView rv_socity;
    ArrayList<Socity_model> filteredList=new ArrayList<>();
    TextView tv_view_all;
    private ArrayList<Socity_model> socity_modelList = new ArrayList<>();
    private Socity_adapter adapter;
    ProgressDialog progressDialog ;
    Module module;
    LoadingBar loadingBar ;
    ToastMsg toastMsg;
    String city="";
    Session_management sessionManagement;


    public SocietyFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_society, container, false);
        initViews(view);
        tv_view_all=(TextView)view.findViewById(R.id.tv_view_all);
        et_search = (AutoCompleteTextView) view.findViewById(R.id.et_socity_search);
        et_search.setThreshold(1);
        sessionManagement = new Session_management(getActivity());
        et_search.setTextColor(getResources().getColor(R.color.green));
        rv_socity = (RecyclerView) view.findViewById(R.id.rv_socity);
        rv_socity.setLayoutManager(new LinearLayoutManager(getActivity()));
        toastMsg=new ToastMsg(getActivity());
        city=getArguments().getString("city");
        et_search.setAdapter(new SocietyAdapter(getActivity(),city,et_search.getText().toString()));
        tv_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeGetSocityRequest(city);


            }
        });
        sessionManagement.clearSocities();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //   adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetSocityRequest(city);
        } else
        {
            Intent intent = new Intent(getActivity(), NoInternetConnection.class);
            startActivity(intent);
        }

        rv_socity.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_socity, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String socity_id = filteredList.get(position).getSociety_id();
                String socity_name = filteredList.get(position).getSociety();
                String socity_pincode = filteredList.get(position).getPincode();
                sessionManagement.updateSocity(socity_name,socity_id,socity_pincode);
                ((SubscriptionActivity) getActivity()).onBackPressed();
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        return view;
    }

    private void initViews(View view) {
        module=new Module(getActivity());
        loadingBar=new LoadingBar(getActivity());
        toastMsg=new ToastMsg(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void filter(String s) {

        filteredList.clear();
        ArrayList<Socity_model> tempList=new ArrayList<>();
        for(Socity_model socity_model : socity_modelList)
        {
            if(socity_model.getSociety().toLowerCase().contains(s.toLowerCase().toString()))
            {
                tempList.add(socity_model);
            }
        }
        filteredList.addAll(tempList);
        adapter.filterList(filteredList);
    }

    private void filterAll() {

        filteredList.clear();
        ArrayList<Socity_model> tempList=new ArrayList<>();
        for(Socity_model socity_model : socity_modelList)
        {
            tempList.add(socity_model);

        }
        filteredList.addAll(tempList);
        adapter.filterList(filteredList);
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetSocityRequest(final String city) {
        loadingBar.show();
        // Tag used to cancel the request
        String tag_json_obj = "json_socity_req";
        HashMap<String,String> params=new HashMap<>();
        params.put("city",city);

        CustomVolleyJsonRequest customVolleyJsonRequest=new CustomVolleyJsonRequest(Request.Method.POST, BaseUrl.GET_SOCITY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingBar.dismiss();
             try {
                 Log.e("sociteiess",""+response.toString());
                 boolean resp=response.getBoolean("responce");
                 if(resp)
                 {
                     JSONArray object=response.getJSONArray("data");
                     Gson gson = new Gson();
                     Type listType = new TypeToken<List<Socity_model>>() {
                     }.getType();

                     socity_modelList = gson.fromJson(object.toString(), listType);
                     filteredList = gson.fromJson(object.toString(), listType);
                     adapter = new Socity_adapter(socity_modelList);
                     rv_socity.setAdapter(adapter);
                     adapter.notifyDataSetChanged();
                     if(socity_modelList.size()<=0)
                     {
                         toastMsg.toastIconError("No Records Found");
                     }
                 }
             }
             catch (Exception ex)
             {
                 ex.printStackTrace();
             }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                String msg=module.VolleyErrorMessage(error);
                if(!msg.isEmpty())
                {
                    module.showToast(""+msg);
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(customVolleyJsonRequest, tag_json_obj);
    }

}