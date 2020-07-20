package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.PackageModel;
import in.sanitization.sanitization.R;

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
        View view= LayoutInflater.from(activity).inflate(R.layout.row_pack,null);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PackageModel model=list.get(position);
        holder.tv_pck_name.setText(model.getPlan_name());
        holder.tv_amount.setText(activity.getResources().getString(R.string.currency)+" "+model.getPlan_price());
        holder.tv_desc.setText(model.getPlan_name());

        if (model.getPlan_name().toLowerCase().contains("apartment"))
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.apartmentsanitization));
        }
        else if (model.getPlan_name().toLowerCase().contains("showroom"))
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.showroom));
        }
        else if (model.getPlan_name().toLowerCase().contains("bank"))
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.banksanitization));
        }
        else if (model.getPlan_name().toLowerCase().contains("school"))
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.schoolsanitization));
        }
        else if (model.getPlan_name().toLowerCase().contains("office"))
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.officesanitization));
        }
        else if (model.getPlan_name().toLowerCase().contains("residential"))
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.residentialsanitization));
        }
//        else if (model.getPlan_name().toLowerCase().contains("colony"))
        else
        {
            holder.iv_pack.setBackground(activity.getResources().getDrawable(R.drawable.commonsanitization));
        }

//      switch (position%4)
//        {
//            case 0:
////                holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_1));
////                holder.iv_back.getBackground().setTint(activity.getResources().getColor(R.color.color_3));
//                break;
//
//            case 1:
////                holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_2));
////                holder.iv_back.getBackground().setTint(activity.getResources().getColor(R.color.color_4));
//                break;
//            case 2:
////                holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_3));
////                holder.iv_back.getBackground().setTint(activity.getResources().getColor(R.color.color_1));
//                break;
//            case 3:
////                holder.lin_container.getBackground().setTint(activity.getResources().getColor(R.color.color_4));
////                holder.iv_back.getBackground().setTint(activity.getResources().getColor(R.color.color_2));
//                break;
//        }
    }

    @Override
    public int getItemCount()
    {
//        if (list.size()<4) {
//            return list.size();
//        }
//        else
//        {
//            return  4 ;
//        }
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_container;
        ImageView iv_back ,iv_pack;
        TextView tv_pck_name,tv_amount,tv_desc;
        RelativeLayout rel_pack;
        public ViewHolder(@NonNull View v) {
            super(v);
            lin_container=v.findViewById(R.id.lin_container);
            rel_pack=v.findViewById(R.id.rel_package);
//            iv_back=v.findViewById(R.id.iv_back);
            iv_pack=v.findViewById(R.id.iv_img);
            tv_pck_name=v.findViewById(R.id.tv_pck_name);
            tv_amount=v.findViewById(R.id.tv_amount);
            tv_desc=v.findViewById(R.id.tv_desc);
        }
    }
}
