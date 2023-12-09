package com.example.android.attendance.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.attendance.Models.SubjectsResponse;
import com.example.android.attendance.R;

import java.util.List;

public class SubjectsAdapter extends BaseAdapter {
    Context context;
    private List<SubjectsResponse> dataList;
    LayoutInflater layoutInflater;

    public SubjectsAdapter(Context context, List<SubjectsResponse> dataList) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_student_subjects_list, null);
        }
        TextView userId = (TextView) convertView.findViewById(R.id.user_id);
        TextView subjectId = (TextView) convertView.findViewById(R.id.subject_id);
        TextView subject = (TextView) convertView.findViewById(R.id.subject_selected_text_view);

        userId.setText(Integer.toString(dataList.get(position).getId()));
        subjectId.setText(Integer.toString(dataList.get(position).getSubject_id()));
        subject.setText(dataList.get(position).getSubject_name());
        return convertView;
    }
}
