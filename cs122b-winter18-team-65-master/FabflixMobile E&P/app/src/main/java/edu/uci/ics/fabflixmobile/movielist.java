package edu.uci.ics.fabflixmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Button;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.view.View.OnClickListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;

import static edu.uci.ics.fabflixmobile.R.id.raw_query;

public class movielist extends ActionBarActivity {
    private ListView listview;
    private TextView table_title;
    private ArrayList<String> data;
    ArrayAdapter<String> sd;
    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE   = 10;
    private int noOfBtns;
    private Button[] btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movielist);
        listview = (ListView)findViewById(R.id.list);
        table_title = (TextView)findViewById(R.id.table_title);
        Bundle bundle = getIntent().getExtras();
        String query = bundle.getString("query");
        Log.e("log",query);
        doSearch(query);
        query = "Search result for \"" + query + "\"";
        ((TextView)findViewById(raw_query)).setText(query);
        return;
    }


    public void doSearch(String query) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String search_url = "http://13.59.189.185:8080/FablixMobile/NFT_search?query=" + query;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (search_url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() == 0){
                            AlertDialog alertDialog = new AlertDialog.Builder(movielist.this).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("No matching result.");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            try {
                                TOTAL_LIST_ITEMS = response.length();
                                Btnfooter();
                                data = new ArrayList<>();
                                for (int i = 0; i < TOTAL_LIST_ITEMS; i++) {
                                    JSONObject one_record = response.getJSONObject(i);
                                    String one_row = "\nid: " + one_record.optString("movie_id") + "\n";
                                    one_row +=  "title: " + one_record.optString("movie_title") + "\n";
                                    one_row +=  "year: " + one_record.optString("movie_year") + "\n";
                                    one_row +=  "director: " + one_record.optString("movie_director") + "\n";
                                    one_row +=  "genres: " + one_record.optString("movie_genres") + "\n";
                                    one_row +=  "stars: " + one_record.optString("movie_stars") + "\n";
                                    data.add(one_row);
                                }
                                loadList(0);
                                CheckBtnBackGroud(0);
                            }catch (JSONException e) {
                            e.printStackTrace();
                            }
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



    private void Btnfooter() {
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfBtns = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;

        LinearLayout ll = (LinearLayout) findViewById(R.id.btnLay);

        btns = new Button[noOfBtns];

        for (int i = 0; i < noOfBtns; i++) {
            btns[i] = new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText("" + (i + 1));

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            ll.addView(btns[i], lp);

            final int j = i;
            btns[j].setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    loadList(j);
                    CheckBtnBackGroud(j);
                }
            });
        }
    }



    private void CheckBtnBackGroud(int index)
    {
        table_title.setText("Page "+(index+1)+" of " + noOfBtns);
        for(int i=0;i<noOfBtns;i++)
        {
            if(i==index)
            {
                btns[i].setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            else
            {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }

    }

    private void loadList(int number)
    {
        ArrayList<String> sort = new ArrayList<>();

        int start = number * NUM_ITEMS_PAGE;
        for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
        {
            if(i<data.size())
                sort.add(data.get(i));
            else
                break;
        }
        sd = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sort);
        listview.setAdapter(sd);
    }


}
