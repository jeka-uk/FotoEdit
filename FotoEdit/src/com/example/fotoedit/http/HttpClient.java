package com.example.fotoedit.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class HttpClient {

	private static final String TAG = "Instagram";

	private static final String CLIENT_ID = "fa1157cde01a447abd94046d96c02942";
	private static final String API_REQUEST = "scenic";
	private static final String API_URL = "https://api.instagram.com/v1";
	private HttpURLConnection urlConnection = null;
	private BufferedReader reader = null;
	private String resultJson = "";
	private JSONObject dataJsonObj = null;
	private String imageUrl = "";
	private String downloadJsonUrl = API_URL + "/tags/" + API_REQUEST
			+ "/media/recent?client_id=" + CLIENT_ID;
	
	public String connect() {

		try {
			URL url = new URL(downloadJsonUrl);

			urlConnection = (HttpURLConnection) url.openConnection();
		    urlConnection.setReadTimeout(20000);
			urlConnection.setConnectTimeout(20000);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();

			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			resultJson = buffer.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}
}
