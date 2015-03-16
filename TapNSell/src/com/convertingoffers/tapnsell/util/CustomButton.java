package com.convertingoffers.tapnsell.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.convertingoffers.tapnsell.R;

public class CustomButton extends Button{

	 public CustomButton(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init(context, attrs);
	    }
	    @SuppressLint("Recycle") public void init(Context context, AttributeSet attrs) {
	        TypedArray a = context.obtainStyledAttributes(attrs,
	                R.styleable.customFont);
	        String fontFamily = null;
	        final int n = a.getIndexCount();
	        for (int i = 0; i < n; ++i) {
	            int attr = a.getIndex(i);
	            if (attr == R.styleable.customFont_fontFamily) {
	                fontFamily = a.getString(attr);
	            }
	            a.recycle();
	        }
	        if (!isInEditMode()) {
	            try {
	                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/"+fontFamily);
	                setTypeface(tf);
	            } catch (Exception e) {
	            }
	        }
	    }
}
