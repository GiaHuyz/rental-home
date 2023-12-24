package com.example.rentalhome.contract;

import com.example.rentalhome.dto.Schedule;

import java.util.List;

public interface ScheduleContract {
    interface View {
        void onScheduleSuccess(String message);
        void onScheduleFailure(String message);
        void display(List<Schedule> scheduleList);
    }

    interface Presenter {
        void addSchedule(Schedule schedule);
        void getSchedules(String roomId);
    }

    interface Model {
        void createSchedule(Schedule schedule, OnScheduleListener listener);
        void getSchedules(String roomId, OnLoadSchedulesListener listener);

        interface OnScheduleListener {
            void onSuccess(String message);
            void onFailure(String message);
        }

        interface OnLoadSchedulesListener {
            void onSuccess(List<Schedule> scheduleList);

            void onFailure(String message);
        }
    }
}

