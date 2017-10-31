package image;
import tools.GlobalVariables;

public class ComparedImage {

	private String imageName;
	private Float imageDistance;
	private String imageClass;
	
	public ComparedImage(String imageName, Float imageDistance) {
		this.imageName = imageName;
		this.imageDistance = imageDistance;
		
		String clearImgClass = imageName.replaceAll(GlobalVariables.ImageBankPath, "");
		this.imageClass = clearImgClass.substring(0,clearImgClass.indexOf("_"));
	}


	public String getImageName() {
		return imageName;
	}

	public Float getImageDistance() {
		return imageDistance;
	}
	
	public String getImageClass() {
		return imageClass;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public void setImageDistance(Float imageDistance) {
		this.imageDistance = imageDistance;
	}
	
	
}
