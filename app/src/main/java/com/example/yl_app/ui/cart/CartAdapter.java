package com.example.yl_app.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.models.CartItem;
import com.example.yl_app.utils.CartManager;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartItem> list;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> list, OnCartChangeListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = list.get(position);
        holder.tvName.setText(item.getName());
        holder.tvSpec.setText("甜度:" + item.getSugar() + " / 冰量:" + item.getIce() + " / 加料:" + item.getToppings());
        holder.tvPrice.setText("¥" + item.getPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.ivImage.setImageResource(item.getImageRes());

        // 减号按钮
        holder.btnMinus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            int newQty = item.getQuantity() - 1;
            if (newQty <= 0) {
                // 删除商品
                list.remove(pos);
                CartManager.getInstance(context).removeItem(pos);
                notifyItemRemoved(pos);
                // 通知刷新购物车
                if (listener != null) {
                    listener.onCartChanged();
                }
            } else {
                item.setQuantity(newQty);
                CartManager.getInstance(context).updateQuantity(pos, newQty);
                holder.tvQuantity.setText(String.valueOf(newQty));
                if (listener != null) {
                    listener.onCartChanged();
                }
            }
        });

        // 加号按钮
        holder.btnPlus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;

            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            CartManager.getInstance(context).updateQuantity(pos, newQty);
            holder.tvQuantity.setText(String.valueOf(newQty));
            if (listener != null) {
                listener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvSpec, tvPrice, tvQuantity, btnMinus, btnPlus;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSpec = itemView.findViewById(R.id.tv_spec);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            btnMinus = itemView.findViewById(R.id.btn_minus);
            btnPlus = itemView.findViewById(R.id.btn_plus);
        }
    }
}