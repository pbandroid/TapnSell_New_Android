package com.convertingoffers.tapnsell.TapboardFooter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.convertingoffers.tapnsell.R;
import com.convertingoffers.tapnsell.util.BaseActivity;
import com.convertingoffers.tapnsell.util.UserFunctions;

public class View_Tearm_Activity extends BaseActivity implements OnClickListener {
	
	Context context;
	String uri = "";
	WebView webViewTerms;
	ProgressBar progressBar;
	TextView tvUserName;
	/*private static String KEY_SUCCESS = "successfull";
	private static String SESSION_ID = "";
	private static int EBAY_USERID = 0x056;
	private static String USER_ID = "";*/

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" }) @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_term);
		
		ivBack.setOnClickListener(this);
		tvHeader.setText("View Terms");
		
		webViewTerms.getSettings().setJavaScriptEnabled(true);
		webViewTerms.setWebViewClient(new MyWebViewClient());
		webViewTerms.loadUrl(uri);
		webViewTerms.addJavascriptInterface(new JavaScriptInterface(), "HtmlViewer");
		
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		uri=UserFunctions.LocalViewTermsUrl;
		context=this;
		webViewTerms=(WebView)findViewById(R.id.webViewTerms);
		 progressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBack:
			finish();
			break;
		
		default:
			break;
		}
	}
	
	class JavaScriptInterface {
		public void showHTML(String html) {
			if (html.contains("userid")) {
				try {
					Document doc = Jsoup.parse(html);
					Elements ele = doc.select("div[class=mbg]");
					Elements span = ele.select("span[class=mbg-l]");
					Elements mbgfb = span.select("a[class=mbg-fb]");
					String useridurl = mbgfb.attr("href");
					Log.e("Element",
							useridurl.substring(useridurl.indexOf("=") + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class MyWebViewClient extends WebViewClient {

		String LOG_TAG = "Login";

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(LOG_TAG, "onPageStarted " + url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.d(LOG_TAG, "onPageFinished " + url);
			progressBar.setVisibility(View.GONE);
			
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

		/*	if (url.contains("username")) {
				String[] args = url.split("&");
				String usrnmarg = args[(args.length) - 1];
				String[] usrnm = usrnmarg.split("=");
				Log.e("shouldoverride", "username  " + usrnm[1]);
				isUrlMyEbay = true;
			}*/
			view.loadUrl(url);
			return true;
		}
	}
	
	
	
	 
	   
}