package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.KeyValuePairModel;
import in.sanitization.sanitization.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 16,July,2020
 */
public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
    ArrayList<KeyValuePairModel> list;
    Activity activity;

    public LogsAdapter(ArrayList<KeyValuePairModel> list, Activity activity) {
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

        holder.tv_log.setText(list.get(position).getKey()+" : "+list.get(position).getValue().toString());
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
