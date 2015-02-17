package com.example.fotoedit.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.fotoedit.Constants;
import com.example.multiexpo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivityFragment extends Fragment {

	private ImageView imageTop, imageBottom, buttonNext, buttonTopCamera,
			buttonTopGaler, buttonTopInternet, buttonBottomCamera,
			buttonButtomGaler, buttonButtomInternet, ramkaimageTop,
			ramkaImageButton;
	private boolean animMenuOnTop = true, animMenuOnButtom = true,
			loadingImageButtom = false, loadingImageTop = false,
			loadinInternetTop = false, loadingInternetButtom = false;
	private Uri outputFileUri;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);

		ramkaImageButton = (ImageView) view.findViewById(R.id.imageView2);
		ramkaimageTop = (ImageView) view.findViewById(R.id.imageView1);
		imageTop = (ImageView) view.findViewById(R.id.imagetop);
		imageBottom = (ImageView) view.findViewById(R.id.imagebuttom);
		buttonNext = (ImageView) view.findViewById(R.id.next);

		buttonTopCamera = (ImageView) view
				.findViewById(R.id.imageViewaAnimCamera);
		buttonTopGaler = (ImageView) view
				.findViewById(R.id.imageViewaAnimGalereya);
		buttonTopInternet = (ImageView) view
				.findViewById(R.id.imageViewaAnimInternet);

		buttonBottomCamera = (ImageView) view.findViewById(R.id.imagecamera);
		buttonButtomGaler = (ImageView) view.findViewById(R.id.imagegalerey);
		buttonButtomInternet = (ImageView) view
				.findViewById(R.id.imageInternet);

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		imageTop.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageTop, 0.9f);
					scaleAnim(ramkaimageTop, 0.9f);

					if (animMenuOnButtom == true) {
						if (animMenuOnTop == true) {
							animateMenuImageTopOn();
							animMenuOnTop = false;
						} else {
							animateMenuImageTopOff();
							animMenuOnTop = true;
						}
					} else {
						animateMenuImageButtomOff();
						animMenuOnButtom = true;
						animateMenuImageTopOn();
						animMenuOnTop = false;
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageTop, 1f);
					scaleAnim(ramkaimageTop, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		imageBottom.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageBottom, 0.9f);
					scaleAnim(ramkaImageButton, 0.9f);

					if (animMenuOnTop == true) {
						if (animMenuOnButtom == true) {
							animateMenuImageButtomOn();
							animMenuOnButtom = false;
						} else {
							animateMenuImageButtomOff();
							animMenuOnButtom = true;
						}
					} else {
						animateMenuImageTopOff();
						animMenuOnTop = true;
						animateMenuImageButtomOn();
						animMenuOnButtom = false;
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageBottom, 1f);
					scaleAnim(ramkaImageButton, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		buttonTopInternet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragment(new PhotoGaleeryFragment(),
						Constants.URL_RESULT_TOP);

			}
		});

		buttonTopGaler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				galeery(Constants.GALLERY_RESULT_TOP);

			}
		});

		buttonTopCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(Constants.CAMERA_RESULT_TOP, Constants.IMGAE_BUTOM_LEFT);

			}
		});

		buttonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragment(new EditFotoFragment(), Constants.NEXT);

			}
		});

		buttonButtomGaler.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				galeery(Constants.GALLERY_RESULT_BUTTOM);

			}
		});

		buttonBottomCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(Constants.CAMERA_RESULT_BUTTOM,
						Constants.IMGAE_BUTOM_RIGHT);

			}
		});

		buttonButtomInternet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragment(new PhotoGaleeryFragment(),
						Constants.URL_RESULT_BUTTOM);

			}
		});

		return view;

	}

	private void animMenu(ImageView mImageView, int x, int y) {
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", x);
		PropertyValuesHolder pvyY = PropertyValuesHolder.ofFloat("y", y);
		ObjectAnimator.ofPropertyValuesHolder(mImageView, pvhX, pvyY)
				.setDuration(100).start();

		ObjectAnimator anim = ObjectAnimator.ofFloat(mImageView, "rotation",
				0f, 360f);
		anim.setDuration(400);
		anim.start();
	}

	private void animMenuOff(ImageView image1, int x, int y, ImageView image2,
			ImageView image3, boolean animMenuOn, boolean animMenuOn_1) {
		animMenu(image1, x, y);
		animMenu(image2, x, y);
		animMenu(image3, x, y);
		animMenuOn = true;
		animMenuOn_1 = true;

	}

	public void startFragment(Fragment fragment, int REQUEST) {
		fragment.setTargetFragment(MainActivityFragment.this, REQUEST);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.add(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();
		animMenuOff(
				buttonTopInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_off_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_off_y),
				buttonTopGaler, buttonTopCamera, true, true);
		animMenuOff(
				buttonButtomInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_y), buttonButtomGaler,
				buttonBottomCamera, true, true);

	}

	private void galeery(int REQUEST) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, REQUEST);
		animMenuOff(
				buttonTopInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_off_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_off_y),
				buttonTopGaler, buttonTopCamera, true, true);
		animMenuOff(
				buttonButtomInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_y), buttonButtomGaler,
				buttonBottomCamera, true, true);

	}

	private void camera(int CAMRES, String nameFile) {

		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File dirToSave = new File(Environment.getExternalStorageDirectory()
				+ File.separator + Constants.FOLDER_PROGRAM);
		dirToSave.mkdir();
		File file = new File(dirToSave, nameFile + ".jpg");
		outputFileUri = Uri.fromFile(file);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(cameraIntent, CAMRES);

		animMenuOff(
				buttonTopInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_off_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_off_y),
				buttonTopGaler, buttonTopCamera, true, true);
		animMenuOff(
				buttonButtomInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_y), buttonButtomGaler,
				buttonBottomCamera, true, true);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case Constants.GALLERY_RESULT_TOP:

			if (resultCode == getActivity().RESULT_OK) {

				intentBitmap(loadingImageTop = true, imageTop, data,
						Constants.IMGAE_BUTOM_LEFT);
			}

			break;
		case Constants.GALLERY_RESULT_BUTTOM:

			if (resultCode == getActivity().RESULT_OK) {
				intentBitmap(loadingImageButtom = true, imageBottom, data,
						Constants.IMGAE_BUTOM_RIGHT);

			}

			break;
		case Constants.CAMERA_RESULT_TOP:

			if (requestCode == Constants.CAMERA_RESULT_TOP) {
				if (data != null) {

				} else if (data == null) {

					Bitmap bitmap = BitmapFactory.decodeFile("mnt/sdcard/"
							+ Constants.FOLDER_PROGRAM + "/imageButomLeft.jpg");

					if (bitmap != null) {
						try {
							saveBitmapToSdcard((getResizedBitmap(bitmap)),
									"imageButomLeft");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					setImageUri(loadingImageTop = true, imageTop);

				}

			}
			break;
		case Constants.CAMERA_RESULT_BUTTOM:

			if (requestCode == Constants.CAMERA_RESULT_BUTTOM) {
				if (data != null) {

				} else if (data == null) {

					Bitmap bitmap = BitmapFactory
							.decodeFile("mnt/sdcard/"
									+ Constants.FOLDER_PROGRAM
									+ "/imageButomRight.jpg");

					if (bitmap != null) {
						try {
							saveBitmapToSdcard((getResizedBitmap(bitmap)),
									"imageButomRight");
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					setImageUri(loadingImageButtom = true, imageBottom);

				}

			}
		case Constants.URL_RESULT_TOP:

			if (requestCode == Constants.URL_RESULT_TOP) {
				String url = (String) data
						.getSerializableExtra(PhotoGaleeryFragment.EXTRA_URL);
				loadingImageTop = true;
				loadinInternetTop = true;
				imageLoaderFromUrl(url);

			}
			break;

		case Constants.URL_RESULT_BUTTOM:

			if (requestCode == Constants.URL_RESULT_BUTTOM) {

				String url = (String) data
						.getSerializableExtra(PhotoGaleeryFragment.EXTRA_URL);
				loadingImageButtom = true;
				loadingInternetButtom = true;
				imageLoaderFromUrl(url);

			}
			break;

		}
	}

	private void intentBitmap(Boolean loading, ImageView image, Intent data,
			String name) {

		Bitmap bitmapIntent = null;
		Uri selectedImageUri = data.getData();

		try {
			bitmapIntent = Media.getBitmap(getActivity().getContentResolver(),
					selectedImageUri);

			saveBitmapToSdcard((getResizedBitmap(bitmapIntent)), name);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		image.setBackgroundColor(Constants.COLORS);
		setPic(image, "mnt/sdcard/" + Constants.FOLDER_PROGRAM + "/" + name
				+ ".jpg");

		if (loadingImageTop == loadingImageButtom) {
			buttonNext.setVisibility(View.VISIBLE);

		}

	}

	private void setImageUri(Boolean loading, ImageView image) {

		Bitmap bitmapIntent = null;

		try {
			bitmapIntent = Media.getBitmap(getActivity().getContentResolver(),
					outputFileUri);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		if (bitmapIntent != null) {

			Bitmap btmNew = Bitmap
					.createScaledBitmap(
							bitmapIntent,
							(int) getResources().getDimension(
									R.dimen.image_1_layout_width),
							(int) getResources().getDimension(
									R.dimen.image_1_layout_height), false);

			image.setBackgroundColor(Constants.COLORS);
			image.setImageBitmap(btmNew);

			if (loadingImageTop == loadingImageButtom) {
				buttonNext.setVisibility(View.VISIBLE);

			}

		} else {

		}

		/*
		 * Bitmap btmNew = Bitmap .createScaledBitmap( bitmapIntent, (int)
		 * getResources().getDimension( R.dimen.image_1_layout_width), (int)
		 * getResources().getDimension( R.dimen.image_1_layout_height), false);
		 * 
		 * image.setBackgroundColor(Constants.COLORS);
		 * image.setImageBitmap(btmNew);
		 */

		/*
		 * if (loadingImageTop == loadingImageButtom) {
		 * buttonNext.setVisibility(View.VISIBLE);
		 * 
		 * }
		 */
	}

	private void saveBitmapToSdcard(Bitmap bitmap, String nameFile)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
		File dirToSave = new File(Environment.getExternalStorageDirectory()
				+ File.separator + Constants.FOLDER_PROGRAM);
		dirToSave.mkdir();
		File file = new File(dirToSave, nameFile + ".jpg");
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(baos.toByteArray());
		fos.flush();
		fos.close();
	}

	private void imageLoaderFromUrl(String url) {

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader.loadImage(url, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {

				if (loadinInternetTop == true) {
					imageTop.setBackgroundColor(Constants.COLORS);
					imageTop.setImageBitmap(loadedImage);
					loadinInternetTop = false;
					try {
						saveBitmapToSdcard(loadedImage,
								Constants.IMGAE_BUTOM_LEFT);
					} catch (IOException e) {

						e.printStackTrace();
					}
				} else {
					imageBottom.setBackgroundColor(Constants.COLORS);
					imageBottom.setImageBitmap(loadedImage);
					loadingInternetButtom = false;
					try {
						saveBitmapToSdcard(loadedImage,
								Constants.IMGAE_BUTOM_RIGHT);
					} catch (IOException e) {

						e.printStackTrace();
					}
				}

			}
		});

		if (loadingImageTop == loadingImageButtom) {
			buttonNext.setVisibility(View.VISIBLE);

		}
	}

	private void animateMenuImageTopOff() {
		animMenu(
				buttonTopInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_off_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_off_y));
		animMenu(
				buttonTopGaler,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_off_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_off_y));
		animMenu(
				buttonTopCamera,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_off_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_off_y));
	}

	private void animateMenuImageTopOn() {
		animMenu(
				buttonTopInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_internet_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_internet_y));
		animMenu(
				buttonTopGaler,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_galer_x), (int) getResources()
						.getDimension(R.dimen.animate_imagetop_galer_y));
		animMenu(
				buttonTopCamera,
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_camera_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagetop_camera_y));
	}

	private void animateMenuImageButtomOff() {
		animMenu(
				buttonButtomInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_y));
		animMenu(
				buttonButtomGaler,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_y));
		animMenu(
				buttonBottomCamera,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_off_y));
	}

	private void animateMenuImageButtomOn() {
		animMenu(
				buttonButtomInternet,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_internet_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_internet_y));
		animMenu(
				buttonButtomGaler,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_galer_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_galer_y));
		animMenu(
				buttonBottomCamera,
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_camera_x),
				(int) getResources().getDimension(
						R.dimen.animate_imagebuttom_camera_y));
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

	private void setPic(ImageView mImageView, String mCurrentPhotoPath) {

		int targetW = (int) getResources().getDimension(
				R.dimen.image_1_layout_width);
		int targetH = (int) getResources().getDimension(
				R.dimen.image_1_layout_height);

		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mImageView.setImageBitmap(bitmap);
	}

	public Bitmap getResizedBitmap(Bitmap bm) {

		int width = bm.getWidth();
		int height = bm.getHeight();

		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int newWidth = size.y;
		int newHeight = size.x;

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		Bitmap resizedBitmap = null;

		if (width < newWidth) {

			resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, null,
					false);
		} else {

			matrix.postScale(scaleWidth, scaleHeight);
			resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
					matrix, false);

		}
		return resizedBitmap;

	}

}
