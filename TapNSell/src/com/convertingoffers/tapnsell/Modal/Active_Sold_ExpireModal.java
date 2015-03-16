package com.convertingoffers.tapnsell.Modal;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Active_Sold_ExpireModal implements Serializable {

	// getter setter for store contact details display
	
	ArrayList<ActiveModal> mActiveList = new ArrayList<ActiveModal>();
	ArrayList<SoldModal> mSoldList = new ArrayList<SoldModal>();
	ArrayList<ExpireModal> mExpireList = new ArrayList<ExpireModal>();
	
	public Active_Sold_ExpireModal(ArrayList<ActiveModal> mActiveList,
			ArrayList<SoldModal> mSoldList, ArrayList<ExpireModal> mExpireList) {
		super();
		this.mActiveList = mActiveList;
		this.mSoldList = mSoldList;
		this.mExpireList = mExpireList;
	}
	
	public ArrayList<ActiveModal> getmActiveList() {
		return mActiveList;
	}
	public void setmActiveList(ArrayList<ActiveModal> mActiveList) {
		this.mActiveList = mActiveList;
	}
	public ArrayList<SoldModal> getmSoldList() {
		return mSoldList;
	}
	public void setmSoldList(ArrayList<SoldModal> mSoldList) {
		this.mSoldList = mSoldList;
	}
	public ArrayList<ExpireModal> getmExpireList() {
		return mExpireList;
	}
	public void setmExpireList(ArrayList<ExpireModal> mExpireList) {
		this.mExpireList = mExpireList;
	}
	public Active_Sold_ExpireModal() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
}
