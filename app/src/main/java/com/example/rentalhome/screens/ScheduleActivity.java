package com.example.rentalhome.screens;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rentalhome.R;
import com.example.rentalhome.adapter.ScheduleAdapter;
import com.example.rentalhome.contract.NotificationContract;
import com.example.rentalhome.contract.ScheduleContract;
import com.example.rentalhome.databinding.ActivityAppointmentScheduleBinding;
import com.example.rentalhome.dto.Notification;
import com.example.rentalhome.dto.Schedule;
import com.example.rentalhome.presenter.NotificationPresenter;
import com.example.rentalhome.presenter.SchedulePresenter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity implements ScheduleContract.View, NotificationContract.View {
    private ActivityAppointmentScheduleBinding binding;
    private SchedulePresenter presenter;
    private NotificationPresenter notificationPresenter;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        boolean isOwner = bundle.getBoolean("isOwner");
        String ownerId = bundle.getString("ownerId");

        if(!isOwner) {
            binding.addLayout.setVisibility(View.GONE);
        }

        String roomId = bundle.getString("roomId");
        String address = bundle.getString("address");
        String userName = bundle.getString("userName");

        binding.tvTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(true);
            }
        });

        binding.tvTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(false);
            }
        });

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerDayOfWeek.setAdapter(arrayAdapter);

        presenter = new SchedulePresenter(this);

        binding.btnScheduleAppointment.setOnClickListener(v -> {
            String dayOfWeek = binding.spinnerDayOfWeek.getSelectedItem().toString();
            String timeFrom = binding.tvTimeFrom.getText().toString();
            String timeTo = binding.tvTimeTo.getText().toString();

            Schedule schedule = new Schedule(roomId, address, dayOfWeek,timeFrom, timeTo);

            presenter.addSchedule(schedule);
        });

        binding.rvSchedule.setLayoutManager(new LinearLayoutManager(this));
        presenter.getSchedules(roomId);

        adapter = new ScheduleAdapter(new ArrayList<>(), isOwner, new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onClick(Schedule schedule) {
                if(schedule.getStatus().equals("booked")) {
                    Toast.makeText(ScheduleActivity.this, "Đã có người đặt", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("rooms").document(roomId).update("status", "booked");
                    notificationPresenter = new NotificationPresenter(ScheduleActivity.this);
                    notificationPresenter.sendNotification(new Notification(ownerId,
                            String.format("%s đã đặt lịch hẹn %s, %s - %s cho trọ %s", userName,
                                    schedule.getDayOfWeek(), schedule.getFrom(), schedule.getTo(), address)));
                    notificationPresenter.sendNotification(new Notification(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.format("Bạn đã đặt lịch hẹn %s, %s - %s cho trọ %s", userName,
                                    schedule.getDayOfWeek(), schedule.getFrom(), schedule.getTo(), address)));
                }
            }
        });

        binding.rvSchedule.setAdapter(adapter);
    }

    private void showTimePickerDialog(final boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                if (isStartTime) {
                    binding.tvTimeFrom.setText(time);
                } else {
                    binding.tvTimeTo.setText(time);
                }
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    @Override
    public void onScheduleSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScheduleFailure(String message) {
        Log.d("ScheduleActivity", message);
    }

    @Override
    public void display(List<Schedule> scheduleList) {
        adapter.updateScheduleList(scheduleList);
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadNotification(List<Notification> notificationList) {

    }
}
