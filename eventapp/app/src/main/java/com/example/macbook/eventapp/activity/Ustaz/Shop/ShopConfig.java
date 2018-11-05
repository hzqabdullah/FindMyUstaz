package com.example.macbook.eventapp.activity.Ustaz.Shop;

/**
 * Created by Haziq on 31/1/2018.
 */

public class ShopConfig
{
    public static final String localhost = "www.findmyustaz.com";

    public static final String URL_PRODUCTADD ="http://"+localhost+"/ustaz/shop/addproduct.php";
    public static final String URL_PRODUCTDISPLAY = "http://"+localhost+"/ustaz/shop/displayproduct.php?uid=";
    public static final String URL_PRODUCTDETAILS = "http://"+localhost+"/ustaz/shop/viewproduct.php?uid=&id=";
    public static final String URL_PRODUCTUPDATE = "http://"+localhost+"/ustaz/shop/updateproduct.php";
    public static final String URL_PRODUCTDELETE = "http://"+localhost+"/ustaz/shop/deleteproduct.php?id=";


    public static final String PID = "id"; //ID
    public static final String PNAME = "name"; //ID
    public static final String PPRICE = "price"; //TITLE
    public static final String PDESC = "desc";
    public static final String PQUANTITY = "quantity";
    public static final String PSTATUS = "status"; //TIME
    public static final String PIMAGE = "image";
    public static final String PINSERT = "insert";
    public static final String PUPDATE = "update";
    public static final String UID = "uid"; //Ustaz ID

    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_PID = "id";
    public static final String TAG_PNAME = "name";
    public static final String TAG_PPRICE = "price";
    public static final String TAG_PDESC = "desc";
    public static final String TAG_PQUANTITY = "quantity";
    public static final String TAG_PSTATUS = "status";
    public static final String TAG_PINSERT = "insert";
    public static final String TAG_PUPDATE = "update";
    public static final String TAG_PIMAGE = "image";


    public static final String P_ID = "p_id";
}
