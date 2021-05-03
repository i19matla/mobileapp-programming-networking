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

    private ArrayAdapter<Mountain> adapter; //Skapar en global variabel även kallat medlemsvariabel
    private ArrayList<Mountain> mountainsListan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter<Mountain>(this,R.layout.listatext,R.id.textView2, mountainsListan); //Skapar objeket
        ListView myListview = findViewById(R.id.listans_id);   //Skapar referensen tänk ungefär som pekare i C++
        myListview.setAdapter(adapter); //raden ovan och denna skriver ut så vackert
        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Berget " + mountainsListan.get(position) + " är " + mountainsListan.get(position).getSize() + "meter högt och det ligger i "+ mountainsListan.get(position).getLocation(), Toast.LENGTH_SHORT).show();
            }
        });
        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=brom");
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
            Log.d("TAG", json); //Inspekterer jsonfilen från nätet
            Gson gson = new Gson();
            Mountain[] mountains = gson.fromJson(json, Mountain[].class); //Alla klasser är datatyper men alla datatyper är som känt inte klasser
            mountainsListan.clear();

            for(int i = 0; i < mountains.length; i++) {
                Log.d("Shottabalulu", "onPostExecute: Berget heter " + mountains[i].getName());
                mountainsListan.add(mountains[i]);
            }

            adapter.notifyDataSetChanged();         //Den här talar om för adaptern att det finns uppdaterad information knuten till den.
        }
    }

}
