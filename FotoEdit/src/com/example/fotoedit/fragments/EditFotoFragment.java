package com.example.fotoedit.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.fotoedit.Constants;
import com.example.multiexpo.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EditFotoFragment extends Fragment {

	private ImageView imageMainEdit, imageMainEditTopLayer, imageButtomLeft,
			imageButtomRight, imageButtonRotationLeft,
			imageButtonRotationRight, imageButtonCropLeft,
			imageButtonCropRight, buttonNext, ramkaLeftButtom,
			ramkaRightButtom;
	private SeekBar seekBar;
	private Bitmap bitmapImageLeft, bitmapImageRight, bitmapImageMainTopLayer,
			imageEditLeft, imageEditRight;
	private Boolean animMenuOnButtom = true, animMenuOnTop = true;
	private Uri croppedOutputUri, picUri;
	private boolean editImageLeft = false, editImageRight = false;
	private int alpha;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);

		buttonNext = (ImageView) view.findViewById(R.id.nextedit);

		ramkaLeftButtom = (ImageView) view.findViewById(R.id.ramkaLeftButtom);
		ramkaRightButtom = (ImageView) view.findViewById(R.id.ramkaRightButtom);
		imageButtomLeft = (ImageView) view.findViewById(R.id.imagbuttomleft);
		imageButtomRight = (ImageView) view.findViewById(R.id.imagbuttomright);
		imageButtonCropLeft = (ImageView) view.findViewById(R.id.cropp);
		imageButtonRotationLeft = (ImageView) view.findViewById(R.id.rotate);
		imageButtonCropRight = (ImageView) view
				.findViewById(R.id.croppimageridht);
		imageButtonRotationRight = (ImageView) view
				.findViewById(R.id.rotateimageright);
		imageMainEdit = (ImageView) view.findViewById(R.id.imageedit);
		imageMainEditTopLayer = (ImageView) view
				.findViewById(R.id.imagemainedittoplayer);
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);

		bitmapImageLeft = BitmapFactory.decodeFile("mnt/sdcard/"
				+ Constants.FOLDER_PROGRAM + "/imageButomLeft.jpg");
		bitmapImageRight = BitmapFactory.decodeFile("mnt/sdcard/"
				+ Constants.FOLDER_PROGRAM + "/imageButomRight.jpg");
		bitmapImageMainTopLayer = BitmapFactory.decodeFile("mnt/sdcard/"
				+ Constants.FOLDER_PROGRAM + "/imageButomRight.jpg");

		try {
			saveBitmapToSdcard(bitmapImageLeft, Constants.IMGAE_EDIT_LEFT);
			saveBitmapToSdcard(bitmapImageMainTopLayer,
					Constants.IMGAE_EDIT_RIGHT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		setPic(imageButtomLeft,
				"mnt/sdcard/" + Constants.FOLDER_PROGRAM + "/"
						+ Constants.IMGAE_BUTOM_LEFT + ".jpg",
				(int) getResources().getDimension(
						R.dimen.imagbuttomleft_layout_width),
				(int) getResources().getDimension(
						R.dimen.imagbuttomleft_layout_height));

		setPic(imageButtomRight,
				"mnt/sdcard/" + Constants.FOLDER_PROGRAM + "/"
						+ Constants.IMGAE_BUTOM_RIGHT + ".jpg",
				(int) getResources().getDimension(
						R.dimen.imagbuttomleft_layout_width),
				(int) getResources().getDimension(
						R.dimen.imagbuttomleft_layout_height));

		setPic(imageMainEdit,
				"mnt/sdcard/" + Constants.FOLDER_PROGRAM + "/"
						+ Constants.IMGAE_BUTOM_LEFT + ".jpg",
				(int) getResources().getDimension(
						R.dimen.imageedit_layout_width),
				(int) getResources().getDimension(
						R.dimen.imageedit_layout_height));

		imageMainEditTopLayer.setImageBitmap(bitmapImageMainTopLayer);

		alpha = seekBar.getProgress();
		imageMainEditTopLayer.setAlpha(alpha);

		seekBar.setOnSeekBarChangeListener(seekBarOnSeekBarChangeListener);

		buttonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragment(new ColorFilterFragment(), Constants.NEXT);

			}
		});

		imageButtomLeft.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageButtomLeft, 0.9f);
					scaleAnim(ramkaLeftButtom, 0.9f);

					if (animMenuOnButtom == true) {
						if (animMenuOnTop == true) {
							imageButtomLeftOn();
							animMenuOnTop = false;
						} else {
							imageButtomLeftOff();
							animMenuOnTop = true;
						}
					} else {
						imageButtomRightOff();
						animMenuOnButtom = true;
						imageButtomLeftOn();
						animMenuOnTop = false;

					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageButtomLeft, 1f);
					scaleAnim(ramkaLeftButtom, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		imageButtonRotationLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bitmapImageLeft = editBitmapRotat(-90, bitmapImageLeft);
				imageMainEdit.setImageBitmap(bitmapImageLeft);
				editImageLeft = true;

				if (bitmapImageLeft != null) {
					try {
						saveBitmapToSdcard(bitmapImageLeft,
								Constants.IMGAE_EDIT_LEFT);
					} catch (IOException e) {

						e.printStackTrace();
					}

				}

				imageButtomLeftOff();

				if (editImageLeft == true) {
					buttonNext.setVisibility(View.VISIBLE);

				}

			}
		});

		imageButtomRight.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageButtomRight, 0.9f);
					scaleAnim(ramkaRightButtom, 0.9f);

					if (animMenuOnTop == true) {
						if (animMenuOnButtom == true) {
							imageButtomRightOn();
							animMenuOnButtom = false;
						} else {
							imageButtomRightOff();
							animMenuOnButtom = true;
						}
					} else {
						imageButtomLeftOff();
						animMenuOnTop = true;
						imageButtomRightOn();
						animMenuOnButtom = false;
					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageButtomRight, 1f);
					scaleAnim(ramkaRightButtom, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		imageButtonRotationRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				bitmapImageMainTopLayer = editBitmapRotat(90,
						bitmapImageMainTopLayer);
				imageMainEditTopLayer.setImageBitmap(bitmapImageMainTopLayer);
				editImageRight = true;

				if (bitmapImageMainTopLayer != null) {
					try {
						saveBitmapToSdcard(bitmapImageMainTopLayer,
								Constants.IMGAE_EDIT_RIGHT);
					} catch (IOException e) {

						e.printStackTrace();
					}
				} else {

				}

				imageButtomRightOff();

				if (editImageRight == true) {
					buttonNext.setVisibility(View.VISIBLE);

				}
			}

		});

		imageButtonCropLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (editImageLeft == true) {

					imageEditLeft = BitmapFactory.decodeFile("mnt/sdcard/"
							+ Constants.FOLDER_PROGRAM + "/imageEditLeft.jpg");

					performCrop("file://mnt/sdcard/" + Constants.FOLDER_PROGRAM
							+ "/imageEditLeft.jpg", Constants.IMGAE_EDIT_LEFT,
							Constants.CROP_IMAGE_REQUEST_LEFT);

				} else

					performCrop("file://mnt/sdcard/" + Constants.FOLDER_PROGRAM
							+ "/imageButomLeft.jpg", Constants.IMGAE_EDIT_LEFT,
							Constants.CROP_IMAGE_REQUEST_LEFT);
				editImageLeft = true;

				imageButtomLeftOff();

			}
		});

		imageButtonCropRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (editImageRight == true) {

					imageEditRight = BitmapFactory.decodeFile("mnt/sdcard/"
							+ Constants.FOLDER_PROGRAM + "/imageEditRight.jpg");

					performCrop("file://mnt/sdcard/" + Constants.FOLDER_PROGRAM
							+ "/imageEditRight.jpg",
							Constants.IMGAE_EDIT_RIGHT,
							Constants.CROP_IMAGE_REQUEST_RIGHT);
				} else

					performCrop("file://mnt/sdcard/" + Constants.FOLDER_PROGRAM
							+ "/imageButomRight.jpg",
							Constants.IMGAE_EDIT_RIGHT,
							Constants.CROP_IMAGE_REQUEST_RIGHT);
				editImageRight = true;
				imageButtomRightOff();

			}
		});

		return view;
	}

	OnSeekBarChangeListener seekBarOnSeekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

			alpha = seekBar.getProgress();

			buttonNext.setVisibility(View.VISIBLE);

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			int alpha = seekBar.getProgress();
			imageMainEditTopLayer.setAlpha(alpha);

			imageButtomRightOff();
			imageButtomLeftOff();

		}
	};

	private Bitmap editBitmapRotat(int angle, Bitmap bitmap) {

		Bitmap newBitmap = null;

		Matrix matrix = new Matrix();
		matrix.preRotate(angle);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawBitmap(bitmap, 0, 0, null);

		return newBitmap;
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

	private void imageButtomLeftOn() {
		animMenu(
				imageButtonCropLeft,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropLeft_On_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropLeft_On_y));
		animMenu(
				imageButtonRotationLeft,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationLeft_On_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationLeft_On_y));

	}

	private void imageButtomLeftOff() {
		animMenu(
				imageButtonCropLeft,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropLeft_Off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropLeft_Off_y));
		animMenu(
				imageButtonRotationLeft,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationLeft_Off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationLeft_Off_y));
	}

	private void imageButtomRightOn() {
		animMenu(
				imageButtonCropRight,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropRight_On_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropRight_On_y));
		animMenu(
				imageButtonRotationRight,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationRight_On_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationRight_On_y));

	}

	private void imageButtomRightOff() {
		animMenu(
				imageButtonCropRight,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropRight_Off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonCropRight_Off_y));
		animMenu(
				imageButtonRotationRight,
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationRight_Off_x),
				(int) getResources().getDimension(
						R.dimen.animate_imageButtonRotationRight_Off_y));

	}

	public void startFragment(Fragment fragment, int REQUEST) {
		fragment.setTargetFragment(EditFotoFragment.this, REQUEST);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		Bundle bundle = new Bundle();
		bundle.putInt("alpha", alpha);
		fragment.setArguments(bundle);
		ft.add(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();

	}

	private void performCrop(String nameFileOpenCrop, String nameFileSaveCrop,
			int CROP_IMAGE) {
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int newHeight = size.y;
		int newWidth = size.x;

		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		cropIntent.setDataAndType((Uri.parse(nameFileOpenCrop)), "image/*");
		cropIntent.putExtra("crop", "true");
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		cropIntent.putExtra("outputX", Constants.sizeImageHigth);
		cropIntent.putExtra("outputY", Constants.sizeImageWight);
		cropIntent.putExtra("output", picUri);
		cropIntent.putExtra("outputFormat", "JPEG");
		File dirToSave = new File(Environment.getExternalStorageDirectory()
				+ File.separator + Constants.FOLDER_PROGRAM);
		dirToSave.mkdir();
		File file = new File(dirToSave, nameFileSaveCrop + ".jpg");
		picUri = Uri.fromFile(file);
		cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);

		startActivityForResult(cropIntent, CROP_IMAGE);

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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {

		case Constants.CROP_IMAGE_REQUEST_LEFT:

			if (requestCode == Constants.CROP_IMAGE_REQUEST_LEFT) {
				imageMainEdit.setImageURI(picUri);

				if (editImageLeft == true) {
					buttonNext.setVisibility(View.VISIBLE);

				}

			}

			break;
		case Constants.CROP_IMAGE_REQUEST_RIGHT:

			if (requestCode == Constants.CROP_IMAGE_REQUEST_RIGHT) {
				imageMainEditTopLayer.setImageURI(picUri);

				if (editImageLeft == true) {
					buttonNext.setVisibility(View.VISIBLE);

				}

			}

		}

	}

	private void setPic(ImageView mImageView, String mCurrentPhotoPath,
			int targetW, int targetH) {

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

}