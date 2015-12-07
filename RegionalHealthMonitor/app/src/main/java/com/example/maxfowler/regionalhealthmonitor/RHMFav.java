package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Button;
import android.view.View;
public class RHMFav extends Activity{

    private ListView lv;
    private int curMark;
    private RHMFavAdapter rfa;

    private RHMFav ref = null;
    private ArrayList<RHMFavModel> models = new ArrayList<RHMFavModel>();

    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmfav);

        ref = this;
        curMark = 0;

        fetchFavs();

        ListView lv =  (ListView) findViewById(R.id.favList);

        rfa = new RHMFavAdapter(ref, models);
        lv.setAdapter(rfa);
    }

    public void fetchFavs(){
        RHMUser u = ((RHMAppData)this.getApplication()).getUser();
        models = RHMDataCenter.fetchFavorites(u.getName(), u.getPass());
    }

    public void onItemClick(int pos){
        RHMFavModel vals = (RHMFavModel) models.get(pos);
        ((RHMAppData)this.getApplication()).setInfoAndMark(vals,curMark);
    }

    public void togglePin(View v) {
        Button b = (Button) this.findViewById(R.id.pinToggle);
        if(curMark == 0){
            curMark =1 ;
            b.setText("Marker: B");
        } else{
            curMark = 0;
            b.setText("Marker: A");
        }

    }
}
