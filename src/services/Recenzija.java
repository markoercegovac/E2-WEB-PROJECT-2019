package services;

public class Recenzija {

	private String id;
	private String name;
	private String recesent; //kupac koji je napisao recenziju
	private String title;
	private String content;
	private String image;
	private boolean correct;
	private boolean agreed;
	
	public Recenzija() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Recenzija(String id,String name, String recesent, String title, String content, String image, boolean correct,
			boolean agreed) {
		super();
		this.id=id;
		this.name = name;
		this.recesent = recesent;
		this.title = title;
		this.content = content;
		this.image = image;
		this.correct = correct;
		this.agreed = agreed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecesent() {
		return recesent;
	}

	public void setRecesent(String recesent) {
		this.recesent = recesent;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public boolean isAgreed() {
		return agreed;
	}

	public void setAgreed(boolean agreed) {
		this.agreed = agreed;
	}
	
	
	
	
	
}
