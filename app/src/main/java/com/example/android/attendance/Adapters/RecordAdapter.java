package com.example.android.attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.attendance.Models.RecordResponse;
import com.example.android.attendance.R;

import java.util.List;

public class RecordAdapter extends BaseAdapter {
    Context context;
    private List<RecordResponse> dataList;
    LayoutInflater layoutInflater;

    public RecordAdapter(Context context, List<RecordResponse> dataList) {
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
            convertView = layoutInflater.inflate(R.layout.attendance_list, null);
        }
        TextView studentId = (TextView) convertView.findViewById(R.id.studid);
        TextView studentName = (TextView) convertView.findViewById(R.id.studname);
        TextView attStatus = (TextView) convertView.findViewById(R.id.attstatus);

        studentId.setText(Integer.toString(dataList.get(position).getStudent_id()));
        studentName.setText(dataList.get(position).getStudent_name());
        attStatus.setText(dataList.get(position).getAtt_status());

        return convertView;
    }
}
