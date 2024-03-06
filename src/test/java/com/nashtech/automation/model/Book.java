package com.nashtech.automation.model;

public class Book {
	public String isbn;
	public String title;
	public String subTitle;
	public String author;
	public String publish_date;
	
	
	
	public Book(String isbn, String title, String subTitle, String author, String publish_date) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.subTitle = subTitle;
		this.author = author;
		this.publish_date = publish_date;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublish_date() {
		return publish_date;
	}
	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}
	
	
	
	
	
	
}
