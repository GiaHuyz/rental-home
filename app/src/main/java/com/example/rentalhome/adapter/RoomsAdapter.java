package com.example.rentalhome.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentalhome.R;
import com.example.rentalhome.dto.Rooms;

import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomViewHolder> {
    private ArrayList<Rooms> roomList;

    public RoomsAdapter(ArrayList<Rooms> roomList) {
        this.roomList = roomList;
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
        holder.tvPrice.setText(String.valueOf(room.getPrice()));
        String imageUrl = room.getImages().get(0);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imgHome);
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public void updateRoomList(ArrayList<Rooms> newRoomList) {
        roomList.clear();
        roomList.addAll(newRoomList);
        notifyDataSetChanged();
    }

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress, tvPrice;
        ImageView imgHome;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgHome = itemView.findViewById(R.id.imgHome);
        }
    }
}
