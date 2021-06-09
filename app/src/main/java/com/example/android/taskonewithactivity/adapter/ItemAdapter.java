package com.example.android.taskonewithactivity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.taskonewithactivity.R;
import com.example.android.taskonewithactivity.listener.ItemListener;
import com.example.android.taskonewithactivity.model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements Filterable {

    private List<Item> items;
    private List<Item> itemListFiltered;
    private ItemListener listener;

    public ItemAdapter(List<Item> items, ItemListener listener){
        this.items = items;
        this.itemListFiltered = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, final int position) {
        holder.tvFirstName.setText(itemListFiltered.get(position).mFirstName);
        holder.tvLastName.setText(itemListFiltered.get(position).mLastName);
        holder.tvEmail.setText(itemListFiltered.get(position).mEmail);
        holder.tvAddress.setText(itemListFiltered.get(position).mAddress);
        holder.tvWeight.setText(itemListFiltered.get(position).weight);
        holder.tvHeight.setText(itemListFiltered.get(position).height);
        holder.tvPhone.setText(itemListFiltered.get(position).phoneNo);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = dateFormat.format(itemListFiltered.get(position).date);
        holder.tvDate.setText(dateInString);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onEdit(position, itemListFiltered.get(position).cursorId);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onDelete(position, itemListFiltered.get(position).cursorId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    public void setData(List<Item> items) {
        this.items = items;
        this.itemListFiltered = items;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()){
                    itemListFiltered = items;
                }
                else {
                    List<Item> filteredList = new ArrayList<>();
                    for (Item row : items){
                        String dateInString = row.date.toString();

                        if (row.mFirstName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.mLastName.toLowerCase().contains(charString.toLowerCase()) ||
                                row.mEmail.contains(charSequence) || row.mAddress.contains(charSequence) ||
                                row.phoneNo.contains(charSequence) || row.weight.contains(charSequence) ||
                                row.height.contains(charSequence) || dateInString.contains(charSequence)){
                            filteredList.add(row);
                        }
                    }
                    itemListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                itemListFiltered = (ArrayList<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFirstName;
        TextView tvLastName;
        TextView tvEmail;
        TextView tvAddress;
        TextView tvWeight;
        TextView tvHeight;
        TextView tvPhone;
        TextView tvDate;

        ImageButton btnDelete;
        ImageButton btnEdit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstName = itemView.findViewById(R.id.tv_item_first_name);
            tvLastName = itemView.findViewById(R.id.tv_item_last_name);
            tvEmail = itemView.findViewById(R.id.tv_item_email_id);
            tvAddress = itemView.findViewById(R.id.tv_item_address);
            tvWeight = itemView.findViewById(R.id.tv_item_weight);
            tvHeight = itemView.findViewById(R.id.tv_item_height);
            tvPhone = itemView.findViewById(R.id.tv_item_phone_no);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            btnDelete = itemView.findViewById(R.id.btn_item_delete);
            btnEdit = itemView.findViewById(R.id.btn_item_edit);
        }
    }
}

