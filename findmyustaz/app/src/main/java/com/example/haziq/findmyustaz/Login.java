package com.example.haziq.findmyustaz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class Login extends AppCompatActivity {

    final String TAG = this.getClass().getSimpleName();
    EditText txtEmail, txtPassword;
    Button btnLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Email required");
                    txtEmail.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Password required");
                    txtPassword.requestFocus();
                    return;
                }


                UserLoginFunction(email, password);

            }
        });

    }

    public void UserLoginFunction(final String semail, final String spassword){

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

                if(httpResponseMsg.equalsIgnoreCase("You have been successfully logged in")){

                    Toast.makeText(Login.this,httpResponseMsg,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    finish();

                    Bundle bundle = new Bundle();
                    bundle.putString(UserConfig.EMAIL, semail);
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
                params.put(UserConfig.EMAIL, semail);
                params.put(UserConfig.PASSWORD, spassword);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(UserConfig.URL_LOGIN, params);
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
