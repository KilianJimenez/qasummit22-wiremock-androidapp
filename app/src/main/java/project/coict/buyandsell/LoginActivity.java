package project.coict.buyandsell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class LoginActivity extends AppCompatActivity {

    final String API_PORT = "3000";
    final String WIREMOCK_PORT = "8080";
    final String URL = "http://192.168.0.22:" + API_PORT + "/login";

    int statusCode;

    Button btnlogin;
    EditText mailField, passField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get elements on view
        mailField = findViewById(R.id.txtmailname);
        passField = findViewById(R.id.txtpassword);
        btnlogin = findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(view -> {

            JSONObject jsonBody = new JSONObject();

            try{

                jsonBody.put("usermail", mailField.getText().toString());
                jsonBody.put("password", passField.getText().toString());

                final String requestBody = jsonBody.toString();

                // Call Login method
                doLogin(requestBody);

            } catch (JSONException e){
                e.printStackTrace();
            }
        });
    }

    public void doLogin(final String body){

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                response -> {

                    // Check response code
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("Response: ", jsonObject.toString());

                        if(statusCode == HttpURLConnection.HTTP_OK){

                            //Call MainActivity if successful login
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);

                        } else
                            Toast.makeText(getApplicationContext(), "Error: Something went wrong...", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {

                        e.printStackTrace();
                        Log.d("JSON Error: ", e.toString());

                    }
                },
                error -> {

                    // Handle error
                    if(error.networkResponse.statusCode == HttpURLConnection.HTTP_FORBIDDEN)
                        Toast.makeText(getApplicationContext(),
                                "Error: Invalid credentials", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),
                                "Error: " + error,Toast.LENGTH_SHORT).show();

                    Log.d("Error: ", error.toString());

                }
                ){

            @Override
            public byte[] getBody() {
                return body == null ? null : body.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            };

        // Do request
        mRequestQueue.add(stringRequest);
    }
}
