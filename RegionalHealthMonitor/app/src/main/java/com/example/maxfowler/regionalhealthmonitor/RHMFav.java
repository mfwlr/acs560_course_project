package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ExpandableListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * Created by maxfowler on 11/5/15.
 */
public class RHMFav extends Activity{

    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmfav);

        String[] placehold = {"Sample 1", "My favorite", "Fake data", "Whee", "Not whee", "Purple"};

       // RHMFavModel fm = new RHMFavModel(getBaseContext(), placehold);

        //Change to Expandable list when not using canned data
        ListView lv =  (ListView) findViewById(R.id.favList);

        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, placehold));
    }
}
