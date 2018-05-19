package com.example.sabina.newsfeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class NewsFeedAdapter extends ArrayAdapter<News> {

    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public NewsFeedAdapter(@NonNull Context context, int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.sectionText = convertView.findViewById(R.id.item_section_name);
            holder.titleText = convertView.findViewById(R.id.item_title);
            holder.timeText = convertView.findViewById(R.id.item_time);
            holder.dateText = convertView.findViewById(R.id.item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News currentNews = getItem(position);
        Date date = null;
        try {
            date = format.parse(currentNews.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.sectionText.setText(currentNews.getSection());
        holder.titleText.setText(currentNews.getTitle());
        holder.timeText.setText(timeFormat.format(date));
        holder.dateText.setText(dateFormat.format(date));

        return convertView;
    }

    static class ViewHolder {
        TextView sectionText;
        TextView titleText;
        TextView timeText;
        TextView dateText;
    }
}
