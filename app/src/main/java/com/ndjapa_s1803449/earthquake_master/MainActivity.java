package com.ndjapa_s1803449.earthquake_master;

// Author : Ghislain T Ndjapa
// StudentID: S1803449

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ndjapa_s1803449.earthquake_master.adapters.EarthQuakesAdapter;
import com.ndjapa_s1803449.earthquake_master.models.Earthquake;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    static final String urlString = "https://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";
    String rssString = "";

    static final String KEY_item = "item";
    static final String KEY_category = "category";
    static final String KEY_title = "title";
    static final String KEY_Description = "description";
    static ProgressDialog progressDialog;
    static final String KEY_link = "link";
    RecyclerView recyclerView;
    static SwipeRefreshLayout refreshLayout;
    private static EarthQuakesAdapter earthQuakesAdapter;
    static ArrayList<Earthquake> earthquakeList = new ArrayList<>();
    private final String LIST_KEY = "earthquakes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading earthquake data..");
        if (savedInstanceState == null) {
            new EarthQuakesTask().execute();
        }

        recyclerView = findViewById(R.id.earthQuakesRecyclerview);
        refreshLayout = findViewById(R.id.swipeToRefresh);
        refreshLayout.setOnRefreshListener(this);
        earthQuakesAdapter = new EarthQuakesAdapter(new ArrayList<Earthquake>(), new EarthQuakesAdapter.OnEarthQuakeClickListener() {
            @Override
            public void onEarthQuakeClicked(Earthquake earthquake) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Log.d("TAG--OnC",earthquake.toString());
                intent.putExtra("data", earthquake);
                intent.putExtra("link",earthquake.getLink());
                startActivity(intent);

            }
        });

        recyclerView.setAdapter(earthQuakesAdapter);

    }

    @Override
    public void onRefresh() {
        new EarthQuakesTask().execute();
    }


    static class EarthQuakesTask extends AsyncTask<Void, Void, ArrayList<Earthquake>> {

        @Override
        protected ArrayList<Earthquake> doInBackground(Void... voids) {

            ArrayList<Earthquake> earthquakeArrayList = new ArrayList<>();
            ParsingData parser = new ParsingData();
            //String data = parser.FetchRSS(urlString);
            //Document doc = parser.getDomElemt(data);
            DocumentBuilderFactory dBf = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder db = dBf.newDocumentBuilder();
                // StringReader sr = new StringReader(rssString);
                // InputSource is = new InputSource(sr);
                //is.setCharacterStream(new StringReader(rssString));
                Document doc = db.parse(urlString);

                NodeList n1 = doc.getElementsByTagName(KEY_item);
                //Looping through each key tag
                for (int i = 0; i < n1.getLength(); i++) {
                    HashMap<String, String> map = new HashMap<>();
                    Element e = (Element) n1.item(i);
                    // adding elements to the map

                    map.put(KEY_title, parser.getValue(e, KEY_title));
                    map.put(KEY_Description, parser.getValue(e, KEY_Description));
                    String link = parser.getValue(e, KEY_link);

                    //Log.d("TAG", link);
                    Map myMap = Utils.getDetailsFromDescription(parser.getValue(e, KEY_Description));
                    Earthquake earthquake = Utils.createEarthQuake(myMap);
                    Utils.getTimeStampFromHumanDate(earthquake.getDateString());
                    earthquake.setLink(link);
                    //Log.d("TAG", earthquake.toString());
                    earthquakeArrayList.add(earthquake);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            earthquakeList = earthquakeArrayList;
            Log.d("TAG--",earthquakeArrayList.toString());
            Log.d("TAG--","-------------------------------");
            Log.d("TAG--",earthquakeList.toString());
            return earthquakeList;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Earthquake> earthquakes) {
            super.onPostExecute(earthquakes);
            Log.d("TAG--- ONPOST", earthquakes.toString());

            progressDialog.dismiss();
            refreshLayout.setRefreshing(false);
            earthquakeList = earthquakes;
            earthQuakesAdapter.setEarthquakes(earthquakes);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_KEY, earthquakeList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filterList) {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar newdate = Calendar.getInstance();
                    newdate.set(year, month, dayOfMonth);

                    try {
                        filterList(newdate);
                    } catch (ParseException e) {
                        Log.d("Error", "Error parsing date");
                        e.printStackTrace();
                    }
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            pickerDialog.show();
            return true;
        }

        if (item.getItemId() == R.id.swipeToRefresh){
            refreshLayout.setRefreshing(true);
            new EarthQuakesTask().execute();
        }


        return true;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        earthquakeList = savedInstanceState.getParcelableArrayList(LIST_KEY);
        earthQuakesAdapter.setEarthquakes(earthquakeList);
    }

    private void filterList(Calendar calendarToCompare) throws ParseException {
        progressDialog.show();
        int dayToCompare = calendarToCompare.get(Calendar.DAY_OF_MONTH);
        int monthToCompare = calendarToCompare.get(Calendar.MONTH);
        int yearToCompare = calendarToCompare.get(Calendar.YEAR);

        Log.d("TOCompare", yearToCompare + " "+ monthToCompare + " " + dayToCompare );

        ArrayList<Earthquake> filteredList = new ArrayList<>();
        if (earthquakeList != null || !earthquakeList.isEmpty()) {

            for (Earthquake earthquake : earthquakeList) {

                Calendar dayOfEarthQuake = Utils.getTimeStampFromHumanDate(earthquake.getDateString());
                int day = dayOfEarthQuake.get(Calendar.DAY_OF_MONTH);
                int month = dayOfEarthQuake.get(Calendar.MONTH);
                int year = dayOfEarthQuake.get(Calendar.YEAR);
                Log.d("TOCompare--",year+ " "+ month+ " "+ day);
                if (dayToCompare == day && monthToCompare == month && yearToCompare == year) {
                    filteredList.add(earthquake);
                }
            }
        }

        earthQuakesAdapter.setEarthquakes(filteredList);
        progressDialog.dismiss();
    }
}
