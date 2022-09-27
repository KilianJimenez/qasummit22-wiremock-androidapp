package project.coict.buyandsell;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    String TAG_SUCCESS="success";
    String TAG_DATA="profile";
    TextView txtfullname,txtgender,txtemail,txtusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtfullname=findViewById(R.id.profile_fullname);
        txtgender=findViewById(R.id.profile_gender);
        txtemail=findViewById(R.id.profile_email);
        txtusername=findViewById(R.id.profile_username);

        getProfile("mojo");
    }


    //**********************************Get PROFILE************************************************//
    public void getProfile(final String username){


        RequestQueue mRequestQueue;

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();

        //change this IP 192.168.56.1 to your IP
        // String url ="http://192.168.56.1/BuyandSell/login.php";

        //access online scripts,using this url you can even run to your mobile
        String url ="http://192.168.0.22:8000/get_profile.php";

// Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Do something with the response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Integer success=jsonObject.getInt(TAG_SUCCESS);
                            if(success==1){
                                JSONArray jsonArray=jsonObject.getJSONArray(TAG_DATA);
                                JSONObject dataobj=jsonArray.getJSONObject(0);

//                                dataobj.getString("fname");
//                                dataobj.getString("lname");
//                                dataobj.getString("email");
//                                dataobj.getString("gender");
//                                dataobj.getString("username");

                                txtfullname.setText("Full Name : "+dataobj.getString("fname")+" "+dataobj.getString("lname"));
                                txtemail.setText("Email : "+dataobj.getString("email"));
                                txtgender.setText("Gender : "+dataobj.getString("gender"));
                                txtusername.setText("Gender : "+dataobj.getString("username"));


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String ,String> getParams() throws AuthFailureError {

                Map<String,String> params=new HashMap<String,String>();
                params.put("username",username);


                return params;
            }};

        //stringRequest.setShouldCache(false); //Use this line if dont want to save in cache
// Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }
}


