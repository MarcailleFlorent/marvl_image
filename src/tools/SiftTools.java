package tools;

public class SiftTools {

	// Le nombre de meilleurs caract�ristiques � retenir. Les caract�ristiques sont class�es par leurs scores (mesur� dans l'algorithme SIFT comme le contraste local).
	public static int nFeatures = 0; 
	
	// Nombre de couche dans chaque octave (3 est la valeur utilis�e avec D.Lowe). Le nombre d'octave est calcul� automatiquement � partir de la r�solution de l'image.
	public static int nOctaveLayers = 3; 
	
	// Seuil de contraste utilis� pour filtrer les caract�ristiques des r�gions � faible contraste. Plus le seuil est important, moins les caract�ristiques sont produites par le d�tecteur.
	public static double contrastThreshold = 0.03; 
	
	// Seuil utilis� pour filtrer les caract�ristiques de pointe. Plus la valeur est importante moins les caract�ristiques sont filtr�es
	public static int edgeThresold = 10; 
	
	// sigma gaussien appliqu� � l'image d'entr�e � l'octave \ # 0. R�duire le nombre si image captur�e est de faible qualit�
	public static double sigma = 1.6; 
}
