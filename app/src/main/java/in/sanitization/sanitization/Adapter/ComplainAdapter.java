package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.ComplainModel;
import in.sanitization.sanitization.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 17,July,2020
 */
public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ViewHolder> {
    ArrayList<ComplainModel> list;
    Activity activity;

    public ComplainAdapter(ArrayList<ComplainModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity).inflate(R.layout.row_logs,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     String[] time=list.get(position).getCreated_at().split(" ");
     holder.tv_log.setText(time[0].toString() +" : "+list.get(position).getComplain());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_log;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_log=itemView.findViewById(R.id.tv_log);
        }
    }
}
