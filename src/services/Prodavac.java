package services;

import java.util.HashMap;

public class Prodavac extends Korisnik {
	private HashMap<String, Oglas> broadcastedPosts;
	private HashMap<String, Oglas> deliveredPosts;
	private HashMap<String, Poruka> messages;
	private int numLike;
	private int numDislike;
	
	public Prodavac() {
		super();
		this.deliveredPosts=new HashMap<String, Oglas>();
		this.broadcastedPosts=new HashMap<String, Oglas>();
		this.messages=new HashMap<String, Poruka>();
		// TODO Auto-generated constructor stub
	}

	public Prodavac(HashMap<String, Oglas> broadcastedPosts, HashMap<String, Oglas> deliveredPosts,
			HashMap<String, Poruka> messages, int numLike, int numDislike) {
		super();
		this.broadcastedPosts = broadcastedPosts;
		this.deliveredPosts = deliveredPosts;
		this.messages = messages;
		this.numLike = numLike;
		this.numDislike = numDislike;
	}

	public HashMap<String, Oglas> getBroadcastedPosts() {
		return broadcastedPosts;
	}

	public void setBroadcastedPosts(HashMap<String, Oglas> broadcastedPosts) {
		this.broadcastedPosts = broadcastedPosts;
	}

	public HashMap<String, Oglas> getDeliveredPosts() {
		return deliveredPosts;
	}

	public void setDeliveredPosts(HashMap<String, Oglas> deliveredPosts) {
		this.deliveredPosts = deliveredPosts;
	}

	public HashMap<String, Poruka> getMessages() {
		return messages;
	}

	public void setMessages(HashMap<String, Poruka> messages) {
		this.messages = messages;
	}

	public int getNumLike() {
		return numLike;
	}

	public void setNumLike(int numLike) {
		this.numLike = numLike;
	}

	public int getNumDislike() {
		return numDislike;
	}

	public void setNumDislike(int numDislike) {
		this.numDislike = numDislike;
	}
	
	
}
