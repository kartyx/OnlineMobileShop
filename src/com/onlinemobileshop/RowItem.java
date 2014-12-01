package com.onlinemobileshop;

public class RowItem {

	 private String name;
	 private int image;

	 public RowItem(String name,int image)
	 {
		 this.name=name;
		 this.image=image;
	 }
	 public String getName() {
	  return name;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }

	 public int getPicId() {
	  return image;
	 }

	 public void setPicId(int image) {
	  this.image = image;
	 }
	

	}
