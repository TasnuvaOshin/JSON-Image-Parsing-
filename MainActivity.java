package com.example.oshin.myimagefetching;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
   		.cacheInMemory(true)
                .cacheOnDisk(true)

           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
           .defaultDisplayImageOptions(defaultOptions)

           .build();
        ImageLoader.getInstance().init(config); // Do it on Application start






        l1= (ListView) findViewById(R.id.l1);

        JsonWork jsonWork = new JsonWork();
        jsonWork.execute();

    }


    public  class JsonWork extends AsyncTask<String,String,List<CarModel>>{


         HttpURLConnection httpURLConnection = null;
          BufferedReader bufferedReader = null;

        @Override
        protected List<CarModel> doInBackground(String... strings) {

            try {
                URL url = new URL("https://api.myjson.com/bins/k2x7t");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader =  new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                String myFile;
                int i;

                while ((line = bufferedReader.readLine()) != null ){

                            stringBuffer.append(line);

                }

                      myFile = stringBuffer.toString();

                List<CarModel> carModelList = new ArrayList<>();
                JSONObject FileObject = new JSONObject(myFile);
                JSONArray cars = FileObject.getJSONArray("cars");

                for (i=0; i<cars.length(); i++){

                      JSONObject innerObject = cars.getJSONObject(i);

                         CarModel carModel = new CarModel();

                          carModel.setName(innerObject.getString("name"));
                          carModel.setImg(innerObject.getString("img"));

                    carModelList.add(carModel);

                }

                 return  carModelList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                httpURLConnection.disconnect();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(List<CarModel> s) {
            super.onPostExecute(s);

            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),R.layout.sample,s);

            l1.setAdapter(customAdapter);


        }
    }


}
