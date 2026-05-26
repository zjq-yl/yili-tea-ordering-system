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

public class HotDrinkAdapter extends RecyclerView.Adapter<HotDrinkAdapter.ViewHolder> {

    private Context context;
    private List<DrinkItem> items;

    public HotDrinkAdapter(Context context, List<DrinkItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hot_drink, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrinkItem item = items.get(position);
        holder.drinkName.setText(item.getName());
        holder.drinkImage.setImageResource(item.getImageRes());

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView drinkImage;
        TextView drinkName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            drinkImage = itemView.findViewById(R.id.drink_image);
            drinkName = itemView.findViewById(R.id.drink_name);
        }
    }
}