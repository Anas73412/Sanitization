package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import in.sanitization.sanitization.Adapter.FAQAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.MainActivity;
import in.sanitization.sanitization.Model.FAQModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.LoadingBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaqFragment extends Fragment {
    ArrayList<FAQModel>faq_list;
    FAQAdapter faqAdapter;
    RecyclerView rv_faq;
    LoadingBar loadingBar;
    Module module ;

    public FaqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_faq, container, false);
        rv_faq=view.findViewById(R.id.rv_faq);
        loadingBar = new LoadingBar(getActivity());
        module = new Module(getActivity());
        faq_list = new ArrayList<>();
        rv_faq.setLayoutManager(new LinearLayoutManager(getActivity()));
        getfaqs();
        ((HomeActivity) getActivity()).setTitle("FAQs");
       return view ;
    }
    private void getfaqs() {
        loadingBar.show();
        faq_list.clear();
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseUrl.FAQ, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("plans", response.toString());
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
}
