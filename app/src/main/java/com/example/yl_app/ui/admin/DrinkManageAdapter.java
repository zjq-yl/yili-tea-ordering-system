package com.example.yl_app.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.database.DrinkEntity;
import java.util.List;

public class DrinkManageAdapter extends RecyclerView.Adapter<DrinkManageAdapter.ViewHolder> {

    private Context context;
    private List<DrinkEntity> list;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnEditListener {
        void onEdit(DrinkEntity drink);
    }

    public interface OnDeleteListener {
        void onDelete(DrinkEntity drink);
    }

    public DrinkManageAdapter(Context context, List<DrinkEntity> list, OnEditListener editListener, OnDeleteListener deleteListener) {
        this.context = context;
        this.list = list;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_drink, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrinkEntity drink = list.get(position);
        holder.tvName.setText(drink.getName());
        holder.tvPrice.setText("¥" + drink.getPrice());

        // 根据标签显示不同颜色
        if (drink.getTag().equals("人气爆款")) {
            holder.tvTag.setText("🔥 人气爆款");
            holder.tvTag.setTextColor(context.getColor(R.color.tag_hot));
        } else if (drink.getTag().equals("新品")) {
            holder.tvTag.setText("✨ 新品");
            holder.tvTag.setTextColor(context.getColor(R.color.tag_new));
        } else {
            holder.tvTag.setText("");
        }

        // 设置图片（根据图片名从drawable获取）
        int resId = context.getResources().getIdentifier(drink.getImageName(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.ivDrink.setImageResource(resId);
        }

        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(drink));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(drink));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDrink;
        TextView tvName, tvPrice, tvTag;
        Button btnEdit, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDrink = itemView.findViewById(R.id.iv_drink);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvTag = itemView.findViewById(R.id.tv_tag);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}