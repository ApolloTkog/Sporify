package com.example.sporify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class MainScreen extends AppCompatActivity {

    //private lateinit var adapter:ArrayAdapter<*>

    ListView listView;
    String[] songNamesTemp = {"LivingOnAPrairie","WalkLikeAPedestrian","22Guns","OGnwstosX"};

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        listView = findViewById(R.id.searchListView);

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,songNamesTemp);
        listView.setAdapter(arrayAdapter);

        //adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.arrat.songs_array))
        //searchListView.adapter = adapter
        //searchListView.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, positiobm id ->


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

            getMenuInflater().inflate(R.menu.search_bar,menu);

            MenuItem menuItem = menu.findItem(R.id.search_bar);
            SearchView songSearch = (SearchView) menuItem.getActionView();
            songSearch.setQueryHint("Search song.");

            songSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query){
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText){

                    arrayAdapter.getFilter().filter(newText);

                    return false;
                }
            });

            return super.onCreateOptionsMenu(menu);
        }

}