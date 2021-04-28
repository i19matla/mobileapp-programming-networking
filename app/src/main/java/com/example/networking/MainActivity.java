package com.example.networking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    //private ArrayList<Mountain>mountains; //Skapar den privata medlemsvariabeln av filen mountain.java
    private Mountain mountains[] = {new Mountain()};
    private ArrayAdapter<Mountain> adapter; //Skapar en global variabel även kallat medlemsvariabel
    private ArrayList<Mountain> mountainsListan = new ArrayList<>(Arrays.asList(mountains));
    private ListView my_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");
        adapter = new ArrayAdapter<Mountain>(this,R.layout.listatext,R.id.textView2, mountainsListan); //Skapar objeket
        my_listview = (ListView) findViewById(R.id.listans_id);   //Skapar referensen tänk ungefär som pekare i C++

        my_listview.setAdapter(adapter); //Här läggs JsonTask skörden in till listan

        my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Rostat bröd är gött även på " + mountains[1], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d("TAG", json);
            Gson gson = new Gson();
            mountains = gson.fromJson(json, Mountain[].class);
            adapter.notifyDataSetChanged();         //Fram hit uppdateras bara listan i ramminnet
            adapter = new ArrayAdapter<Mountain>(MainActivity.this,R.layout.listatext,R.id.textView2, mountains);
            my_listview.setAdapter(adapter); //raden ovan och denna skriver ut så vackert

            for(int i = 0; i < mountains.length; i++) {
                Log.d("Shottabalulu", "onPostExecute: Berget heter " + mountains[i].getType());
            }
        }
    }

}
