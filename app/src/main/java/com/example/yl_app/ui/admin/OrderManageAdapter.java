package com.example.yl_app.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.database.OrderEntity;
import java.util.List;

public class OrderManageAdapter extends RecyclerView.Adapter<OrderManageAdapter.ViewHolder> {

    private Context context;
    private List<OrderEntity> list;
    private OnCompleteListener listener;

    public interface OnCompleteListener {
        void onComplete(OrderEntity order);
    }

    public OrderManageAdapter(Context context, List<OrderEntity> list, OnCompleteListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderEntity order = list.get(position);
        holder.tvOrderId.setText("订单号: " + order.getOrderId());
        holder.tvUserId.setText("用户: " + order.getUserId());  // 显示用户ID
        holder.tvDate.setText(order.getDate());
        holder.tvItems.setText(order.getItems());
        holder.tvTotal.setText("¥" + order.getTotal());
        holder.tvStatus.setText(order.getStatus());

        if ("进行中".equals(order.getStatus())) {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnComplete.setOnClickListener(v -> listener.onComplete(order));
        } else {
            holder.btnComplete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserId,tvOrderId, tvDate, tvItems, tvTotal, tvStatus;
        Button btnComplete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvItems = itemView.findViewById(R.id.tv_items);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnComplete = itemView.findViewById(R.id.btn_complete);
        }
    }
}