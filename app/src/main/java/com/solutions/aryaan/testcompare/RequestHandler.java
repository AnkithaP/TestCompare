package com.solutions.aryaan.testcompare;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ankitha on 12/16/2016.
 */

public class RequestHandler {
    public String sendGetRequest(String uri){
        try {
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String result = null;

            StringBuilder sb = new StringBuilder();

            while ((result = reader.readLine())!= null){
                sb.append(result);
            }
            return sb.toString();

        } catch (Exception e) {
            return null;
            //e.printStackTrace();
        }

    }

    public String sendPostRequest(String requestURL, HashMap<String,String> params){
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = br.readLine();
            }else {
                response = "Error Registering";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry :
                params.entrySet()) {
            if (first){
                first = false;
            }else
                builder.append("&");

            builder.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(),"UTF-8"));

        }
        return builder.toString();
    }
}
