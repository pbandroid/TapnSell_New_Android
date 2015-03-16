package com.convertingoffers.tapnsell.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;

import com.convertingoffers.tapnsell.R;

public class ImageLoaderTopBottamRounded {
	  Bitmap r_bmp1;
	Bitmap icon;
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoaderTopBottamRounded(Context context) {
		fileCache = new FileCache(context);
	//	Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_launcher);
		executorService = Executors.newFixedThreadPool(5);
	}

	final int stub_id = R.drawable.ic_launcher;

	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			//imageView.setImageBitmap(bitmap);
		  Bitmap r_bmp =getRoundedCornerBitmap(bitmap);
		    imageView.setImageBitmap(r_bmp);
		}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        bitmap.getHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(output);
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    Shader shader = new BitmapShader(output, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
    paint.setAntiAlias(true);
    paint.setShader(shader);
    RectF rec = new RectF(0, 0, 50, 50);
//  you may need this for only top round corner
//  RectF rec = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()-20);
//  c.drawRect(new RectF(0, 20, getMeasuredWidth(), getMeasuredHeight()),      paint);
    canvas.drawRoundRect(rec, 20, 20, paint);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);
    
  /*  final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    final RectF rectF = new RectF(rect);
    final float roundPx = 10;
 
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
 
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);*/
 
    return output;
  }


	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			UtilsImage.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 120;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			
			bmp=getRoundedCornerBitmap(bmp);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null){
				
				//r_bmp1=getRoundedCornerBitmap(bitmap);
				photoToLoad.imageView.setImageBitmap(bitmap);
			}
			else{
				
			}
				//photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}