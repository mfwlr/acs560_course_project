package com.example.maxfowler.regionalhealthmonitor;


import android.content.Context;
import android.widget.BaseAdapter;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;

/**
 * RHMFavAdapter is the list adapter that links the RHMFav to the RHMFavModel
 * It heavily leverages tutorials, as it was one of the better methods for learning
 * the process.
 */
public class RHMFavAdapter extends BaseAdapter implements OnClickListener{

    private Activity act;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    private RHMFavModel temps = null;
    private int i = 0;

    /**
     * This establishes an RHMFavAdapter for a given RHMFav and list of data
     * @param a
     * @param d
     */
    public RHMFavAdapter(Activity a, ArrayList d){
        act = a;
        data = d;

        inflater = (LayoutInflater)act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /**
     * getCount gets the favorite list map size - it defaults to being one "No element exists" choice
     * if no elements exist
     * @return
     */
    public int getCount(){
        if(data.size()<=0){
            return 1;
        }
        return data.size();
    }

    /**
     * getItem get's the position of a chosen object
     * @param position
     * @return
     */
    public Object getItem(int position){
        return position;
    }

    /**
     * getItemId does the same thing as getItem in this case
     * @param position
     * @return
     */
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

    /**
     * This fetches the view for a given list item, using the position and the viewgroup of the list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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

    /**
     * Who needs this method?  I sure don't :D
     * @param v
     */
        public void onClick(View v){
            System.out.println("This one doesn't do anything");
        }

    /**
     * This uses the onItemClick from the RHMFav to set up the chosen favorite for the map
     */
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

