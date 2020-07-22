package in.sanitization.sanitization.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.sanitization.sanitization.Model.OrderModel;
import in.sanitization.sanitization.R;

import static android.content.Context.MODE_PRIVATE;

public class Order_adapter extends RecyclerView.Adapter<Order_adapter.MyViewHolder> {

    private List<OrderModel> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
    SharedPreferences preferences;
    private Context context;

    public Order_adapter(List<OrderModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date,tv_email,user_address;
        public TextView tv_pending_date, tv_pending_time, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
        public View view1, view2, view3, view4, view5, view6;
        public RelativeLayout relative_background;
        public ImageView Confirm, Out_For_Deliverde, Delivered;
        CardView cardView;
        public TextView tv_methid1;
        public String method;
        LinearLayout linearLayout,lin_expected;

        public MyViewHolder(View view) {

            super(view);
            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            tv_email = (TextView) view.findViewById(R.id.email);
            tv_methid1 = (TextView) view.findViewById(R.id.method1);
            user_address = (TextView) view.findViewById(R.id.user_address);
            relative_background=view.findViewById(R.id.relative_background);
            cardView = view.findViewById(R.id.card_view);

        }
    }


    @Override
    public Order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_past_order_rv, parent, false);
        context = parent.getContext();
        return new Order_adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(Order_adapter.MyViewHolder holder, int position) {
      OrderModel  mList = modelList.get(position);

        holder.tv_orderno.setText("A2Z_ID"+mList.getOrder_id());


        if (mList.getStatus().equals("0")) {
            holder.tv_status.setText(context.getResources().getString(R.string.pending));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_1));
            holder.relative_background.setBackgroundTintList(context.getResources().getColorStateList(R.color.color_1));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));

        } else if (mList.getStatus().equals("1")) {
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green_500));
            holder.relative_background.setBackgroundTintList(context.getResources().getColorStateList(R.color.green_500));
            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));

        }
        else if (mList.getStatus().equals("3")) {
            holder.relative_background.setBackgroundTintList(context.getResources().getColorStateList(R.color.red_600));
            holder.tv_status.setText("Cancelled");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.red_600));
            holder.relativetextstatus.setText("Cancelled");

        }
        else if (mList.getStatus().equals("4")) {
            holder.relative_background.setBackgroundTintList(context.getResources().getColorStateList(R.color.color_2));
            holder.tv_status.setText("Completed");
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.color_2));
            holder.relativetextstatus.setText("Completed");
        }

        holder.tv_date.setText(mList.getOrder_date());

            String created_at=mList.getCreated_at();
            String time = created_at.substring(11,created_at.length());
            holder.tv_time.setText(time);

     holder.tv_email.setText(mList.getReceiver_mobile());
     holder.tv_price.setText(context.getResources().getString(R.string.currency)+mList.getGross_amount());
      holder.user_address.setText(mList.getReceiver_name());
      holder.tv_methid1.setText(mList.getPayment());
    }
    public void removeddata(int postion){
        modelList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion,getItemCount());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

