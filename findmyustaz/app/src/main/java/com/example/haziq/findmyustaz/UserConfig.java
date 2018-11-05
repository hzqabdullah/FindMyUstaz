package com.example.haziq.findmyustaz;

/**
 * Created by Haziq on 22/2/2018.
 */

public class UserConfig
{
    //IP ADDRESS & PORT
    public static final String localhost = "www.findmyustaz.com";

    public static final String PAYPAL_CLIENTID = "AbvwOfdpeSOuA00gr9PY5PjcamJFfm5e_M9KMqB6yqmZbD3NB5vZxBEY_3Un6v5xUy2M3FlWLIFfzjLm";


        //----------------------------------------------------------------------------------------------------------------------------
        //USER INFORMATION URL
        public static final String URL_SIGNUP = "http://"+localhost+"/society/register.php";
        public static final String URL_LOGIN = "http://"+localhost+"/society/login.php";
        public static final String URL_USERINFO = "http://"+localhost+"/society/userinfo.php?email=";
        public static final String URL_USERUPDATE = "http://"+localhost+"/society/userupdate.php";

        //----------------------------------------------------------------------------------------------------------------------------
        //DASHBOARD PAGE URL
        public static final String URL_DISPLAYEVENT = "http://"+localhost+"/society/displayevent.php";

        //----------------------------------------------------------------------------------------------------------------------------
        //USTAZ URL
        public static final String URL_USTAZPROFILE = "http://"+localhost+"/society/ustazprofile.php?uid=";

        //----------------------------------------------------------------------------------------------------------------------------
        //SHOP URL
        public static final String URL_USTAZSHOP = "http://"+localhost+"/society/ustazshop.php?uid=";
        public static final String URL_DETAILSPRODUCT = "http://"+localhost+"/society/detailsproduct.php?id=";
        public static final String URL_USTAZSHOPCOUNT = "http://"+localhost+"/society/ustazshopCount.php?uid=";

        //----------------------------------------------------------------------------------------------------------------------------
        //EVENT URL
        public static final String URL_USTAZEVENT = "http://"+localhost+"/society/ustazevent.php?uid=";
        public static final String URL_DETAILSEVENT = "http://"+localhost+"/society/detailsevent.php?id=";
        public static final String URL_USTAZEVENTCOUNT = "http://"+localhost+"/society/ustazeventCount.php?uid=";


        //----------------------------------------------------------------------------------------------------------------------------
        //SEARCH PAGE URL
        public static final String URL_USTAZLIST = "http://"+localhost+"/society/ustazlist.php";
        public static final String URL_SEARCHUSTAZ = "http://"+localhost+"/society/searchustaz.php";
        public static final String URL_USTAZTOTAL = "http://"+localhost+"/society/ustaztotal.php";


        //----------------------------------------------------------------------------------------------------------------------------
        //SEARCH PAGE URL
        public static final String URL_SEARCHPRODUCTRANGE = "http://"+localhost+"/society/searchproductrange.php";
        public static final String URL_SEARCHPRODUCT = "http://"+localhost+"/society/searchproduct.php";
        public static final String URL_LATESTPRODUCT = "http://"+localhost+"/society/latestproduct.php";

        //----------------------------------------------------------------------------------------------------------------------------
        //FAVOURITE URL
        public static final String URL_ADDFAVOURITE = "http://"+localhost+"/society/addfavourite.php";
        public static final String URL_CHECKFAVOURITE = "http://"+localhost+"/society/checkfavourite.php";
        public static final String URL_FAVOURITELIST = "http://"+localhost+"/society/favouritelist.php?id=";
        public static final String URL_FAVTOTAL = "http://"+localhost+"/society/favouritetotal.php?id=";

