package com.ndjapa_s1803449.earthquake_master;

// Author : Ghislain T Ndjapa
// StudentID: S1803449

import android.util.Log;

import com.ndjapa_s1803449.earthquake_master.models.Earthquake;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static String KEY_LOCATION = "location";
    public static String KEY_MAGNITUDE = "magnitude";
    private static String KEY_DATE = "date";
    private static String KEY_DEPTH = "depth";
    private static String KEY_LATITUDE = "lat";
    private static String KEY_LONGITUDE = "long";
    private static String KEY_link = "link";




    static Map getDetailsFromDescription(String description) {

        HashMap<String,String> map = new HashMap<>();
        Pattern numberPattern = Pattern.compile("\\d+");
        String[] split = description.split(";");
        String dateString = split[0].split("Origin date/time: ")[1].trim();
        map.put(KEY_DATE,dateString);
        String loc = split[1].trim();
        String location = loc.split("Location: ")[1];

        map.put(KEY_LOCATION,location);
        String[] latLong = split[2].split("Lat/long: ")[1].split(",");
        map.put(KEY_LATITUDE,latLong[0]);
        map.put(KEY_LONGITUDE,latLong[1]);
        Matcher matcher = numberPattern.matcher(split[3]);
        String depth = "";
        if (matcher.find()) {
            depth = matcher.group();
            map.put(KEY_DEPTH,depth);
        }

        String magnitude = split[4].split("Magnitude: ")[1];
        map.put(KEY_MAGNITUDE,magnitude);

        Log.d("DETAILS",map.toString());
        return map;
    }

    static Earthquake createEarthQuake(Map<String,String> map) {
        Earthquake earthquake = new Earthquake();
        earthquake.setDateString(map.get(KEY_DATE));
        earthquake.setDepth(Integer.parseInt(Objects.requireNonNull(map.get(KEY_DEPTH))));
        earthquake.setMagnitude(Double.parseDouble(Objects.requireNonNull(map.get(KEY_MAGNITUDE))));
        earthquake.setLocation(map.get(KEY_LOCATION));
        earthquake.setLatitude(Double.parseDouble(Objects.requireNonNull(map.get(KEY_LATITUDE))));
        earthquake.setLongitude(Double.parseDouble(Objects.requireNonNull(map.get(KEY_LONGITUDE))));
        return earthquake;
    }


    static Calendar getTimeStampFromHumanDate(String readableDate) throws ParseException {

        //The last 9 characters include space and HH:mm:ss
        Calendar calendar = Calendar.getInstance();
        String withoutHours = readableDate.substring(0,readableDate.length()-9);
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = df.parse(readableDate);
        calendar.setTime(date);
        return calendar;

    }

}
