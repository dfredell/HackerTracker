package com.shortstack.hackertracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shortstack.hackertracker.Activity.DetailsActivity;
import com.shortstack.hackertracker.Common.Constants;
import com.shortstack.hackertracker.Model.Default;
import com.shortstack.hackertracker.R;
import com.shortstack.hackertracker.Utils.SharedPreferencesUtil;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Whitney Champion
 * Date: 7/11/13
 * Time: 9:21 AM
 * Description:
 */
public class DefaultAdapter extends ArrayAdapter<Default> {

    Context context;
    int layoutResourceId;
    List<Default> data;

    public DefaultAdapter(Context context, int layoutResourceId, List<Default> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DefaultHolder holder;
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DefaultHolder();
            holder.title = (TextView) row.findViewById(R.id.title);
            holder.time = (TextView) row.findViewById(R.id.time);
            holder.name = (TextView) row.findViewById(R.id.name);
            holder.name.setVisibility(View.GONE);
            holder.demo = (ImageView) row.findViewById(R.id.demo);
            holder.demo.setVisibility(View.GONE);
            holder.exploit = (ImageView) row.findViewById(R.id.exploit);
            holder.exploit.setVisibility(View.GONE);
            holder.tool = (ImageView) row.findViewById(R.id.tool);
            holder.tool.setVisibility(View.GONE);
            holder.icons = (LinearLayout) row.findViewById(R.id.icons);
            holder.icons.setVisibility(View.GONE);
            holder.is_new = (TextView) row.findViewById(R.id.isNew);
            holder.is_new.setVisibility(View.GONE);
            holder.where = (TextView) row.findViewById(R.id.where);
            holder.defaultLayout = (LinearLayout) row.findViewById(R.id.rootLayout);
            row.setTag(holder);

        } else {
            holder = (DefaultHolder) row.getTag();
        }

        final Default item = data.get(position);

        // if items in list, populate data
        if (item.getTitle() != null) {

            // set title
            holder.title.setText(item.getTitle());

            // if starred, highlight title
            if (item.getStarred() == 1) {
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            } else {
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

            // set name if it's a speaker, demo lab, skytalk, party, or village talk
            if (!Arrays.asList(Constants.TYPE_SPEAKER, Constants.TYPE_DEMOLAB, Constants.TYPE_SKYTALKS, Constants.TYPE_VILLAGE, Constants.TYPE_PARTY, Constants.TYPE_OMG).contains(item.getType()) || item.getName() == null) {
                holder.name.setVisibility(View.GONE);
            } else if (item.getName().equals("")) {
                holder.name.setVisibility(View.GONE);
            } else {
                holder.name.setVisibility(View.VISIBLE);
                holder.name.setText(item.getName());
            }

            // set speaker type
            if (item.getType().equals(Constants.TYPE_SPEAKER)) {

                if (item.getTool() == 1) {
                    holder.icons.setVisibility(View.VISIBLE);
                    holder.tool.setVisibility(View.VISIBLE);
                } else {
                    holder.tool.setVisibility(View.GONE);
                }
                if (item.getExploit() == 1) {
                    holder.icons.setVisibility(View.VISIBLE);
                    holder.exploit.setVisibility(View.VISIBLE);
                } else {
                    holder.exploit.setVisibility(View.GONE);
                }
                if (item.getDemo() == 1) {
                    holder.icons.setVisibility(View.VISIBLE);
                    holder.demo.setVisibility(View.VISIBLE);
                } else {
                    holder.demo.setVisibility(View.GONE);
                }

                if (item.getTool() == 0 && item.getExploit() == 0 && item.getDemo() == 0)
                    holder.icons.setVisibility(View.GONE);

            } else {

                holder.icons.setVisibility(View.GONE);
                holder.tool.setVisibility(View.GONE);
                holder.exploit.setVisibility(View.GONE);
                holder.demo.setVisibility(View.GONE);

            }

            // set time
            if (!item.getBegin().equals("")) {
                String time = "";

                if (SharedPreferencesUtil.shouldShowMilitaryTime()) {
                    time = item.getBegin();

                } else {
                    String dateStr = item.getBegin();
                    DateFormat readFormat = new SimpleDateFormat("HH:mm");
                    DateFormat writeFormat = new SimpleDateFormat("h:mm aa");
                    Date date = null;
                    try {
                        date = readFormat.parse(dateStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null) {
                        time = writeFormat.format(date);
                    }
                }

                holder.time.setText(time);

            } else {
                holder.time.setText(context.getResources().getString(R.string.tba));
            }

            // set location
            holder.where.setText(item.getLocation());

            // if new, show "new"
            holder.is_new.setVisibility(View.GONE);
            if (item.isNew() != null) {
                if (item.isNew() == 1)
                    holder.is_new.setVisibility(View.VISIBLE);
            } else {
                holder.is_new.setVisibility(View.GONE);
            }

            // set onclicklistener for viewing event
            holder.defaultLayout.setOnClickListener(new detailsOnClickListener(item));

        }


        return row;
    }

    public class detailsOnClickListener implements View.OnClickListener {

        private Default item;

        public detailsOnClickListener(Default item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            startDetailsActivity();
        }

        private void startDetailsActivity() {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("item", Parcels.wrap(item));
            getContext().startActivity(intent);
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    static class DefaultHolder {
        TextView title;
        TextView time;
        TextView name;
        TextView where;
        TextView is_new;
        ImageView demo;
        ImageView tool;
        ImageView exploit;
        LinearLayout icons;
        LinearLayout defaultLayout;
    }
}


