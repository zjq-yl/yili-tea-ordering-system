package com.example.yl_app.ui.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.yl_app.R;
import com.example.yl_app.database.AddressEntity;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<AddressEntity> list;
    private OnSetDefaultListener defaultListener;
    private OnEditListener editListener;
    private OnDeleteListener deleteListener;

    public interface OnSetDefaultListener {
        void onSetDefault(AddressEntity address);
    }
    public interface OnEditListener {
        void onEdit(AddressEntity address);
    }
    public interface OnDeleteListener {
        void onDelete(AddressEntity address);
    }

    public AddressAdapter(Context context, List<AddressEntity> list,
                          OnSetDefaultListener defaultListener,
                          OnEditListener editListener,
                          OnDeleteListener deleteListener) {
        this.context = context;
        this.list = list;
        this.defaultListener = defaultListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressEntity address = list.get(position);
        holder.tvName.setText(address.getName());
        holder.tvPhone.setText(address.getPhone());
        holder.tvAddress.setText(address.getAddress());

        if (address.isDefault()) {
            holder.tvDefault.setVisibility(View.VISIBLE);
            holder.btnSetDefault.setVisibility(View.GONE);
        } else {
            holder.tvDefault.setVisibility(View.GONE);
            holder.btnSetDefault.setVisibility(View.VISIBLE);
            holder.btnSetDefault.setOnClickListener(v -> defaultListener.onSetDefault(address));
        }

        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(address));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(address));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvAddress, tvDefault, btnSetDefault, btnEdit, btnDelete;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDefault = itemView.findViewById(R.id.tv_default);
            btnSetDefault = itemView.findViewById(R.id.btn_set_default);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}