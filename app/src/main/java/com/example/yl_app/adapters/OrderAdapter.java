package com.example.yl_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.models.OrderItem;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderItem> orders;

    public OrderAdapter(Context context, List<OrderItem> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = orders.get(position);
        holder.orderId.setText("订单号: " + item.getOrderId());
        holder.orderStatus.setText(item.getStatus());
        holder.orderItems.setText(item.getItems());
        holder.orderDate.setText(item.getDate());
        holder.orderTotal.setText("¥" + item.getTotal());

        if ("已完成".equals(item.getStatus())) {
            holder.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.purple_medium));
        } else if ("进行中".equals(item.getStatus())) {
            holder.orderStatus.setTextColor(ContextCompat.getColor(context, R.color.purple_dark));
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderStatus, orderItems, orderDate, orderTotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderItems = itemView.findViewById(R.id.order_items);
            orderDate = itemView.findViewById(R.id.order_date);
            orderTotal = itemView.findViewById(R.id.order_total);
        }
    }
}