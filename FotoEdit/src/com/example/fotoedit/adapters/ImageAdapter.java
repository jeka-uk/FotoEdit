package com.example.fotoedit.adapters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.multiexpo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	public static String LOG_TAG = "my_log";

	ArrayList<String> arrayList = new ArrayList();

	public ImageAdapter(Context c, ArrayList<String> ob) {
		mContext = c;
		arrayList = ob;

	}

	public int getCount() {
		return arrayList.size();
	}

	public Object getItem(int position) {
		return arrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {

			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(
					(int) mContext.getResources().getDimension(R.dimen.gridview_height_width),
					(int) mContext.getResources().getDimension(R.dimen.gridview_height_width)));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(3, 3, 3, 3);
		} else {
			imageView = (ImageView) convertView;
		}

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader.displayImage(arrayList.get(position), imageView, options);

		return imageView;

	}
}
