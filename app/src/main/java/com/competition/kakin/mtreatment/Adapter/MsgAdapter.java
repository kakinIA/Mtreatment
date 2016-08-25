package com.competition.kakin.mtreatment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.competition.kakin.mtreatment.MsgContent;
import com.competition.kakin.mtreatment.R;

import java.util.List;

/**
 * Created by kakin on 2016/8/21.
 */
public class MsgAdapter extends ArrayAdapter<MsgContent> {
    private int resId;
    public MsgAdapter(Context context, int resource, List<MsgContent> msgContent) {
        super(context, resource, msgContent);
        resId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgContent msgContent = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resId, null);
            viewHolder = new ViewHolder();
            viewHolder.doctorName = (TextView) view.findViewById(R.id.msglist_doctorName);
            viewHolder.msgDetail = (TextView) view.findViewById(R.id.msglist_detail);
            viewHolder.msgMonth = (TextView) view.findViewById(R.id.msglist_month);
            viewHolder.msgDay = (TextView) view.findViewById(R.id.msglist_day);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.doctorName.setText(msgContent.getDoctorName());
        viewHolder.msgDetail.setText(msgContent.getMsgDetail());
        viewHolder.msgDay.setText(msgContent.getMsgTime().get("dayOfMonth") + "");
        viewHolder.msgMonth.setText(msgContent.getMsgTime().get("monthOfYear") + "");
        return view;
    }

    class ViewHolder{
        TextView doctorName;
        TextView msgDetail;
        TextView msgMonth;
        TextView msgDay;
    }
}
