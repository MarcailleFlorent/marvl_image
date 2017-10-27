import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;


import java.util.Arrays;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.helper.opencv_calib3d;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DrawMatchesFlags;
import org.bytedeco.javacpp.opencv_shape;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

import static org.bytedeco.javacpp.opencv_features2d.drawKeypoints;
import static org.bytedeco.javacpp.opencv_features2d.drawMatches;


public class Hello {
	
	/**
	* 	Main
	*	@param	args
	*/
	public	static	void	main(String[]	args)	{	
		//Mat img = loadImg();
		//Mat grayImg = putInGray(img);
		
		// displaySiftofImage(img);
		
		
		Mat[] images = new Mat[2];
		images[0] = loadImg();
		images[1] = loadImg();
		
		displayImg("fff", images[1]);
		
		KeyPointVector[] keyPoints = new KeyPointVector[2];
		
		int nFeatures = 0; // Le nombre de meilleurs caractéristiques à retenir. Les caractéristiques sont classées par leurs scores (mesuré dans l'algorithme SIFT comme le contraste local).
		int nOctaveLayers = 3; // Nombre de couche dans chaque octave (3 est la valeur utilisée avec D.Lowe). Le nombre d'octave est calculé automatiquement à partir de la résolution de l'image.
		double contrastThreshold = 0.03; // Seuil de contraste utilisé pour filtrer les caractéristiques des régions à faible contraste. Plus le seuil est important, moins les caractéristiques sont produites par le détecteur.
		int edgeThresold = 10; // Seuil utilisé pour filtrer les caractéristiques de pointe. Plus la valeur est importante moins les caractéristiques sont filtrées
		double sigma = 1.6; // sigma gaussien appliqué à l'image d'entrée à l'octave \ # 0. Réduire le nombre si image capturée est de faible qualité
		Mat[] descriptors = null;
		
		Loader.load(opencv_calib3d.class);
		Loader.load(opencv_shape.class);
		
		SIFT sift;
		sift = SIFT.create(nFeatures,nOctaveLayers,contrastThreshold,edgeThresold,sigma);

		sift.detect(images[0], keyPoints[0]);
		sift.detect(images[1], keyPoints[1]);
		
		descriptors[0] = new Mat();
		descriptors[1] = new Mat();
		
		sift.compute(images[0], keyPoints[0], descriptors[0]);
		sift.compute(images[1], keyPoints[1], descriptors[1]);
		
		// Match descriptors
		BFMatcher matcher = new BFMatcher(NORM_L2,false);
		DMatchVector matches = new DMatchVector();
		
		matcher.match(descriptors[0], descriptors[1], matches);
		
		DMatchVector bestMatches = selectBestMatcher(matches, 10);
		
		// Draw Matches
		Mat imageMatches = new Mat();
		BytePointer mask = null;
		
		drawMatches(images[0], keyPoints[0], images[1], keyPoints[1], bestMatches, imageMatches);
		displayImg("Comparison", imageMatches);
	}
	
	// === Functions ===
	
	private static Mat putInGray(Mat image) {
		Mat gray = new Mat(image.size());
		cvtColor(image, gray, CV_BGR2GRAY);
		return gray;
	}

	private static void displaySiftofImage(Mat matImage) {
		KeyPointVector keyPoints = new KeyPointVector();
		
		int nFeatures = 0; // Le nombre de meilleurs caractéristiques à retenir. Les caractéristiques sont classées par leurs scores (mesuré dans l'algorithme SIFT comme le contraste local).
		int nOctaveLayers = 3; // Nombre de couche dans chaque octave (3 est la valeur utilisée avec D.Lowe). Le nombre d'octave est calculé automatiquement à partir de la résolution de l'image.
		double contrastThreshold = 0.03; // Seuil de contraste utilisé pour filtrer les caractéristiques des régions à faible contraste. Plus le seuil est important, moins les caractéristiques sont produites par le détecteur.
		int edgeThresold = 10; // Seuil utilisé pour filtrer les caractéristiques de pointe. Plus la valeur est importante moins les caractéristiques sont filtrées
		double sigma = 1.6; // sigma gaussien appliqué à l'image d'entrée à l'octave \ # 0. Réduire le nombre si image capturée est de faible qualité
		
		Loader.load(opencv_calib3d.class);
		Loader.load(opencv_shape.class);
		
		SIFT sift;
		
		sift = SIFT.create(nFeatures,nOctaveLayers,contrastThreshold,edgeThresold,sigma);
		sift.detect(matImage, keyPoints);
		
		// Draw KeyPoints
		Mat featureImage = new Mat();
		drawKeypoints(matImage,keyPoints, featureImage, new Scalar(255,255,255,0), DrawMatchesFlags.DRAW_RICH_KEYPOINTS);
		
		displayImg("keyPointsDisplay", featureImage);
	}
	
