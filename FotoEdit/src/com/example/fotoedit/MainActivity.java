package com.example.fotoedit;

import java.io.File;
import java.io.IOException;

import com.example.fotoedit.fragments.MainActivityFragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends SingleFragmentActivity {

	private static long back_pressed;

	@Override
	protected Fragment createFragment() {

		 deleteFiles("mnt/sdcard/" + Constants.FOLDER_PROGRAM);

		return new MainActivityFragment();
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
