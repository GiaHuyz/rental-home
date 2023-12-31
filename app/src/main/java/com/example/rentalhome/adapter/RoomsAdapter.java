package com.example.rentalhome.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentalhome.R;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.screens.DetailRoomActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    private ArrayList<Rooms> roomList;
    private OnItemClickListener itemClickListener;

    public RoomsAdapter(ArrayList<Rooms> roomList, OnItemClickListener itemClickListener) {
        this.roomList = roomList;
        this.itemClickListener = itemClickListener;
    }

    public void removeAt(int position) {
        roomList.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Rooms room);
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Rooms room = roomList.get(position);
        holder.tvAddress.setText(room.getAddress());
        holder.tvPrice.setText("Price: " + NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("vn").setRegion("VN").build()).format(room.getPrice()));
        holder.tvArea.setText("Area: " + room.getArea() + " m\u00B2");
        String imageUrl = room.getImages().get(0);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.imgHome);
        }

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(room);
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public Rooms getItem(int position) {
        return roomList.get(position);
    }
    public void updateRoomList(ArrayList<Rooms> newRoomList) {
        roomList.clear();
        roomList.addAll(newRoomList);
        notifyDataSetChanged();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress, tvPrice, tvArea;
        ImageView imgHome;
        CardView recCard;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvArea = itemView.findViewById(R.id.tvArea);
            imgHome = itemView.findViewById(R.id.imgHome);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
