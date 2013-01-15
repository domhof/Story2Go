package at.ac.tuwien.igw.story2go.model;

public class Story {
	private String name;
	private String description;
	private int image;
	private int imageHeight;

	public Story(String name, String description, int image, int imageHeight) {
		this.name = name;
		this.description = description;
		this.imageHeight = imageHeight;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}
