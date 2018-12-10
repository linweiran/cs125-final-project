package edu.illinois.cs.cs125.lab11;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main class for our UI design lab.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab11:Main";

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    /**
     * Run when this activity comes to the foreground.
     *
     * @param savedInstanceState unused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        setContentView(R.layout.activity_main);

        //startAPICall("192.17.96.8");
        //startAPICall();
        Button btn = findViewById(R.id.getweather);
        final EditText toinput = findViewById(R.id.inputedit);
        final EditText toinput2 = findViewById(R.id.inputedit2);
        final EditText toinput3 = findViewById(R.id.inputedit3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] li = new String[153];

                int a = toinput.getText().toString().hashCode();
                int b = toinput2.getText().toString().hashCode();
                int c = toinput3.getText().toString().hashCode();
                if (a < 0) a = -a;
                if (b < 0) b = -b;
                if (c < 0) c = -c;
                a = a % 150;
                b = b % 150;
                c = c % 150;
                if (a == b) b++;
                if (b == c) c++;
                startAPICall(a,b,c);
                //Toast.makeText(MainActivity.this, Integer.toString(a)+Integer.toString(b)+Integer.toString(c), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * Run when this activity is no longer visible.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Make a call to the IP geolocation API.
     *
     *ipAddress IP address to look up
     */
    void startAPICall(final int a, final int b, final int c) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://dog.ceo/api/breeds/list/all",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            //apiCallDone(response);
                            Log.d(TAG, response.toString());
                            String k = response.toString();
                            String aa="by";
                            String bb="default";
                            String cc="value";
                            String l="";
                            int count = 0;
                            boolean x = false;
                            int i = 0;
                            while (i < k.length()) {
                                if (k.charAt(i) == '"') {
                                    if (x == false) {
                                        x= true;
                                    }   else {
                                        Log.d(TAG, l);
                                        if (count+3 == a) aa=l;
                                        if (count+3 == b) bb=l;
                                        if (count+3 == c) cc=l;
                                        l = "";

                                        count++;
                                        x = false;
                                    }
                                    i++;
                                    continue;
                                }
                                if (x) {
                                    l += k.charAt(i);
                                }
                                i++;
                            }
                            Log.d(TAG,Integer.toString(count));
                            final TextView tv = findViewById(R.id.textView);
                            tv.setText(aa+", "+bb+", "+cc);
                            //Toast.makeText(MainActivity.this, aa+", "+bb+", "+cc, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.e(TAG, error.toString());
                        }
                    });
            jsonObjectRequest.setShouldCache(false);
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            Log.i(TAG, response.get("hostname").toString());
        } catch (JSONException ignored) { }
    }
}
