package in.sanitization.sanitization.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        FAQModel model = faq_list.get(position);
        holder.txt_faq.setText(model.getQuestion());
        holder.txt_ans.setText(model.getAnswer());
        holder.lin_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rel_ans.getVisibility()==View.VISIBLE)
                {
                    holder.rel_ans.setVisibility(View.GONE);
                }
                else
                {
                    holder.rel_ans.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return faq_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_faq,txt_ans;
        LinearLayout lin_faq ;
        RelativeLayout rel_ans ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_faq = itemView.findViewById(R.id.tv_faq);
            txt_ans = itemView.findViewById(R.id.tv_ans);
           lin_faq = itemView.findViewById(R.id.lin_faq);
           rel_ans = itemView.findViewById(R.id.rel_ans);
        }
    }
}
