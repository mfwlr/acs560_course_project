package com.example.maxfowler.regionalhealthmonitor;


import android.content.Context;
import android.widget.BaseAdapter;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import java.util.ArrayList;
import android.app.Activity;
import android.content.res.Resources;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;



public class RHMFavAdapter extends BaseAdapter implements OnClickListener{

    private Activity act;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    private RHMFavModel temps = null;
    private int i = 0;

    public RHMFavAdapter(Activity a, ArrayList d){
        act = a;
        data = d;

        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount(){
        if(data.size()<=0){
            return 1;
        }
        return data.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    /**
     * Modified and learned from online tutorials, so it is not the most efficient code
     */
    public class RHMListView {
        public TextView countyName;
        public TextView stateName;
        public TextView cancerName;
        public TextView latLon;
    }
        public View getView(int position, View convertView, ViewGroup parent){
            View vi = convertView;
            RHMListView rlv;

            if(convertView == null){
                vi = inflater.inflate(R.layout.rhmfavitem, null);

                rlv = new RHMListView();

                rlv.countyName = (TextView)vi.findViewById(R.id.fct);
                rlv.stateName = (TextView)vi.findViewById(R.id.snt);
                rlv.cancerName = (TextView)vi.findViewById(R.id.cnt);
                rlv.latLon = (TextView)vi.findViewById(R.id.llt);

                vi.setTag(rlv);
            }else{
                rlv = (RHMListView)vi.getTag();
            }

            if(data.size() <=0){
                rlv.countyName.setText("No favorites to display");
            }else{
                temps = (RHMFavModel) data.get(position);

                rlv.countyName.setText(temps.getCountyName());
                rlv.stateName.setText(temps.getStateName());
                rlv.cancerName.setText(temps.getCancerType());
                rlv.latLon.setText(temps.getLat() + "," + temps.getLon());
                vi.setOnClickListener(new OnItemClickListener(position));
            }
            return vi;
        }

        public void onClick(View v){
            System.out.println("This one doesn't do anything");
        }

        private class OnItemClickListener implements OnClickListener{
            private int pos;

            OnItemClickListener(int pos){
                this.pos = pos;
            }

            public void onClick(View sV){
                RHMFav rhmf = (RHMFav)act;

                rhmf.onItemClick(pos);
            }
        }
    }

