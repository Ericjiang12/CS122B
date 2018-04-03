package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BlueActivity extends ActionBarActivity {
    int MAX_page;
    int page_num;
    JSONArray movie_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        TableLayout ll = (TableLayout) findViewById(R.id.movie_list_layout);
        //Bundle bundle = getIntent().getExtras();
      //  Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        //String msg = bundle.getString("message");

        Intent intent=getIntent();
        String jsonArray= intent.getStringExtra("jsonArray");
        page_num=intent.getIntExtra("page_num",-1);
        String pages=Integer.toString(page_num);
        Log.e("LOG",pages);

        int row_num=0;
        try{
            movie_list= new JSONArray(jsonArray);
            MAX_page=(int)Math.ceil(movie_list.length()/5.0);


            for (int i = 5*page_num; i < Math.min(5*(page_num+1),movie_list.length()); i++) {
                JSONObject js = movie_list.getJSONObject(i);
                String movie_title=js.optString("movie_title");
                String movie_id=js.optString("movie_id");
                String movie_year=js.optString("movie_year");
                String movie_genres=js.optString("movie_genres");
                String movie_stars=js.optString("movie_stars");

                /*movie id*/
                TableRow row0= new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row0.setLayoutParams(lp);


                TextView m_id = new TextView(this);
                m_id.setText("ID:");
                m_id.setTextColor(Color.RED);
                m_id.setTextSize(20);
                row0.addView(m_id);
                ll.addView(row0,row_num++);

                TableRow row1= new TableRow(this);
                TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row1.setLayoutParams(lp1);

                TextView M_id = new TextView(this);
                M_id.setText(movie_id);
                M_id.setTextSize(15);
                row1.addView(M_id);
                ll.addView(row1,row_num++);

                /*movie title*/
                TableRow row2= new TableRow(this);
                TableRow.LayoutParams lp2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row2.setLayoutParams(lp2);
                TextView m_t = new TextView(this);
                m_t.setText("Title:");
                m_t.setTextColor(Color.RED);
                m_t.setTextSize(20);
                row2.addView(m_t);
                ll.addView(row2,row_num++);

                TableRow row3= new TableRow(this);
                TableRow.LayoutParams lp3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row3.setLayoutParams(lp3);
                TextView M_t = new TextView(this);
                M_t.setText(movie_title);
                M_t.setTextSize(15);
                row3.addView(M_t);
                ll.addView(row3,row_num++);

                /*movie year*/
                TableRow row4= new TableRow(this);
                TableRow.LayoutParams lp4 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row4.setLayoutParams(lp4);

                TextView m_y = new TextView(this);
                m_y.setText("Year:");
                m_y.setTextColor(Color.RED);
                m_y.setTextSize(20);
                row4.addView(m_y);

                ll.addView(row4,row_num++);

                TableRow row5= new TableRow(this);
                TableRow.LayoutParams lp5 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row5.setLayoutParams(lp);

                TextView M_y = new TextView(this);
                M_y.setText(movie_year);
                M_y.setTextSize(15);
                row5.addView(M_y);

                ll.addView(row5,row_num++);
                /*movie genres*/
                TableRow row_g= new TableRow(this);
                TableRow.LayoutParams lp_g = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row_g.setLayoutParams(lp_g);

                TextView m_g = new TextView(this);
                m_g.setText("List of Genres:");
                m_g.setTextSize(20);
                m_g.setTextColor(Color.RED);
                row_g.addView(m_g);
                ll.addView(row_g,row_num++);


                TableRow row_g1= new TableRow(this);
                TableRow.LayoutParams lp_g1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row_g1.setLayoutParams(lp_g1);

                TextView M_g = new TextView(this);
                M_g.setText(movie_genres);
                M_g.setTextSize(15);
                row_g1.addView(M_g);

                ll.addView(row_g1,row_num++);

                /*movie star*/
                TableRow row_s= new TableRow(this);
                TableRow.LayoutParams lp_s = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row_s.setLayoutParams(lp_s);

                TextView m_s = new TextView(this);

                m_s.setText("List of Stars:");
                m_s.setTextSize(20);
                m_s.setTextColor(Color.RED);
                row_s.addView(m_s);
                ll.addView(row_s,row_num++);


                TableRow row_s1= new TableRow(this);
                TableRow.LayoutParams lp_s1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row_s1.setLayoutParams(lp_s1);
                TextView M_s = new TextView(this);
                String star_list[] = movie_stars.split(",",0);
                int count_s=1;
                for (String temp : star_list)
                {
                    if((count_s%2)==0 || (count_s)==star_list.length)
                    {
                        M_s.setText(M_s.getText()+temp+"\n");
                        count_s++;
                    }
                    else {
                        M_s.setText(M_s.getText() + temp + ",");
                        count_s++;
                    }
                }
                M_s.setTextSize(15);
                row_s1.addView(M_s);

                ll.addView(row_s1,row_num++);






            }

        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void goToNext(View view){

        Intent goToIntent = new Intent(this, BlueActivity.class);
        if ((page_num+1)==MAX_page)
        {
            Toast.makeText(getApplicationContext(),"The is the last page",Toast.LENGTH_SHORT).show();
        }
        else {
            int new_page=page_num+1;
            goToIntent.putExtra("jsonArray", movie_list.toString());
            goToIntent.putExtra("page_num", new_page);
            startActivity(goToIntent);
        }

    }

    public void goToPrev(View view){

        Intent goToIntent = new Intent(this, BlueActivity.class);
        if ((page_num)==0)
        {
            Toast.makeText(getApplicationContext(),"The is the first page",Toast.LENGTH_SHORT).show();
        }
        else {
            int new_page=page_num-1;
            goToIntent.putExtra("jsonArray", movie_list.toString());
            goToIntent.putExtra("page_num", new_page);
            startActivity(goToIntent);
        }

    }

}
