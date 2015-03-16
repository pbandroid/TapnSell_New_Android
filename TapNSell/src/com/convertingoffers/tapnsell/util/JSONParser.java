package com.convertingoffers.tapnsell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	
	
	    // constructor
	    public JSONParser() {
	 
	    }
	 
	    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
	    	  InputStream is = null;
	 	     JSONObject jObj = null;
	 	     String json = "";
	 	 
	        try {
	            // defaultHttpClient
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	            httpPost.setEntity(new UrlEncodedFormEntity(params));
	 
	            HttpResponse httpResponse = httpClient.execute(httpPost);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	 
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "iso-8859-1"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            is.close();
	            json = sb.toString();
	            Log.e("JSON FROM SERVER", json);
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	 
	        // try parse the string to a JSON object
	        try {
	            jObj = new JSONObject(json);            
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	 
	        return jObj;
	 
	    }

	    public JSONObject getMethodUrl(String url) {
	    	  InputStream is = null;
		 	     JSONObject jObj = null;
		 	     String json = "";
		 	   
		 	     HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		 	   DefaultHttpClient client = new DefaultHttpClient();
		 	   SchemeRegistry registry = new SchemeRegistry();
		 	   SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		 	   socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		 	   registry.register(new Scheme("https", socketFactory, 443));
		 	   SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		 	   DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
		 	   HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	        try {
	            HttpGet httpget = new HttpGet(url);
	            HttpResponse httpResponse = httpClient.execute(httpget);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	 
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	 
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    is, "UTF-8"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            is.close();
	            json = sb.toString();
	          
	            json = json.replaceAll("\\\\n", "");
	            Log.e("JSON FROM SERVER", json);
	        } catch (Exception e) {
	            Log.e("Buffer Error", "Error converting result " + e.toString());
	        }
	 
	        // try parse the string to a JSON object
	        try {
	            jObj = new JSONObject(json);            
	        } catch (JSONException e) {
	            Log.e("JSON Parser", "Error parsing data " + e.toString());
	        }
	 
	        // return JSON String
	        return jObj;
	 
	    }
}
