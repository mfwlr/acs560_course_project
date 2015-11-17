package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ArrayAdapter;
/**
 * Created by maxfowler on 11/5/15.
 */
public class RHMFav extends Activity{

    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmfav);

        String[] placehold = {"Sample 1", "My favorite", "Fake data", "Whee", "Not whee", "Purple"};

        RHMFavModel fm = new RHMFavModel(null, placehold);

      ExpandableListView lv =  (ExpandableListView) findViewById(R.id.favList);

        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, fm.getList()));
    }
}
