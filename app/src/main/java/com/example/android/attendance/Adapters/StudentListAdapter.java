package com.example.android.attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.attendance.Models.StudentListResponse;
import com.example.android.attendance.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    Context context;
    private List<StudentListResponse> dataList;
    LayoutInflater layoutInflater;

    public StudentListAdapter(Context context, List<StudentListResponse> dataList) {
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
            convertView = layoutInflater.inflate(R.layout.students_list, null);
        }
        TextView studentId = (TextView) convertView.findViewById(R.id.student_id);
        TextView studentName = (TextView) convertView.findViewById(R.id.student_name);
        ShapeableImageView studentImage = (ShapeableImageView) convertView.findViewById(R.id.student_image);

        studentId.setText(Integer.toString(dataList.get(position).getStudent_id()));
        studentName.setText(dataList.get(position).getStudent_name());
        String studentImageURL = dataList.get(position).getProfile_image();
        Picasso.get().load("http://192.168.1.01:8000" + studentImageURL).into(studentImage);

        return convertView;
    }
}
