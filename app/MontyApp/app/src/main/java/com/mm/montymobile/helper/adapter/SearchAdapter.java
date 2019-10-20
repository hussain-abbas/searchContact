package com.mm.montymobile.helper.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mm.montymobile.R;
import com.mm.montymobile.helper.pojo.AllContactsResponse;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends ListAdapter<AllContactsResponse, SearchAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;

    public SearchAdapter() {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<AllContactsResponse> DIFF_CALLBACK = new DiffUtil.ItemCallback<AllContactsResponse>() {
        @Override
        public boolean areItemsTheSame(@NonNull AllContactsResponse oldContact, @NonNull AllContactsResponse newContact) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AllContactsResponse oldContact, @NonNull AllContactsResponse newContact) {
            return false;
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        AllContactsResponse contact = getItem(i);

        holder.name.setText(contact.getName());
        holder.number.setText(contact.getNumber());
        holder.address.setText(contact.getAddress());
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        //Todo Views
        public TextView name;
        public TextView number;
        public TextView address;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            address = itemView.findViewById(R.id.address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.OnItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(AllContactsResponse item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
