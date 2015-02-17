package com.example.fotoedit.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.fotoedit.Constants;
import com.example.multiexpo.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

public class SaveImageFragment extends Fragment {

	private ImageView savemail, savegaler, saveinstagram, savetwitter, nextSave;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_save, container, false);

		savemail = (ImageView) view.findViewById(R.id.savemail);
		savegaler = (ImageView) view.findViewById(R.id.savegaler);
		saveinstagram = (ImageView) view.findViewById(R.id.saveinstagram);
		savetwitter = (ImageView) view.findViewById(R.id.savetwit);
		nextSave = (ImageView) view.findViewById(R.id.nextSave);
		
		
		nextSave.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {				
				

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(nextSave, 0.9f);					

					
				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(nextSave, 1f);
					deleteFiles("mnt/sdcard/" + Constants.FOLDER_PROGRAM);
					getActivity().finish();

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});
		

		savemail.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {				
				

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(savemail, 0.9f);

					sendMail("file:///mnt/sdcard/" + Constants.FOLDER_PROGRAM
							+ "/image.jpg");

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(savemail, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		savegaler.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(savegaler, 0.9f);

					galleryAddPic("file:///mnt/sdcard/"
							+ Constants.FOLDER_PROGRAM + "/image.jpg");

					Toast toast = Toast.makeText(getActivity()
							.getApplicationContext(), "Сохранено в галерее",
							Toast.LENGTH_SHORT);
					toast.show();
					getActivity().finish();

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(savegaler, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		saveinstagram.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(saveinstagram, 0.9f);
					boolean installed = isPackageInstalled(
							"com.instagram.android", getActivity());
					if (installed) {
						createIntent("com.instagram.android",
								"file:///mnt/sdcard/"
										+ Constants.FOLDER_PROGRAM
										+ "/image.jpg", "Foto to Multi Expo");
					} else {
						Toast toast = Toast.makeText(getActivity()
								.getApplicationContext(),
								"Приложение Instagram не установлено",
								Toast.LENGTH_SHORT);
						toast.show();
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(saveinstagram, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		savetwitter.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(savetwitter, 0.9f);

					boolean installed = isPackageInstalled(
							"com.twitter.android", getActivity());
					if (installed) {
						createIntent("com.twitter.android",
								"file:///mnt/sdcard/"
										+ Constants.FOLDER_PROGRAM
										+ "/image.jpg", "Foto to Multi Expo");
					} else {
						Toast toast = Toast.makeText(getActivity()
								.getApplicationContext(),
								"Приложение Twitter не установлено",
								Toast.LENGTH_SHORT);
						toast.show();
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(savetwitter, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		return view;

	}

	private void scaleAnim(ImageView imageWiew, float f) {

		ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imageWiew, "scaleX",
				f);
		ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imageWiew, "scaleY",
				f);

		scaleDownX.setDuration(0);
		scaleDownY.setDuration(0);

		AnimatorSet scaleDown = new AnimatorSet();

		scaleDown.play(scaleDownX).with(scaleDownY);
		scaleDown.start();

	}

	public void sendMail(String filepath) {

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("application/image");

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Multi Expo foto");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"Foto from Multi Expo");
		emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filepath));
		startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}

	private void galleryAddPic(String mCurrentPhotoPath) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.parse(mCurrentPhotoPath);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	private void createIntent(String type, String mediaPath, String caption) {

		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mediaPath));
		shareIntent.putExtra(Intent.EXTRA_TEXT, caption);
		shareIntent.setPackage(type);
		getActivity().startActivity(shareIntent);
	}

	private boolean isPackageInstalled(String packagename, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);

			return true;
		} catch (NameNotFoundException e) {

			return false;
		}
	}
	
	public static void deleteFiles(String path) {

		File file = new File(path);

		if (file.exists()) {
			String deleteCmd = "rm -r " + path;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) {
			}
		}
	}

}
