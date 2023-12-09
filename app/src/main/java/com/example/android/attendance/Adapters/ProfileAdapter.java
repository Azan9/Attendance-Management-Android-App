package com.example.android.attendance.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.attendance.Models.ProfileResponse;
import com.example.android.attendance.R;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {
    Context context;
    private List<ProfileResponse> dataList;
    LayoutInflater layoutInflater;


    public ProfileAdapter(Context context, List<ProfileResponse> dataList) {
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
            convertView = layoutInflater.inflate(R.layout.fragment_profile, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.edit_full_name);
        TextView email = (TextView) convertView.findViewById(R.id.edit_email_text);
        TextView phone_number = (TextView) convertView.findViewById(R.id.edit_phone_text);
        TextView gender = (TextView) convertView.findViewById(R.id.edit_gender_text);
        TextView birthdate = (TextView) convertView.findViewById(R.id.edit_birthday_text);
        ImageView profileImage = (ImageView) convertView.findViewById(R.id.user_image);

        name.setText(dataList.get(position).getFull_name());
        email.setText(dataList.get(position).getEmail());
        phone_number.setText(dataList.get(position).getPhone_number());
        gender.setText(dataList.get(position).getGender());
        birthdate.setText(dataList.get(position).getBirthdate());

        return convertView;
    }
}
