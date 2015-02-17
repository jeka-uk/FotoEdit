package com.example.fotoedit.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.fotoedit.Constants;
import com.example.multiexpo.R;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ColorFilterFragment extends Fragment {

	private ImageView buttonNext, imageMain, imageButtomGrey, imageButtomRed,
			imageButtomGreen, imageBottomYllow, ramkagrey, ramkared,
			ramkagreen, ramkayellow;
	private Bitmap bitmapImageLeft, bitmapImageRight, bitmapTwitter;
	private int alpha;
	private Uri picUri;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_filtr, container, false);

		Bundle bundle = getArguments();
		if (bundle != null) {
			Integer recieveInfo = bundle.getInt("alpha");
			alpha = bundle.getInt("alpha");

		}

		ramkagrey = (ImageView) view.findViewById(R.id.ramkagrey);
		ramkared = (ImageView) view.findViewById(R.id.ramkared);
		ramkagreen = (ImageView) view.findViewById(R.id.ramkagreen);
		ramkayellow = (ImageView) view.findViewById(R.id.ramkayellow);
		buttonNext = (ImageView) view.findViewById(R.id.nextfiltr);
		imageMain = (ImageView) view.findViewById(R.id.imageeditcolorfiltrLeft);
		imageButtomGrey = (ImageView) view.findViewById(R.id.imagefiltrgrey);
		imageButtomRed = (ImageView) view.findViewById(R.id.imagefiltrred);
		imageButtomGreen = (ImageView) view.findViewById(R.id.imagefiltrgreen);
		imageBottomYllow = (ImageView) view.findViewById(R.id.imagefiltryellow);

		buttonNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startFragment(new SaveImageFragment(), Constants.NEXT);

			}
		});

		bitmapImageLeft = BitmapFactory.decodeFile("mnt/sdcard/"
				+ Constants.FOLDER_PROGRAM + "/imageEditLeft.jpg");
		bitmapImageRight = BitmapFactory.decodeFile("mnt/sdcard/"
				+ Constants.FOLDER_PROGRAM + "/imageEditRight.jpg");

		Bitmap mainBitmp = editBitmap(bitmapImageLeft, bitmapImageRight);

		imageMain.setImageBitmap(mainBitmp);

		imageButtomGrey.setImageBitmap(colorFiltr(
				editBitmap(bitmapImageLeft, bitmapImageRight), 0, 0));
		imageButtomRed.setImageBitmap(colorFiltr(
				editBitmap(bitmapImageLeft, bitmapImageRight), 0, Color.RED));
		imageButtomGreen.setImageBitmap(colorFiltr(
				editBitmap(bitmapImageLeft, bitmapImageRight), 0, Color.GREEN));
		imageBottomYllow
				.setImageBitmap(colorFiltr(
						editBitmap(bitmapImageLeft, bitmapImageRight), 0,
						Color.YELLOW));

		imageButtomGrey.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageButtomGrey, 0.9f);
					scaleAnim(ramkagrey, 0.9f);

					imageMain
							.setImageBitmap(colorFiltr(
									editBitmap(bitmapImageLeft,
											bitmapImageRight), 0, 0));

					try {
						saveBitmapToSdcard(
								(colorFiltr(
										editBitmap(bitmapImageLeft,
												bitmapImageRight), 0, 0)),
								Constants.IMAGE_RESULT);
					} catch (IOException e) {

					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageButtomGrey, 1f);
					scaleAnim(ramkagrey, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		imageButtomRed.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageButtomRed, 0.9f);
					scaleAnim(ramkared, 0.9f);

					imageMain.setImageBitmap(colorFiltr(
							editBitmap(bitmapImageLeft, bitmapImageRight), 0,
							Color.RED));

					try {
						saveBitmapToSdcard(
								(colorFiltr(
										editBitmap(bitmapImageLeft,
												bitmapImageRight), 0, Color.RED)),
								Constants.IMAGE_RESULT);
					} catch (IOException e) {

					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageButtomRed, 1f);
					scaleAnim(ramkared, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		imageButtomGreen.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageButtomGreen, 0.9f);
					scaleAnim(ramkagreen, 0.9f);

					imageMain.setImageBitmap(colorFiltr(
							editBitmap(bitmapImageLeft, bitmapImageRight), 0,
							Color.GREEN));

					try {
						saveBitmapToSdcard(
								(colorFiltr(
										editBitmap(bitmapImageLeft,
												bitmapImageRight), 0,
										Color.GREEN)), Constants.IMAGE_RESULT);
					} catch (IOException e) {

					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageButtomGreen, 1f);
					scaleAnim(ramkagreen, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		imageBottomYllow.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {

					scaleAnim(imageBottomYllow, 0.9f);
					scaleAnim(ramkayellow, 0.9f);

					imageMain.setImageBitmap(colorFiltr(
							editBitmap(bitmapImageLeft, bitmapImageRight), 0,
							Color.YELLOW));

					try {
						saveBitmapToSdcard(
								(colorFiltr(
										editBitmap(bitmapImageLeft,
												bitmapImageRight), 0,
										Color.YELLOW)), Constants.IMAGE_RESULT);
					} catch (IOException e) {

					}

				} else if (event.getAction() == MotionEvent.ACTION_UP) {

					scaleAnim(imageBottomYllow, 1f);
					scaleAnim(ramkayellow, 1f);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

				}

				return true;
			}
		});

		return view;

	}

	public void startFragment(Fragment fragment, int REQUEST) {
		fragment.setTargetFragment(ColorFilterFragment.this, REQUEST);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.add(R.id.container, fragment);
		ft.addToBackStack(null);
		ft.commit();

	}

	private Bitmap editBitmap(Bitmap imageLeft, Bitmap imageRight) {

		Bitmap newBitmap = null;
		int height, width, imageleftX = 0, imageLeftY = 0, imageRigthX = 0, imageRigthY = 0;

		if (imageLeft.getWidth() >= imageRight.getWidth()) {

			width = imageLeft.getWidth();
		} else {
			width = imageRight.getWidth();
		}

		if (imageLeft.getHeight() >= imageRight.getHeight()) {

			height = imageLeft.getHeight();
		} else {
			height = imageRight.getHeight();
		}

		if (imageLeft.getWidth() < width) {

			imageleftX = (width - imageLeft.getWidth()) / 2;

		} else {
			imageleftX = 0;
		}

		if (imageLeft.getHeight() < height) {

			imageLeftY = (height - imageLeft.getHeight()) / 2;

		} else {
			imageLeftY = 0;
		}

		if (imageRight.getWidth() < width) {

			imageRigthX = (width - imageRight.getWidth()) / 2;

		} else {
			imageRigthX = 0;
		}

		if (imageRight.getHeight() < height) {

			imageRigthY = (height - imageRight.getHeight()) / 2;

		} else {
			imageRigthY = 0;
		}

		newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas newCanvas = new Canvas(newBitmap);
		newCanvas.drawBitmap(imageLeft, imageleftX, imageLeftY, null);

		Paint paint = new Paint();
		paint.setAlpha(alpha);
		newCanvas.drawBitmap(imageRight, imageRigthX, imageRigthY, paint);

		try {
			saveBitmapToSdcard(newBitmap, Constants.IMAGE_RESULT);
		} catch (IOException e) {

		}

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

	private Bitmap colorFiltr(Bitmap src, float settingSat, int color) {

		int width = src.getWidth();
		int height = src.getHeight();

		Bitmap bitmapResult = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvasResult = new Canvas(bitmapResult);

		if (color != 0) {

			Paint paint = new Paint(color);
			ColorFilter filter = new LightingColorFilter(color, 1);
			paint.setColorFilter(filter);
			canvasResult.drawBitmap(src, 0, 0, paint);

		} else {

			Paint paint = new Paint();
			ColorMatrix colorMatrix = new ColorMatrix();
			colorMatrix.setSaturation(settingSat);
			ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
					colorMatrix);
			paint.setColorFilter(filter);
			canvasResult.drawBitmap(src, 0, 0, paint);
		}

		return bitmapResult;
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

}