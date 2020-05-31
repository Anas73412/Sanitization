package in.binplus.sanitization.Adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.binplus.sanitization.Model.PackageModel;
import in.binplus.sanitization.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 18,May,2020
 */
public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    Activity activity;
    ArrayList<PackageModel> list;

    public PackageAdapter(Activity activity, ArrayList<PackageModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_packages,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PackageModel model=list.get(position);
        holder.tv_pck_name.setText(model.getPlan_name());
        holder.tv_amount.setText(activity.getResources().getString(R.string.currency)+" "+model.getPlan_price());
        holder.tv_desc.setText(model.getProduct_name());

        switch (position%4)
        {
            case 0:holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_1));
                holder.rel_next.getBackground().setTint(activity.getResources().getColor(R.color.color_3));
                break;

            case 1:holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_2));
                holder.rel_next.getBackground().setTint(activity.getResources().getColor(R.color.color_4));
                break;
            case 2:holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_3));
                holder.rel_next.getBackground().setTint(activity.getResources().getColor(R.color.color_1));
                break;
            case 3:holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_4));
                holder.rel_next.getBackground().setTint(activity.getResources().getColor(R.color.color_2));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_container;
        TextView tv_pck_name,tv_amount,tv_desc;
        RelativeLayout rel_next;
        public ViewHolder(@NonNull View v) {
            super(v);
            lin_container=v.findViewById(R.id.lin_container);
            rel_next=v.findViewById(R.id.rel_next);
            tv_pck_name=v.findViewById(R.id.tv_pck_name);
            tv_amount=v.findViewById(R.id.tv_amount);
            tv_desc=v.findViewById(R.id.tv_desc);
        }
    }
}
