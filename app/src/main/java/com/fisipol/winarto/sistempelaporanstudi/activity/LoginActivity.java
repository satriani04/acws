package com.fisipol.winarto.sistempelaporanstudi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fisipol.winarto.sistempelaporanstudi.R;
import com.fisipol.winarto.sistempelaporanstudi.config.Constant;
import com.fisipol.winarto.sistempelaporanstudi.helper.MyApplication;
import com.fisipol.winarto.sistempelaporanstudi.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etUsername, etPassword;
    private ProgressDialog pDialog;
    private String username, password;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        sessionManager = new SessionManager(getApplicationContext());
        if(sessionManager.isUserLogedIn()){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            //closing all activity from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //add new flag to start activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //start activity
            startActivity(i);
            finish();
        }

        //inisialisasi view
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("please wait....");
        pDialog.setCancelable(true);


        //click handle
        btnLogin = (Button) findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("message", "aku di klik");
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    //do login
                    login(username, password);
                } else {
                    //alert toast
                    Toast.makeText(getApplicationContext(), "enter credential please", Toast.LENGTH_LONG).show();
                }
            }


        });

    }

    public void login(final String username, final String password) {
        //show pdialog
        pDialog.show();

        //start calling volley

        StringRequest loginReq = new StringRequest(Request.Method.POST, Constant.HOST + Constant.URL_SERVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                Log.d("respon", response.toString());

                try{
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    Log.d("status", status);
                    Log.d("tag ok", Constant.TAG_OK);

                    if(status.equals(Constant.TAG_OK)){
                        //login sukses
                        Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_LONG).show();
                        JSONObject data = jObj.getJSONObject("data");
                        Log.d("json data", data.toString());

                        String niu = data.getString("susrNama");
                        String namaMahasiswa = data.getString("susrProfil");

                        sessionManager.createUserLoginSession(namaMahasiswa,niu);

                        Intent dashboard = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(dashboard);
                        finish();

                    }else if(status.equals(Constant.TAG_FAILED)){
                        //login failed
                        Toast.makeText(getApplicationContext(),"login failed",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                Log.d("error",error.getStackTrace().toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("username", username);
                param.put("password", password);
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put(Constant.HEADER,Constant.APIKEY);
                return params;
            }
        };

        MyApplication.getInstance().addToReqQueue(loginReq);

    }

}
