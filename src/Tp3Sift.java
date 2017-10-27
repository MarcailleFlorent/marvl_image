// auto imports

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCalcHist;
import static org.bytedeco.javacpp.helper.opencv_imgproc.cvCreateHist;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.CV_HIST_ARRAY;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_highgui.WINDOW_AUTOSIZE;
import static org.bytedeco.javacpp.opencv_highgui.imshow;
import static org.bytedeco.javacpp.opencv_highgui.namedWindow;
import static org.bytedeco.javacpp.opencv_highgui.waitKey;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.MORPH_OPEN;
import static org.bytedeco.javacpp.opencv_imgproc.THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvNormalizeHist;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.erode;
import static org.bytedeco.javacpp.opencv_imgproc.morphologyEx;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.opencv_core.CvHistogram;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Scalar;

// imports

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.util.Vector;

import javax.print.attribute.standard.PrinterMoreInfoManufacturer;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.indexer.UByteIndexer;

public class Tp3Sift {
		
		public void doMain() {
		// ==== TP2 ====
	
			Mat img = loadImg();
			Mat grayImg = putInGray(img);
			
			printMatUint8(grayImg);
			
			// ==== TP1 ====
			
			/*displaySeuillage();
			displayEroded();
			displayOpened();
			
			displayGrayImg();
			
			displayGraySeuillage();
			displayGrayEroded();
			displayGrayOpened();*/
		}
		
		// === Functions ===

		private static Mat seuillage(Mat image) {
			Mat thresh = new Mat(image.size());
			threshold(image,thresh,120,255,THRESH_BINARY);
			
			return thresh;
		}
		
		private static Mat eroded(Mat image) {
			Mat thresh = loadThresh(image);

			Mat element5 = new Mat(5,5,CV_8U, new Scalar(1d));	// Element structurant permet de définir la nature de l'opération
			
			Mat eroded = new Mat();
			erode(thresh,eroded,element5);
			
			return eroded;
		}
		
		private static Mat opened(Mat image) {
			Mat thresh = loadThresh(image);
			
			Mat element5 = new Mat(5,5,CV_8U, new Scalar(1d));	// Element structurant permet de définir la nature de l'opération

			Mat opened = new Mat();
			morphologyEx(thresh,opened, MORPH_OPEN,element5);

			return opened;
		}
		
		private static Mat putInGray(Mat image) {
			Mat gray = new Mat(image.size());
			cvtColor(image, gray, CV_BGR2GRAY);
			
			return gray;
		}
		
		/*
		 * Separate the image in 3 places (Blue, Green and Red)
		 */
		private static CvHistogram imgCalcHistogram(IplImage image) {	

			// Create IplImage wich will receive the hist
			IplImage greyImage = cvCreateImage(image.cvSize(), image.depth(), 1);
			cvCvtColor(image, greyImage, CV_RGB2GRAY);
					
			// Establish number of bins
			int histSize = 256;
			
			// Set BGR ranges
			float minRange = 0f;
			float maxRange = 255f;
			float[] range = new float[] {minRange,maxRange};
			float[][] histRange = new float[][] {range};
			
			// Allocate hist object
			int dims = 1;
			int uniform = 1;
			int[] sizes = new int[] {histSize};
			int histType = CV_HIST_ARRAY;
			CvHistogram histogram = cvCreateHist(dims, sizes, histType, histRange, uniform);
			
			// Compute histogram
			int accumulate = 0;
			IplImage mak = null;
			IplImage[] aux = new IplImage[] {greyImage};
			
			// calculate histogram for images in aux list, with a 256 histogram bin
			cvCalcHist(aux, histogram, accumulate, null);
			cvNormalizeHist(histogram, 1);
			
			return histogram;
		}
		
		// === Display functions ===
		
		private static void displaySeuillage() {
			Mat image = loadImg();
			Mat thresh = seuillage(image);
			
			displayImg("Display Seuillage",thresh);
		}
		
		private static void displayEroded() {
			Mat image = loadImg();
			Mat thresh = eroded(image);
			
			displayImg("Display Eroded", thresh);
		}
		
		private static void displayOpened() {
			Mat image = loadImg();
			Mat thresh = opened(image);
			
			displayImg("Display Opened", thresh);
		}
		
		private static void displayGrayImg() {
			Mat image = loadImg();
			Mat thresh = putInGray(image);
			
			displayImg("Display Gray", thresh);
		}
		
		private static void displayGraySeuillage() {
			Mat image = loadImg();
			Mat gray = putInGray(image);
			Mat thresh = seuillage(gray);
			
			displayImg("Display Gray Seuillage",thresh);
		}
		
		private static void displayGrayEroded() {
			Mat image = loadImg();
			Mat gray = putInGray(image);
			Mat thresh = eroded(gray);
			
			displayImg("Display Gray Eroded", thresh);
		}
		
		private static void displayGrayOpened() {
			Mat image = loadImg();
			Mat gray = putInGray(image);
			Mat thresh = opened(gray);
			
			displayImg("Display Gray Opened", thresh);
		}
		
		private static void displayHistogram() {
			IplImage image = loadIplImg();
			
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
		
		private static IplImage loadIplImg() {
			String	imgName	=	"lena.jpg";	
			IplImage	image	=	cvLoadImage(imgName);
			
			if	(image.isNull())	{
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
		
		private static void printMatUint8(Mat img) {
			UByteIndexer byteIndexer = (UByteIndexer) img.createIndexer();
			
			for(int i=0;i<img.rows();i++) {
				for(int j=0;j<img.cols();j++) {
					System.out.print(" "+byteIndexer.get(i,j));
				}
				System.out.println();
			}
			
		}
		
		/*public static void main(String[] args) {
			System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
			Mat mat = Mat.eye( 3,3,CvType.CV_8UC1 );
			System.out.println( "mat = "+mat.dump() );
		}*/
	
}