        //----------------------------------------------------------------------------------------------------------------------------
        //JOIN EVENT URL
        public static final String URL_JOINEVENT = "http://"+localhost+"/society/joinevent.php";
        public static final String URL_CHECKJOINEVENT = "http://"+localhost+"/society/checkevent.php";
        public static final String URL_JOINEVENTTOTAL = "http://"+localhost+"/society/joineventtotal.php?id=";
        public static final String URL_DISPLAYJOINEVENT = "http://"+localhost+"/society/displayjoinevent.php";
        public static final String URL_DETAILSJOINEVENT = "http://"+localhost+"/society/detailsjoinevent.php?id=";
        public static final String URL_DELETEJOINEVENT = "http://"+localhost+"/society/deletejoinevent.php?id=";
        public static final String URL_JOINUSERTOTAL = "http://"+localhost+"/society/joinusertotal.php?id=";

        public static final String URL_RATEEVENT = "http://"+localhost+"/society/rateevent.php";

        //----------------------------------------------------------------------------------------------------------------------------
        //CART URL
        public static final String URL_ADDCART = "http://"+localhost+"/society/addcart.php";
        public static final String URL_DISPLAYCART = "http://"+localhost+"/society/displaycart.php?id=";
        public static final String URL_DETAILSCART = "http://"+localhost+"/society/detailscart.php?id=";
        public static final String URL_UPDATECART = "http://"+localhost+"/society/updatecart.php";
        public static final String URL_TOTALCART = "http://"+localhost+"/society/totalcart.php?id=";
        public static final String URL_DELETECART = "http://"+localhost+"/society/deletecart.php?id=";


        //----------------------------------------------------------------------------------------------------------------------------
        //ORDER URL
        public static final String URL_DISPLAYORDER = "http://"+localhost+"/society/displayorder.php?id=";
        public static final String URL_DETAILSORDER = "http://"+localhost+"/society/detailsorder.php?id=";
        public static final String URL_DELETEORDER = "http://"+localhost+"/society/deleteorder.php?id=";
        public static final String URL_DETAILSORDERLIST = "http://"+localhost+"/society/detailsorderlist.php?id=";


        public static final String URL_PAYMENTINFO = "http://"+localhost+"/society/paymentinfo.php";
        public static final String URL_PAYMENTPROCESS = "http://"+localhost+"/society/paymentprocess.php";

        public static final String URL_PAYMENTHISTORY = "http://"+localhost+"/society/paymenthistory.php?id=";
        public static final String URL_PAYMENTHISTORYDETAILSLIST = "http://"+localhost+"/society/paymenthistorydetailslist.php?id=";
        public static final String URL_PAYMENTHISTORYDETAILS = "http://"+localhost+"/society/paymenthistorydetails.php?id=";
        public static final String URL_DELETEPAYMENTHISTORY = "http://"+localhost+"/society/paymenthistorydelete.php?id=";

        //----------------------------------------------------------------------------------------------------------------------------
        //RESULT
        public static final String TAG_JSON_ARRAY = "result";
        public static final String TAG_JSON_UCOUNT = "ucount";
        public static final String TAG_JSON_ECOUNT = "ecount";
        public static final String TAG_JSON_PCOUNT = "pcount";
        public static final String TAG_JSON_FCOUNT = "fcount";
        public static final String TAG_JSON_CARTCOUNT = "cartcount";
        public static final String TAG_JSON_JCOUNT = "jcount";
        public static final String TAG_JSON_JUCOUNT = "jucount";

        //----------------------------------------------------------------------------------------------------------------------------
        //JOIN EVENT MODULE
        public static final String TAG_AID = "aid";
        public static final String TAG_JOINEVENTCOUNT = "jointotal";
        public static final String TAG_JOINUSERCOUNT = "joinuser";

        public static final String TAG_RATE = "rate";
        public static final String TAG_COMMENT = "comment";

        public static final String A_ID = "aid";

        //----------------------------------------------------------------------------------------------------------------------------
        //LOCATION MODULE
        public static final String TAG_DISTANCE = "distance";

        //----------------------------------------------------------------------------------------------------------------------------
        //CART MODULE
        public static final String TAG_CID = "cid";
        public static final String TAG_ADDED = "added";
        public static final String TAG_TOTALPRICE = "totalprice";
        public static final String TAG_CARTTOTAL = "carttotal";

        public static final String C_ID = "c_id";

