package com.example.maxfowler.regionalhealthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

public class RHMFavModel extends ArrayAdapter<String> {
    private final Context context;
    private String[] favorites;

    public RHMFavModel(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.favorites = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      /*  LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position]);

        return rowView;*/

        return null;
    }

    public ArrayList<String> getList(){
        return new ArrayList<String>(Arrays.asList(favorites));
    }
}
