package com.example.macbook.eventapp.activity.Ustaz.Event;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.macbook.eventapp.R;
import com.example.macbook.eventapp.activity.Ustaz.Config;
import com.example.macbook.eventapp.activity.Ustaz.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewComment extends AppCompatActivity {

    private String JSON_STRING;
    String uid, uemail, e_id;
    ListView commentlistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcomment);

        Intent intent = getIntent();
        uid = intent.getStringExtra(Config.TAG_UID);
        e_id = intent.getStringExtra(EventConfig.E_ID);
        uemail = intent.getStringExtra(Config.TAG_UEMAIL);

        commentlistView = (ListView) findViewById(R.id.commentlistView);

        getComment();
    }

    private void showComment(){


        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(EventConfig.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String comment = jo.getString(Config.TAG_COMMENT);
                String user = jo.getString(Config.TAG_USER);
                String userphoto = jo.getString(Config.TAG_USERPHOTO);


                HashMap<String, String> event = new HashMap<>();
                event.put(Config.TAG_COMMENT, comment);
                event.put(Config.TAG_USER, user);
                event.put(Config.TAG_USERPHOTO, userphoto);

                list.add(event);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        CommentAdapter commentAdapter = new CommentAdapter(this, list);
        commentlistView.setAdapter(commentAdapter);

    }

    private void getComment(){
        class GetComment extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ViewComment.this,"Loading Data","Please wait for a moment...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showComment();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.URL_COMMENTDISPLAY, e_id);
                return s;
            }
        }
        GetComment gc = new GetComment();
        gc.execute();
    }

    public void GoToEventArchiveDetails(View view)
    {

        Intent intent = new Intent(this, EventArchiveDetails.class);
        finish();

        Bundle bundle = new Bundle();
        bundle.putString(Config.TAG_UID, uid);
        bundle.putString(EventConfig.E_ID, e_id);
        bundle.putString(Config.UEMAIL, uemail);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
