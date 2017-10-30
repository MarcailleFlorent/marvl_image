
public class ComparedImage {

	private String imageName;
	private Float imageDistance;
	
	
	public ComparedImage(String imageName, Float imageDistance) {
		this.imageName = imageName;
		this.imageDistance = imageDistance;
	}


	public String getImageName() {
		return imageName;
	}

	public Float getImageDistance() {
		return imageDistance;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImageDistance(Float imageDistance) {
		this.imageDistance = imageDistance;
	}
	
	
}
