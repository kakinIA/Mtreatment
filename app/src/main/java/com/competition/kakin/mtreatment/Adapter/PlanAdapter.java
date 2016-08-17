package com.competition.kakin.mtreatment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.competition.kakin.mtreatment.PlanContent;
import com.competition.kakin.mtreatment.R;

import java.util.List;

/**
 * Created by kakin on 2016/8/8.
 */
public class PlanAdapter extends ArrayAdapter<PlanContent> {
    private int resId;
    public PlanAdapter(Context context, int resource, List<PlanContent> planContents) {
        super(context, resource, planContents);
        resId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlanContent planContent = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resId, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView) view.findViewById(R.id.plan_img);
            viewHolder.title = (TextView) view.findViewById(R.id.plan_title);
            viewHolder.period = (TextView) view.findViewById(R.id.plan_period);
            viewHolder.introduce = (TextView) view.findViewById(R.id.plan_introduce);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.img.setImageResource(planContent.getImgId());
        viewHolder.title.setText(planContent.getPlanTitle());
        viewHolder.period.setText(planContent.getPeriod());
        viewHolder.introduce.setText(planContent.getIntroduce());
        return view;
    }

    class ViewHolder{
        ImageView img;
        TextView title;
        TextView period;
        TextView introduce;
    }
}
