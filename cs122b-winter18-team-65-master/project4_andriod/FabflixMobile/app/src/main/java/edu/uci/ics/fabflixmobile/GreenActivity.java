package edu.uci.ics.fabflixmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GreenActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green);


    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back button again to logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void connectToTomcat(View view) {

        //

        final Map<String, String> params = new HashMap<String, String>();


        // no user is logged in, so we must connect to the server
        RequestQueue queue = Volley.newRequestQueue(this);

        final Context context = this;
        String query = ((EditText) findViewById(R.id.movie_message)).getText().toString();
        String search_url = "http://13.59.189.185:8080/FablixMobile/NFT_search?query=" + query;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (search_url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                                if (response.length()>0 && response!=null){
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject js = response.getJSONObject(i);

                                }
                                    Intent goToIntent = new Intent(GreenActivity.this, BlueActivity.class);
                                    goToIntent.putExtra("jsonArray",response.toString());
                                    goToIntent.putExtra("page_num",0);

                                    startActivity(goToIntent);

                                }

                                else {
                                AlertDialog alertDialog = new AlertDialog.Builder(GreenActivity.this).create();
                                alertDialog.setTitle("Alert");
                                alertDialog.setMessage("No matching movies");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i("LOG", error.toString());
                    }
                });
        queue.add(jsonArrayRequest);


        return;
    }

}