	private static void displaySiftComparison() {
		Mat[] images = new Mat[2];
		images[0] = loadImg();
		images[1] = loadImg();
		
		KeyPointVector[] keyPoints = new KeyPointVector[2];
		
		int nFeatures = 0; // Le nombre de meilleurs caractéristiques à retenir. Les caractéristiques sont classées par leurs scores (mesuré dans l'algorithme SIFT comme le contraste local).
		int nOctaveLayers = 3; // Nombre de couche dans chaque octave (3 est la valeur utilisée avec D.Lowe). Le nombre d'octave est calculé automatiquement à partir de la résolution de l'image.
		double contrastThreshold = 0.03; // Seuil de contraste utilisé pour filtrer les caractéristiques des régions à faible contraste. Plus le seuil est important, moins les caractéristiques sont produites par le détecteur.
		int edgeThresold = 10; // Seuil utilisé pour filtrer les caractéristiques de pointe. Plus la valeur est importante moins les caractéristiques sont filtrées
		double sigma = 1.6; // sigma gaussien appliqué à l'image d'entrée à l'octave \ # 0. Réduire le nombre si image capturée est de faible qualité
		Mat[] descriptors = null;
		
		Loader.load(opencv_calib3d.class);
		Loader.load(opencv_shape.class);
		
		SIFT sift;
		sift = SIFT.create(nFeatures,nOctaveLayers,contrastThreshold,edgeThresold,sigma);

		// Detect SIFT features and compute descriptors for both images
		/*for(int i=0; i < images.length ; i++) {
			sift.detect(images[i], keyPoints[i]);
			// Create CvMat initialized with empty pointer, using simply  'new CvMat()' leads to an exception
			descriptors[i] = new Mat();
			sift.compute(images[i], keyPoints[i], descriptors[i]);
		}*/
		
		sift.detect(images[0], keyPoints[0]);
		sift.detect(images[1], keyPoints[1]);
		
		descriptors[0] = new Mat();
		descriptors[1] = new Mat();
		
		sift.compute(images[0], keyPoints[0], descriptors[0]);
		sift.compute(images[1], keyPoints[1], descriptors[1]);
		
		// Match descriptors
		BFMatcher matcher = new BFMatcher(NORM_L2,false);
		DMatchVector matches = new DMatchVector();
		
		matcher.match(descriptors[0], descriptors[1], matches);
		
		DMatchVector bestMatches = selectBestMatcher(matches, 10);
		
		// Draw Matches
		Mat imageMatches = new Mat();
		BytePointer mask = null;
		
		drawMatches(images[0], keyPoints[0], images[1], keyPoints[1], bestMatches, imageMatches);
		displayImg("Comparison", imageMatches);
	}
	
	private static DMatchVector selectBestMatcher(DMatchVector matches, int numberToSelect) {
		DMatch[] sorted = DMatchVectorToArray(matches);
		Arrays.sort(sorted, (a,b) -> {
			return a.lessThan(b) ? -1 : 1;
		});
		
		DMatch[] bestArray = Arrays.copyOf(sorted, numberToSelect);
		
		return new DMatchVector(bestArray);
	}
	
	private static DMatch[] DMatchVectorToArray(DMatchVector matches) {
		assert matches.size() <= Integer.MAX_VALUE;
		int matchesSize = (int) matches.size();
		
		// Convert keyPoints to Scala sequence
		DMatch[] result = new DMatch[matchesSize];
		
		for(int i=0; i < matchesSize ; i++) {
			result[i] = new DMatch(matches.get(i));
		}
		
		return result;
	}
	
	// === Display functions ===
		
	private static void displayGrayImg() {
		Mat image = loadImg();
		Mat thresh = putInGray(image);
		
		displayImg("Display Gray", thresh);
	}
	
	// === Globals ===
	
	private static Mat loadImg() {
		String	imgName	=	"lena.jpg";	
		Mat	image	=	imread(imgName);
		
		if	(image.empty())	{
			throw	new	RuntimeException("cannot	find	img	"	+	imgName	+	"	in	classpath");
		}
		
		return image;
	}

	private static Mat loadThresh(Mat image) {
		Mat thresh = new Mat(image.size());
		threshold(image,thresh,120,255,THRESH_BINARY);
		
		return thresh;
	}
	
	private static void displayImg(String windowsName, Mat displayImg) {
		namedWindow(windowsName, WINDOW_AUTOSIZE);	// Create a window for display
		imshow(windowsName, displayImg);	// Show our image inside it
		waitKey(0);	// Wait for a keys in the windows
	}
}
