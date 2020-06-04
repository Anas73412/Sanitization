package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.sanitization.sanitization.Model.FAQModel;
import in.sanitization.sanitization.R;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.ViewHolder> {
    Activity activity ;
    ArrayList<FAQModel>faq_list ;

    public FAQAdapter(Activity activity, ArrayList<FAQModel> faq_list) {
        this.activity = activity;
        this.faq_list = faq_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_faq,null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FAQModel model = faq_list.get(position);
        holder.txt_faq.setText(model.getQuestion());

    }

    @Override
    public int getItemCount() {
        return faq_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_faq;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_faq = itemView.findViewById(R.id.tv_faq);
        }
    }
}
