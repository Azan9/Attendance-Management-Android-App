package com.example.android.attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android.attendance.Models.SubjectsResponse;
import com.example.android.attendance.R;

import java.util.ArrayList;
import java.util.List;

public class SubAdapter extends BaseAdapter{
    Context context;
    private List<SubjectsResponse> dataList;
    LayoutInflater layoutInflater;
    private ArrayList<Object> suggestions = new ArrayList<>();
    public SubAdapter(Context context, List<SubjectsResponse> dataList) {
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
            convertView = layoutInflater.inflate(R.layout.class_subjects_list, null);
        }
//        TextView subjectId = (TextView) convertView.findViewById(R.id.s_id);
        TextView subject = (TextView) convertView.findViewById(R.id.sub_selected_text_view);

//        subjectId.setText(Integer.toString(dataList.get(position).getSubject_id()));
        subject.setText(dataList.get(position).getSubject_name());
        return convertView;
    }

}
