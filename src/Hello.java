import static org.bytedeco.javacpp.opencv_core.NORM_L2;
import static org.bytedeco.javacpp.opencv_features2d.drawMatches;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core.DMatchVector;
import org.bytedeco.javacpp.opencv_core.KeyPointVector;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_shape;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

import tools.GlobalTools;
import tools.MatchingTools;
import tools.SiftTools;


public class Hello {
	
	/**
	* 	Main
	*	@param	args
	*/
	public	static	void	main(String[]	args)	{			
		System.out.println("Best image : ");
		
		// Calculate and display closest image compared to Coca_1
		String bestComparedImg = getMostSimilitudeImageComparedToDataBank("Coca_1.jpg", "ImageBank/TestImage/");
		displayComparisonBetweenTwoImage("Coca_1.jpg", bestComparedImg);
		
		System.out.println("Top 5 image now :");
		
		// Calculate and display 5 closest image compared to Coca_1
		ArrayList<ComparedImage> best5ComparedImg = getOrderedNbOfMostSimilitudeComparedToDataBank("Coca_1.jpg", "ImageBank/TestImage/", 5);
		for(int i=0; i < best5ComparedImg.size(); i++) {
			System.out.println("N°"+(i+1)+" :"+best5ComparedImg.get(i).getImageName() + " | Distance = "+best5ComparedImg.get(i).getImageDistance());
			displayComparisonBetweenTwoImage("Coca_1.jpg", best5ComparedImg.get(i).getImageName());
		}
		
	}
	
	
	public static void displayComparisonBetweenTwoImage(String pathToImg1, String pathToImg2) {
		Mat[] images = new Mat[2];
		KeyPointVector keyPoints1 = new KeyPointVector();
		KeyPointVector keyPoints2 = new KeyPointVector();
				
		SIFT sift;
		
		BFMatcher matcher = new BFMatcher(NORM_L2,false);
		DMatchVector matches = new DMatchVector(); //DMatchVectorVector matchesKnn = new DMatchVectorVector();
				
		images[0] = GlobalTools.loadImg(pathToImg1);
		images[1] = GlobalTools.loadImg(pathToImg2);
				
		Loader.load(opencv_calib3d.class);
		Loader.load(opencv_shape.class);
				
		sift = SIFT.create(SiftTools.nFeatures,SiftTools.nOctaveLayers,SiftTools.contrastThreshold,SiftTools.edgeThresold,SiftTools.sigma);

		sift.detect(images[0], keyPoints1);
		sift.detect(images[1], keyPoints2);
		
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();
		
		sift.compute(images[0], keyPoints1, descriptors1);
		sift.compute(images[1], keyPoints2, descriptors2);
		
		// Match descriptors | matcher.knnMatch(descriptors1,descriptors2,matchesKnn,5);
		matcher.match(descriptors1, descriptors2, matches);
						
		DMatchVector bestMatches = MatchingTools.selectBestMatcher(matches, 25);
		
		// Draw Matches
		Mat imageMatches = new Mat();
		drawMatches(images[0], keyPoints1, images[1], keyPoints2, bestMatches, imageMatches);
		
		GlobalTools.displayImg("Comparison", imageMatches);
		
		matcher.close();
	}
	
	public static String getMostSimilitudeImageComparedToDataBank(String pathToImg, String pathToDataBank) {
		HashMap<String, Float> imgValueMap = generateMapSimilitudeOfImageComparedToDataBank(pathToImg, pathToDataBank);
		Set<String> imgSet = imgValueMap.keySet();
		Iterator<String> i = imgSet.iterator();
		
		String bestImg = (String) i.next();
		Float bestDistance = (Float) imgValueMap.get(bestImg);
				
		while(i.hasNext()) {
			String tmpImg = (String) i.next();
			Float tmpDistance = (Float) imgValueMap.get(tmpImg);
			
			// seeking for the smallest distance
			if(bestDistance > tmpDistance) {
				bestImg = tmpImg;
				bestDistance = tmpDistance;
			}						
			
		}
		
		return bestImg;
	}
	
	public static ArrayList<ComparedImage> getOrderedNbOfMostSimilitudeComparedToDataBank(String pathToImg, String pathToDataBank, Integer nbOfImgToReturn){
		HashMap<String, Float> imgValueMap = generateMapSimilitudeOfImageComparedToDataBank(pathToImg, pathToDataBank);
		
		if(nbOfImgToReturn > imgValueMap.size()) {
			return null;
		}else {
			ArrayList<ComparedImage> similitudeArray = convertHashMapToArrayListOfComparedImage(imgValueMap);
			ArrayList<ComparedImage> sortedArray = new ArrayList<>();
			
			for(int i=0; i<nbOfImgToReturn; i++) {
				Integer bestIdx = getIndexOfMostSimilitudeInComparedImageList(similitudeArray);
				sortedArray.add(i,similitudeArray.get(bestIdx));
				similitudeArray.remove(bestIdx+0); // +0 because if not it doesnt delete object
			}
			
			return sortedArray;
		}
		
	}
	
	//OK
	public static ArrayList<ComparedImage> convertHashMapToArrayListOfComparedImage(HashMap<String, Float> imgValueMap) {
		ArrayList<ComparedImage> comparedImgArray = new ArrayList<>();
		
		Set<String> imgSet = imgValueMap.keySet();
		Iterator<String> i = imgSet.iterator();
		
		String imgName = (String) i.next();
		Float imgDistance = (Float) imgValueMap.get(imgName);
		
		comparedImgArray.add(new ComparedImage(imgName, imgDistance));
		
		while(i.hasNext()) {
			imgName = (String) i.next();
			imgDistance = (Float) imgValueMap.get(imgName);
			
			comparedImgArray.add(new ComparedImage(imgName, imgDistance));
		}
		
		return comparedImgArray;
	}
	
	public static Integer getIndexOfMostSimilitudeInComparedImageList(ArrayList<ComparedImage> comparedImageArray) {
		Float bestDist = comparedImageArray.get(0).getImageDistance();
		Integer bestIdx = 0;
		
		for(int i=1; i<comparedImageArray.size();i++) {
			Float dist = comparedImageArray.get(i).getImageDistance();
		
			// seeking for the smallest distance
			if(bestDist > dist) {
				bestDist = dist;
				bestIdx = i;
			}
		}
				
		return bestIdx;
	}
	
	public static HashMap<String, Float> generateMapSimilitudeOfImageComparedToDataBank(String pathToImg, String pathToDataBank) {
		HashMap<String, Float> imgValueMap = new HashMap<>();
		
		File dataBank = new File(pathToDataBank);
		File[] dataBankFiles = dataBank.listFiles();
				
		for(File file : dataBankFiles) {
			String dataBankFileToCompare = pathToDataBank + file.getName();
						
			imgValueMap.put(dataBankFileToCompare, MatchingTools.getBestMatchesAvgBetweenTwoImage(pathToImg, dataBankFileToCompare));
		}
		
		return imgValueMap;			
	}
	
}
