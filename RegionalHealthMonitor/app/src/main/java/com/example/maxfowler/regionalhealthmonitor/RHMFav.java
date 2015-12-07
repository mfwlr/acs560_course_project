package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
/**
 * Created by maxfowler on 11/5/15.
 */
public class RHMFav extends Activity{

    private ListView lv;
    private RHMFavAdapter rfa;

    private RHMFav ref = null;
    private ArrayList<RHMFavModel> models = new ArrayList<RHMFavModel>();

    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmfav);

        ref = this;

        fetchFavs();

        ListView lv =  (ListView) findViewById(R.id.favList);

        rfa = new RHMFavAdapter(ref, models);
        lv.setAdapter(rfa);
    }

    public void fetchFavs(){
        RHMUser u = ((RHMAppData)this.getApplication()).getUser();
        models = RHMDataCenter.fetchFavorites(u.getName(), u.getPass());
    }
}
