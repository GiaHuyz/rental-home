package com.example.rentalhome.presenter;

import com.example.rentalhome.contract.ScheduleContract;
import com.example.rentalhome.dto.Schedule;
import com.example.rentalhome.model.ScheduleModel;

import java.util.List;

public class SchedulePresenter implements ScheduleContract.Presenter {

    private ScheduleContract.View view;
    private ScheduleContract.Model model;

    public SchedulePresenter(ScheduleContract.View view) {
        this.view = view;
        this.model = new ScheduleModel();
    }

    @Override
    public void addSchedule(Schedule schedule) {
        model.createSchedule(schedule, new ScheduleContract.Model.OnScheduleListener() {
            @Override
            public void onSuccess(String message) {
                view.onScheduleSuccess(message);
            }

            @Override
            public void onFailure(String message) {
                view.onScheduleFailure(message);
            }
        });
    }

    @Override
    public void getSchedules(String roomId) {
        model.getSchedules(roomId, new ScheduleContract.Model.OnLoadSchedulesListener() {
            @Override
            public void onSuccess(List<Schedule> scheduleList) {
                view.display(scheduleList);
            }

            @Override
            public void onFailure(String message) {
                view.onScheduleFailure(message);
            }
        });
    }
}

