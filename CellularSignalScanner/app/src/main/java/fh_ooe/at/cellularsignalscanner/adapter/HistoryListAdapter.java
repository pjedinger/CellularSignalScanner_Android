package fh_ooe.at.cellularsignalscanner.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

import fh_ooe.at.cellularsignalscanner.R;
import fh_ooe.at.cellularsignalscanner.activities.HistoryActivity;
import fh_ooe.at.cellularsignalscanner.data.HistoryEntry;

public class HistoryListAdapter extends ArrayAdapter<HistoryEntry> implements View.OnClickListener{

        private ArrayList<HistoryEntry> dataSet;
        Context mContext;

        private static class ViewHolder {
            TextView nameTextView;
            TextView scanDurationTextView;
            TextView datelocationTextView;
        }

        public HistoryListAdapter(ArrayList<HistoryEntry> data, Context context) {
            super(context, R.layout.history_cell, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public void onClick(View v) {
            ViewHolder viewHolder = (ViewHolder) v.getTag();
            int position=(Integer) viewHolder.nameTextView.getTag();
            HistoryEntry historyEntry = dataSet.get(position);

            HistoryActivity historyActivity = (HistoryActivity) mContext;
            historyActivity.historyEntrySelected(historyEntry);


        }

        private int lastPosition = -1;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HistoryEntry historyEntry = getItem(position);
            ViewHolder viewHolder;

            final View result;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.history_cell, parent, false);
                convertView.setOnClickListener(this);
                viewHolder.nameTextView = convertView.findViewById(R.id.history_cell_name_textview);
                viewHolder.datelocationTextView = convertView.findViewById(R.id.history_cell_datelocation_textview);
                viewHolder.scanDurationTextView =  convertView.findViewById(R.id.history_cell_duration_textview);
                result=convertView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            lastPosition = position;
            viewHolder.nameTextView.setText(historyEntry.getName());
            viewHolder.nameTextView.setTag(position);
            viewHolder.scanDurationTextView.setText(historyEntry.getScanDuration() + " seconds");
            viewHolder.datelocationTextView.setText(historyEntry.getDate() + ", "+ historyEntry.getLocation());
            return convertView;
        }
}
