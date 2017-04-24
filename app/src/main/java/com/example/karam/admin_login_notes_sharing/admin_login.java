package com.example.karam.admin_login_notes_sharing;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

  public class admin_login extends AppCompatActivity {

    private EditText email_id, username_id, password_id;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        Button login = (Button) findViewById(R.id.login_button);

        email_id = (EditText) findViewById(R.id.email_edit_text);
        username_id = (EditText) findViewById(R.id.username_edit_text);
        password_id = (EditText) findViewById(R.id.password_edit_text);

        final View.OnClickListener onbtn_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.login_button)
                {
                    Intent i = new Intent(admin_login.this , MainActivity.class);


                    startActivity(i);


                }


            }
        };

        login.setOnClickListener(onbtn_click);
    }




    public void login()
    {
        String email = email_id.getText().toString();
        String username = username_id.getText().toString();
        String password = password_id.getText().toString();

        JSONObject job = new JSONObject();

        try {
            job.put("email_key", email);
            job.put("username_key",username);
            job.put("password_key" , password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jobreq = new JsonObjectRequest("http://192.168.0.17/admin_login_note_sharing.php", job, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getString("key").equals("done"))
                    {
                        Toast.makeText(admin_login.this, " done ", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor sp = getSharedPreferences("admin_info",MODE_PRIVATE).edit();

                        sp.putString("admin_id",response.getString("admin_id"));

                        sp.commit();

                        Intent i = new Intent(admin_login.this ,MainActivity.class);

                        startActivity(i);
                        finish();
                    }

                    else {
                        Toast.makeText(admin_login.this, "error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println(error);

            }
        });

        jobreq.setRetryPolicy(new DefaultRetryPolicy(20000, 2, 2));

        AppController app = new AppController(admin_login.this);

        app.addToRequestQueue(jobreq);
    }
}



