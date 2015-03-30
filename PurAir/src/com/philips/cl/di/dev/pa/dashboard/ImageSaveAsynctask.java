package com.philips.cl.di.dev.pa.dashboard;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.ImageSaveListener;

public class ImageSaveAsynctask extends AsyncTask<Bitmap, Void, Void> {
	
	ImageSaveListener mimagesaveListnear;
	
	
	public ImageSaveAsynctask(ImageSaveListener imagesave) {
		
		mimagesaveListnear = imagesave;
	}

	@Override
	protected Void doInBackground(Bitmap... bitmap) {
		
		saveImageToSD(bitmap[0]);
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mimagesaveListnear.onImagesave();
	}

	private void saveImageToSD(Bitmap bitmap) {

		if (!DashboardUtil.isHaveSDCard()) {
			return;
		}
		if (null == bitmap) {
			return;
		}
		if (DashboardUtil.SAVE_FREESPACE_BYTE > DashboardUtil.freeSpaceOnSd_BYTE()) {
			return;
		}
		File dir = new File(AppConstants.CACHEDIR_IMG);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File imageFile = new File(AppConstants.CACHEDIR_IMG + AppConstants.SHARE_DASHBOARD);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imageFile));
			if (bitmap != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	


}
