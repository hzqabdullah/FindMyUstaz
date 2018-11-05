package com.example.macbook.eventapp.activity.Ustaz;

public class Config {

    public static final String localhost = "www.findmyustaz.com";

    //LOGIN AND SIGNUP MODULE
    public static final String URL_SIGNUP = "http://"+localhost+"/ustaz/ustazregister.php";
    public static final String URL_LOGIN = "http://"+localhost+"/ustaz/ustazlogin.php";
    public static final String URL_DASHBOARD = "http://"+localhost+"/ustaz/dashboard.php?uemail=";
    public static final String URL_USTAZPROFILE = "http://"+localhost+"/ustaz/ustazprofile.php?uid=";
    public static final String URL_PROFILEUPDATE = "http://"+localhost+"/ustaz/ustazupdateprofile.php";
    public static final String URL_EVENTCOUNT = "http://"+localhost+"/ustaz/event/eventCount.php?uid=";
    public static final String URL_TOTALEVENTCOUNT = "http://"+localhost+"/ustaz/event/totaleventCount.php?uid=";
    public static final String URL_EVENTARCHIVECOUNT = "http://"+localhost+"/ustaz/event/eventarchiveCount.php?uid=";
    public static final String URL_PRODUCTCOUNT = "http://"+localhost+"/ustaz/shop/productCount.php?uid=";
    public static final String URL_FOLLOWERCOUNT = "http://"+localhost+"/ustaz/followerCount.php?uid=";
    public static final String URL_JOINUSERTOTAL = "http://"+localhost+"/ustaz/joinuserCount.php?id=";
    public static final String URL_ORDERCOUNT = "http://"+localhost+"/ustaz/order/orderCount.php?uid=";
    public static final String URL_ORDERHISTORYCOUNT = "http://"+localhost+"/ustaz/order/orderhistoryCount.php?uid=";
    public static final String URL_RATECOUNT = "http://"+localhost+"/ustaz/rateCount.php?id=";
    public static final String URL_COMMENTDISPLAY = "http://"+localhost+"/ustaz/displaycomment.php?id=";


    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_JSON_ECOUNT = "ecount"; //event
    public static final String TAG_JSON_TECOUNT = "tecount"; //event
    public static final String TAG_JSON_FCOUNT = "fcount"; //followers
    public static final String TAG_JSON_PCOUNT = "pcount"; //product
    public static final String TAG_JSON_ACOUNT = "acount"; //archive
    public static final String TAG_JSON_JUCOUNT = "jucount"; //join user
    public static final String TAG_JSON_RCOUNT = "rcount"; //rate event display
    public static final String TAG_JSON_OCOUNT = "ocount"; //order

    public static final String TAG_UID = "id";
    public static final String TAG_UFIRSTNAME = "firstname";
    public static final String TAG_ULASTNAME = "lastname";
    public static final String TAG_UPROFILENAME = "profilename";
    public static final String TAG_UICNO = "icno";
    public static final String TAG_UDOB = "dob";
    public static final String TAG_UPLACE = "place";
    public static final String TAG_UCONTACT = "contact";
    public static final String TAG_UEMAIL = "email";
    public static final String TAG_UPHOTO = "photo";
    public static final String TAG_UPASSWORD = "password";

    public static final String TAG_EVENTCOUNT = "eventCount";
    public static final String TAG_TOTALEVENTCOUNT = "totaleventCount";
    public static final String TAG_ARCHIVECOUNT = "archiveCount";
    public static final String TAG_PRODUCTCOUNT = "productCount";
    public static final String TAG_FOLLOWERCOUNT = "followerCount";
    public static final String TAG_JOINUSERCOUNT = "joinuserCount";
    public static final String TAG_RATECOUNT = "rateCount";
    public static final String TAG_ORDERCOUNT = "orderCount";



    public static final String TAG_COMMENT = "comment";
    public static final String TAG_USER = "user";
    public static final String TAG_USERPHOTO = "userphoto";


    //Register

    public static final String UID = "id";
    public static final String UFIRSTNAME = "firstname";
    public static final String ULASTNAME = "lastname";
    public static final String UPROFILENAME = "profilename";
    public static final String UICNO = "icno";
    public static final String UDOB = "dob";
    public static final String UPLACE = "place";
    public static final String UCONTACT = "contact";
    public static final String UEMAIL = "email";
    public static final String UPASSWORD = "password";
    public static final String UPHOTO = "photo";


}
