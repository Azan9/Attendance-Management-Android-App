package com.example.android.attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.attendance.Models.ScheduleResponse;
import com.example.android.attendance.R;

import java.util.List;

public class ScheduleAdapter extends BaseAdapter {
    Context context;
    private List<ScheduleResponse> dataList;
    LayoutInflater layoutInflater;

    public ScheduleAdapter(Context context, List<ScheduleResponse> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_schedule_list, null);
        }

        TextView time = (TextView) convertView.findViewById(R.id.time_text_view);
        TextView subject = (TextView) convertView.findViewById(R.id.subject_text_view);
        TextView classGroup = (TextView) convertView.findViewById(R.id.class_group);
        TextView classroom = (TextView) convertView.findViewById(R.id.classroom_text_view);
        TextView day = (TextView) convertView.findViewById(R.id.day_text_view);

        time.setText(dataList.get(position).getStart_time());
        subject.setText(dataList.get(position).getSubject_name());
        classGroup.setText(dataList.get(position).getClass_name());
        classroom.setText(dataList.get(position).getClass_venue());
        day.setText(dataList.get(position).getDay());

        return convertView;
    }
}
