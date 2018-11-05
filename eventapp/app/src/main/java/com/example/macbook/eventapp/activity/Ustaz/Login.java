package com.example.macbook.eventapp.activity.Ustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.macbook.eventapp.R;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();
    EditText txtEmail, txtPassword, txtICNo;
    Button btnLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtICNo = (EditText)findViewById(R.id.txtICNo);

        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Uemail = txtEmail.getText().toString();
                final String Upassword = txtPassword.getText().toString();
                final String Uicno = txtICNo.getText().toString();

                if (TextUtils.isEmpty(Uemail)) {
                    txtEmail.setError("Email required");
                    txtEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Upassword)) {
                    txtPassword.setError("Password required");
                    txtPassword.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(Uicno)) {
                    txtICNo.setError("IC No required");
                    txtICNo.requestFocus();
                    return;
                }

                UserLoginFunction(Uemail, Upassword, Uicno);

            }
        });

    }

    public void UserLoginFunction(final String email, final String password, final String icno){

        class UserLoginClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(Login.this,"Loading Data",null,true,true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                if(httpResponseMsg.equalsIgnoreCase("Login success")){

                    Toast.makeText(Login.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    finish();

                    Bundle bundle = new Bundle();
                    bundle.putString(Config.UEMAIL, email);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                else{

                    Toast.makeText(Login.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(Void... v) {

                HashMap<String,String> params = new HashMap<>();
                params.put(Config.UEMAIL, email);
                params.put(Config.UPASSWORD, password);
                params.put(Config.UICNO, icno);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_LOGIN, params);
                return res;
            }
        }

        UserLoginClass userLoginClass = new UserLoginClass();
        userLoginClass.execute();
    }


    public void GoToRegister(View view)
    {
        startActivity(new Intent(this, Register.class));
    }

}
