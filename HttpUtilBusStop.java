package com.example.splendor.bustest;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Splendor on 2017-04-10.
 */

public class HttpUtilBusStop extends AsyncTask<String,String,String> {
    private Context context;
    public HttpUtilBusStop(Context context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... objects) {
        String url = objects[0];
        try{
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            System.out.println("******************* >>> server conn : "+conn);

            // 커넥션 옵션들
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true); conn.setDoOutput(true);
            conn.setRequestProperty("content-type","application/x-xxx-form-urlencoded");

            OutputStream os = conn.getOutputStream();
            os.flush();
            os.close();
            int returnCode = conn.getResponseCode();
            System.out.println("******************* >>> server response code " + returnCode);

            if(returnCode == HttpURLConnection.HTTP_OK){
                System.out.println("******************* >>> server response ok");
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                char[] buff = new char[1024];
                int length = -1;
                String line = "";
                StringBuffer response = new StringBuffer();
                while ((length =br.read(buff)) != -1){
                    response.append(new String(buff,0,length));
                }
                br.close();
                System.out.println("******************* >>> server response" + response.toString());
                return response.toString();
            }else{
                System.out.println("******************* >>> server response error");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String json) {
        System.out.println("******************* >>> onPostExecute called...........");
        JSONArray ary = null;
        try{
            ary = new JSONArray(json);
            ((BusInfoActivity)context).parseAry(ary);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
