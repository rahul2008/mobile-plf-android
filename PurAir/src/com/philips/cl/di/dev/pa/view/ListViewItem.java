package com.philips.cl.di.dev.pa.view;

public class ListViewItem {
	private int textId;
	private int imageId;
	
	public ListViewItem(int textId, int imageId) {
		this.textId = textId;
		this.imageId = imageId;
	}

	public int getTextId() {
		return textId;
	}

	public void setTextId(int textId) {
		this.textId = textId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
