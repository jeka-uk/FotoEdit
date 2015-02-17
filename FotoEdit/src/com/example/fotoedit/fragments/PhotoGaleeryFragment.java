package com.example.fotoedit.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.fotoedit.Constants;
import com.example.fotoedit.MainActivity;
import com.example.fotoedit.adapters.ImageAdapter;
import com.example.fotoedit.http.HttpClient;
import com.example.multiexpo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoGaleeryFragment extends Fragment {

	private String resultJson = "";
	private JSONObject dataJsonObj = null;
	private String imageUrl = "", mUrl;
	private GridView gridview;
	public static final String EXTRA_URL = "url";
	// private static long back_pressed;

	ArrayList<String> imagesUrls = new ArrayList<String>();

	private class InstagramTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			return new HttpClient().connect();
		}

		@Override
		protected void onPostExecute(String strJson) {
			super.onPostExecute(strJson);

			try {
				dataJsonObj = new JSONObject(strJson);
				JSONArray jsonArray = dataJsonObj.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject imageJsonObject = jsonArray.getJSONObject(i)
							.getJSONObject("images")
							.getJSONObject("standard_resolution");
					imageUrl = imageJsonObject.getString("url");
					imagesUrls.add(imageUrl);
					// Log.d(LOG_TAG, "Url Image: " + imageUrl);
					gridview.setAdapter(new ImageAdapter(getActivity(),
							imagesUrls));

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new InstagramTask().execute();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photogaleery, container,
				false);

		isNetworkConnected();

		gridview = (GridView) view.findViewById(R.id.gridView1);
		gridview.setAdapter(new ImageAdapter(getActivity(), imagesUrls));
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				mUrl = imagesUrls.get(position);
				sendResult(Activity.RESULT_OK);
				getActivity().onBackPressed();

			}
		});

		return view;
	}

	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return;
		Intent i = new Intent();
		i.putExtra(EXTRA_URL, mUrl);
		getTargetFragment().onActivityResult(getTargetRequestCode(),
				resultCode, i);
	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Передача мобильных данных отключена")
					.setMessage(
							"Включите передачу пакетных данных или воспользуйтесь Wi-Fi для завершения этого действия")
					.setCancelable(false)
					.setNegativeButton("ОК",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									onBackPressed();
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			return false;
		} else
			return true;
	}

	public void onBackPressed() {

		int count = getFragmentManager().getBackStackEntryCount();

		if (count == 0) {
			getActivity().onBackPressed();

		} else {
			getFragmentManager().popBackStack();
		}

	}

}
