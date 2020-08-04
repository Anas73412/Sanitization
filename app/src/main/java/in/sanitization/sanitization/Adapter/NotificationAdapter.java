package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.NotificationModel;
import in.sanitization.sanitization.R;
import in.sanitization.sanitization.util.NotificationHandler;
import in.sanitization.sanitization.util.ToastMsg;


/**
 * Developed by Binplus Technologies pvt. ltd.  on 08,July,2020
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Activity activity;
    ArrayList<NotificationModel> list;
    NotificationHandler notificationHandler;
    ToastMsg toastMsg;
    public NotificationAdapter(Activity activity, ArrayList<NotificationModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_user_notifications,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
       final NotificationModel model=list.get(position);
       holder.tv_title.setText(Html.fromHtml(model.getTitle()));
       if(notificationHandler.isInNotification(model.getId()))
       {
           holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.white));
       }
       else
       {
           holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.unread));
       }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RelativeLayout rel_main;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title=itemView.findViewById(R.id.tv_title);
            rel_main=itemView.findViewById(R.id.rel_main);
            notificationHandler=new NotificationHandler(activity);
            toastMsg=new ToastMsg(activity);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
