package in.sanitization.sanitization.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

import in.sanitization.sanitization.Adapter.NotificationAdapter;
import in.sanitization.sanitization.AppController;
import in.sanitization.sanitization.Config.BaseUrl;
import in.sanitization.sanitization.Config.Module;
import in.sanitization.sanitization.HomeActivity;
import in.sanitization.sanitization.Model.NotificationModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.CustomVolleyJsonArrayRequest;
import in.sanitization.sanitization.util.LoadingBar;
import in.sanitization.sanitization.util.NotificationHandler;
import in.sanitization.sanitization.util.RecyclerTouchListener;
import in.sanitization.sanitization.util.Session_management;
import in.sanitization.sanitization.util.ToastMsg;

import static in.sanitization.sanitization.util.NotificationHandler.COLUMN_CID;
import static in.sanitization.sanitization.util.NotificationHandler.COLUMN_ID;
import static in.sanitization.sanitization.util.NotificationHandler.COLUMN_TITLE;

public class NotificationFragment extends Fragment {

    public final String TAG=NotificationFragment.class.getSimpleName().toString();
    SwipeRefreshLayout rf_ntfs;
    Module module;
    ToastMsg toastMsg;
    LoadingBar loadingBar;
    NotificationHandler notificationHandler;
    Session_management session_management;
    RecyclerView rv_notifications;
    RelativeLayout rel_no_items;
    ArrayList<NotificationModel> list;
    NotificationAdapter adapter;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View v) {
        rf_ntfs=v.findViewById(R.id.rf_ntfs);
        rv_notifications=v.findViewById(R.id.rv_notifications);
        rel_no_items=v.findViewById(R.id.rel_no_items);
        session_management=new Session_management(getActivity());
        module=new Module(getActivity());
        toastMsg=new ToastMsg(getActivity());
        loadingBar=new LoadingBar(getActivity());
        notificationHandler=new NotificationHandler(getActivity());
        list=new ArrayList<>();
        getAllNotifications();

        rv_notifications.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_notifications, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NotificationModel model=list.get(position);
                int c_id=notificationHandler.getNotificationCount();

                HashMap<String,String> map=new HashMap<>();
                map.put(COLUMN_CID,String.valueOf(c_id+1));
                map.put(COLUMN_ID,model.getId());
                map.put(COLUMN_TITLE,model.getTitle());
                try {
                    boolean insert = notificationHandler.setNotification(map);
                    if (insert) {
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                          int list_size=list.size();
                        int readNotifications = notificationHandler.getNotificationCount();
                        int unreadNotifcations = list_size - readNotifications;
                        ((HomeActivity)getActivity()).setNotificationCounter(unreadNotifcations);

                    } else {
//                       toastMsg.toastIconError("Something went wrong");
                    }
                    Fragment fm=new ReadNotificationFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("title",model.getTitle());
                    bundle.putString("desc",model.getDescription());
                    fm.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fm)
                            .addToBackStack(null).commit();

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void getAllNotifications() {
        loadingBar.show();
        list.clear();
        HashMap<String,String> params=new HashMap<>();
        CustomVolleyJsonArrayRequest arrayRequest=new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseUrl.NOTIFICATIONS_URL, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingBar.dismiss();
                try {
                    Gson gson=new Gson();
                    Type typeList=new TypeToken<List<NotificationModel>>(){}.getType();
                    list=gson.fromJson(response.toString(),typeList);
                    rv_notifications.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter =new NotificationAdapter(getActivity(),list);
                    rv_notifications.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                module.errMessage(error);
            }
        });
        AppController.getInstance().addToRequestQueue(arrayRequest);
    }
}