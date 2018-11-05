package com.example.macbook.eventapp.activity.Ustaz.Event;

/**
 * Created by Macbook on 1/13/18.
 */

public class EventConfig {

    public static final String localhost = "www.findmyustaz.com";

    public static final String URL_EVENTADD ="http://"+localhost+"/ustaz/event/addevent.php";
    public static final String URL_EVENTDISPLAY = "http://"+localhost+"/ustaz/event/displayevent.php?uid=";
    public static final String URL_EVENTARCHIVE = "http://"+localhost+"/ustaz/event/displayeventarchive.php?uid=";
    public static final String URL_EVENTDETAILS = "http://"+localhost+"/ustaz/event/viewevent.php?uid=&id=";
    public static final String URL_EVENTARCHIVEDETAILS = "http://"+localhost+"/ustaz/event/vieweventarchive.php?id=";
    public static final String URL_EVENTUPDATE = "http://"+localhost+"/ustaz/event/updateevent.php";
    public static final String URL_EVENTDELETE = "http://"+localhost+"/ustaz/event/deleteevent.php?id=";
    public static final String URL_EVENTAUTODELETE = "http://"+localhost+"/ustaz/event/autodeleteevent.php";

    //ADD ON INSERT DATE & UPDATE DATE

    public static final String EID = "id"; //ID
    public static final String ECATEGORY = "category"; //ID
    public static final String ETITLE = "title"; //TITLE
    public static final String EDATE = "date";
    public static final String EDATEEND = "dateend";
    public static final String EDAY = "day";
    public static final String ETIME = "time"; //TIME
    public static final String EVENUE = "venue";
    public static final String EPLACE = "place";
    public static final String ELATITUDE = "latitude";
    public static final String ELONGITUDE = "longitude";
    public static final String EIMAGE = "image";
    public static final String EINSERT = "insert";
    public static final String EUPDATE = "update";
    public static final String UID = "uid"; //Ustaz ID

    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_EID = "id";
    public static final String TAG_ECATEGORY = "category";
    public static final String TAG_ETITLE = "title";
    public static final String TAG_EDATE = "date";
    public static final String TAG_EDATEEND = "dateend";
    public static final String TAG_EDAY = "day";
    public static final String TAG_ETIME = "time";
    public static final String TAG_EVENUE = "venue";
    public static final String TAG_EPLACE = "place";
    public static final String TAG_ELATITUDE = "latitude";
    public static final String TAG_ELONGITUDE = "longitude";
    public static final String TAG_EIMAGE = "image";
    public static final String TAG_EINSERT = "insert";
    public static final String TAG_EUPDATE = "update";

    public static final String E_ID = "e_id";
}
