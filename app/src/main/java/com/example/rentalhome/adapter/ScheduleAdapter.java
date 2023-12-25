package com.example.rentalhome.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentalhome.R;
import com.example.rentalhome.dto.Rooms;
import com.example.rentalhome.dto.Schedule;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> scheduleList;
    private OnItemClickListener itemClickListener;
    private boolean isOwner;

    public ScheduleAdapter(List<Schedule> scheduleList, boolean isOwner, OnItemClickListener itemClickListener) {
        this.scheduleList = scheduleList;
        this.isOwner = isOwner;
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Schedule schedule);
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        if(isOwner) {
            holder.btnBook.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        }
        holder.tvId.setText(schedule.getScheduleId());
        holder.tvAddress.setText(schedule.getAddress());
        holder.tvDayOfWeek.setText(schedule.getDayOfWeek());
        holder.tvTime.setText(schedule.getFrom() + " - " + schedule.getTo());
        holder.tvStatus.setText(schedule.getStatus());

        holder.btnBook.setOnClickListener(v -> itemClickListener.onClick(schedule));
        holder.btnCancel.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("notifications").document(schedule.getScheduleId()).delete();
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvAddress, tvDayOfWeek, tvTime, tvStatus;
        Button btnBook, btnCancel;

        ScheduleViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnBook = itemView.findViewById(R.id.btnBook);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }

    public void updateScheduleList(List<Schedule> newSchedules) {
        scheduleList.clear();
        scheduleList.addAll(newSchedules);
        notifyDataSetChanged();
    }
}

