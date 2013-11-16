package wargame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
//import javax.swing.Timer;

/**
 * Classe implémentant une méthode statique permettant de dessiner une infobulle sur la carte
 */
final class Infobulle extends Rectangle
{
	private static final long serialVersionUID = 3889208541227481368L;
//	private static Timer timer;

	/**draw
	 * Constructeur privé, ce n'est pas une classe faite pour être instanciée
	 */
	private Infobulle(){}
	
	/**
	 * Fonction statique permettant de dessiner une infobulle sur la carte
	 * @param g Graphics dans lequel on va dessiner l'infobulle
	 * @param x coordonnée x de la case de la carte
	 * @param y coordonnée y de la case de la carte
	 * @param message message à écrire dans l'infobulle 
	 * @param string_color couleur du texte
	 * @param background_color couleur d'arrière plan de l'infobulle
	 */
	public static void dessiner(Graphics g, int x, int y, String message, Color string_color, Color background_color)
	{
		/* Sauvegarde de l'ancienne couleur pour la remettre ensuite */
		Color ancienne_couleur = g.getColor();
		
		/* drawString ne gère pas les \n donc on est obligé de faire appel à la méthode pour chaque ligne manuellement */
		String[] lignes = message.split("\n");
		
		FontMetrics fm = g.getFontMetrics(); 
		
		int start_x = x * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE / 2;
		int start_y = y * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE / 2;
		
		/* La hauteur correspond au nombre de ligne * la hauteur de la police à laquelle on rajoute une marge */
		int hauteur_rectangle = lignes.length * g.getFontMetrics().getHeight() + IConfig.MARGE_INFOBULLE;
		/* La largeur correspond à la largeur de la ligne la plus longue à laquelle on rajoute une marge */
		int largeur_rectangle = fm.stringWidth(Infobulle.stringTailleMax(lignes)) + 2*IConfig.MARGE_INFOBULLE;
		
		/* Calcul du nombre de pixels en hauteur et largeur de la carte */
		int pixel_largeur_carte = IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE;
		int pixel_hauteur_carte = IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE;
		
		/* Si la hauteur ou la largeur dépasse de la carte, alors on la replace à l'extreme limite */
		if(hauteur_rectangle + start_y > pixel_hauteur_carte){
			start_y = pixel_hauteur_carte - hauteur_rectangle;
		}
		if(largeur_rectangle + start_x > pixel_largeur_carte){
			start_x = pixel_largeur_carte - largeur_rectangle;
		}
		
		if(background_color != null) {
			/* On spécifie la couleur d'arrière plan et on trace notre rectangle */
			g.setColor(background_color);
			g.fillRect(start_x - IConfig.MARGE_INFOBULLE, start_y, largeur_rectangle, hauteur_rectangle); 
			
			/* Bordure du rectangle */
			g.setColor(Color.DARK_GRAY);
			g.drawRect(start_x - IConfig.MARGE_INFOBULLE, start_y, largeur_rectangle, hauteur_rectangle);
		}
		
		/* Pour chaque ligne, on l'écrit dans la couleur spécifiée */
		g.setColor(string_color);
		for (String ligne : lignes){
			g.drawString(ligne, start_x, start_y += g.getFontMetrics().getHeight()); /* On simule un saut de ligne */
		}
		
		g.setColor(ancienne_couleur);  
	}
	
	/* Méthode retournant la chaine de taille maximale parmi un tableau de chaines */
	private static String stringTailleMax(String[] message){
		String string_max = message[0];
		int length_max = string_max.length();
		
		for(int i = 1; i < message.length; i++){
			if(message[i].length() > length_max){
				string_max = message[i];
				length_max = string_max.length();
			}
		}
		
		return string_max;
	}
}