        //----------------------------------------------------------------------------------------------------------------------------
        //User Information
        public static final String TAG_ID = "id";
        public static final String TAG_PROFILENAME = "profilename";
        public static final String TAG_DETAILS = "details";
        public static final String TAG_LOCATION = "location";
        public static final String TAG_LATITUDE = "latitude";
        public static final String TAG_LONGITUDE = "longitude";
        public static final String TAG_CONTACT = "contact";
        public static final String TAG_EMAIL = "email";
        public static final String TAG_PASSWORD = "password";
        public static final String TAG_PHOTO = "photo";


        public static final String ID = "id";
        public static final String PROFILENAME = "profilename";
        public static final String DETAILS = "details";
        public static final String LOCATION = "location";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String CONTACT = "contact";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String PHOTO = "photo";

        //----------------------------------------------------------------------------------------------------------------------------

        //Display Ustaz Information
        public static final String TAG_UID = "uid";
        public static final String TAG_UFIRSTNAME = "firstname";
        public static final String TAG_ULASTNAME = "lastname";
        public static final String TAG_UPROFILENAME = "profilename";
        public static final String TAG_UDOB = "dob";
        public static final String TAG_UPLACE = "place";
        public static final String TAG_UCONTACT = "contact";
        public static final String TAG_UEMAIL = "email";
        public static final String TAG_UPHOTO = "photo";

        public static final String TAG_USTAZCOUNT = "ustazCount";
        public static final String TAG_EVENTCOUNT = "eventCount";
        public static final String TAG_PRODUCTCOUNT = "productCount";
        public static final String TAG_FAVUSTAZCOUNT = "favCount";

        //Search Ustaz / Product
        public static final String SEARCHUSTAZ = "search";
        public static final String SEARCHPRODUCT = "product";
        public static final String SEARCHRANGE1 = "range1";

        public static final String U_ID = "u_id";

        //----------------------------------------------------------------------------------------------------------------------------
        //Display Event Information
        public static final String TAG_EID = "eid";
        public static final String TAG_ECATEGORY = "category";
        public static final String TAG_ETITLE = "title";
        public static final String TAG_EDATE = "date";
        public static final String TAG_EDATEEND = "dateend";
        public static final String TAG_EDAY = "day";
        public static final String TAG_ETIME = "time";
        public static final String TAG_EVENUE = "venue";
        public static final String TAG_EPLACE = "eplace";
        public static final String TAG_ELATITUDE = "latitude";
        public static final String TAG_ELONGITUDE = "longitude";
        public static final String TAG_EIMAGE = "image";

        public static final String E_ID = "e_id";


        //----------------------------------------------------------------------------------------------------------------------------
        //Display Shop Information
        public static final String TAG_PID = "pid";
        public static final String TAG_PNAME = "name";
        public static final String TAG_PPRICE = "price";
        public static final String TAG_PDESC = "desc";
        public static final String TAG_PQUANTITY = "quantity";
        public static final String TAG_PSTATUS = "status";
        public static final String TAG_PIMAGE = "image";

        public static final String P_ID = "p_id";


        //----------------------------------------------------------------------------------------------------------------------------
        //Payment Information

        public static final String PAYFULLNAME = "fullname";
        public static final String PAYADDRESS = "address";
        public static final String PAYEMAIL = "email";
        public static final String PAYCONTACT = "contact";
        public static final String PAYTOTALPRICE = "totalprice";
        public static final String PAYCREATED = "created";

        public static final String TAG_PAYID = "payid";
        public static final String TAG_PAYFULLNAME = "payfullname";
        public static final String TAG_PAYADDRESS = "payaddress";
        public static final String TAG_PAYEMAIL = "payemail";
        public static final String TAG_PAYCONTACT = "paycontact";
        public static final String TAG_PAYQTY = "payqty";
        public static final String TAG_PAYPRICE = "payprice";
        public static final String TAG_PAYCREATED = "paycreated";
        public static final String TAG_PAYSTATUS = "paystatus";
        public static final String TAG_PAYDATE = "paydate";

        public static final String PAY_ID = "pay_id";



}
