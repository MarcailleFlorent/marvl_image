import tools.DisplayTools;

public class Hello {
	
	/**
	* 	Main
	*	@param	args
	*/
	public	static	void	main(String[]	args)	{			
		// Display SIFT Matching between the 2 images
		DisplayTools.displayComparisonBetweenTwoImage("CocaLogo_1.jpg", "CocaLogo_2.png");
		
		// Display the best matching of Coca_1.jpg compared to all the DataBank images
		DisplayTools.displayBestImageInDataBankComparedTo("Coca_1.jpg");
		
		// Display each top 5 best matching image of Coca_1.jpg compared to all the DataBank images (starting with the best one) + distances + best class found
		DisplayTools.displayXNbOfBestImageInDataBankComparedTo("Coca_1.jpg", 5);
	}
	
}
