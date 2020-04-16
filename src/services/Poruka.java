package services;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class Poruka {

	private String id;
	private String namePoster;
	private String sender;
	private String title;
	private String content;
	private String dateAndTime;
	private String primaoc;
	
	public Poruka() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Poruka(String id,String namePoster, String sender, String title, String content, String dateAndTime,String primaoc) {
		super();
		this.id=id;
		this.namePoster = namePoster;
		this.sender = sender;
		this.title = title;
		this.content = content;
		this.dateAndTime = dateAndTime;
		this.primaoc=primaoc;
	}

	public String getNamePoster() {
		return namePoster;
	}

	public void setNamePoster(String namePoster) {
		this.namePoster = namePoster;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrimaoc() {
		return primaoc;
	}

	public void setPrimaoc(String primaoc) {
		this.primaoc = primaoc;
	}
	
}
