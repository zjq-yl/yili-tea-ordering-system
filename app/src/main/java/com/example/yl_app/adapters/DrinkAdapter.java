package com.example.yl_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.models.DrinkItem;
import java.util.List;
import com.example.yl_app.ui.menu.DrinkDetailDialog;
public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> {

    private Context context;
    private List<DrinkItem> items;

    public DrinkAdapter(Context context, List<DrinkItem> items) {
        this.context = context;
        this.items = items;
    }

    public void updateData(List<DrinkItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drink, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrinkItem item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("¥" + item.getPrice());
        holder.slogan.setText(item.getSlogan());
        holder.image.setImageResource(item.getImageRes());

        holder.addBtn.setOnClickListener(v -> {
            DrinkDetailDialog.show(context, item.getName(), item.getPrice(), item.getImageRes());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, addBtn, slogan;
        ImageView image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.drink_name);
            price = itemView.findViewById(R.id.drink_price);
            addBtn = itemView.findViewById(R.id.add_to_cart);
            slogan = itemView.findViewById(R.id.drink_slogan);
            image = itemView.findViewById(R.id.drink_image);
        }
    }
}