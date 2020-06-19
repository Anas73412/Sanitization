package in.sanitization.sanitization.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.Socity_model;
import in.sanitization.sanitization.R;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 14,June,2020
 */
public class Socity_adapter extends RecyclerView.Adapter<Socity_adapter.MyViewHolder>
        {
private ArrayList<Socity_model> modelList;
private ArrayList<Socity_model> mFilteredList;

private Context context;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    LinearLayout lay_home;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.tv_socity_name);
        lay_home=view.findViewById(R.id.lay_home);
    }
}

    public Socity_adapter(ArrayList<Socity_model> modelList) {
        this.modelList = modelList;
        this.mFilteredList = modelList;
    }

    @Override
    public Socity_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_socity_rv, parent, false);

        context = parent.getContext();


        return new Socity_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Socity_adapter.MyViewHolder holder, int position) {
        Socity_model mList = mFilteredList.get(position);

        holder.title.setText(mList.getSociety()+" - "+mList.getPincode());
//        ((MainActivity)context).onBackPressed();

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    public void filterList(ArrayList<Socity_model> filteredList)
    {
        mFilteredList=filteredList;
        notifyDataSetChanged();
    }

}