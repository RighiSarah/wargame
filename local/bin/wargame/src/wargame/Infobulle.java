package wargame;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

/**
 * Classe implémentant une méthode statique permettant de dessiner une infobulle sur la carte
 */
final class Infobulle extends Rectangle
{
	private static final long serialVersionUID = 3889208541227481368L;
	private static LinkedList<Message> File = new LinkedList<Message>();
	
//	private static Timer timer;

	/**
	 * Constructeur privé, ce n'est pas une classe faite pour être instanciée
	 */
	private Infobulle(){}
	
	/* Classe message qui instancie les messages */
	static class Message{
		
		private Point coord;
		private String message;
		private Color color;
		private char direction;
		private int timer;
		private int promptNext;
				
		
		/**
		 * Constructeur d'un message
		 * @param loc Numero de la case sur laquel le message doit être dessiné
		 * @param mes Message à écrire 
		 * @param direction au choix entre :
		 * 			- IConfig.HAUT : les messages monterons et seront animés
		 * 			- IConfig.BAS : les messages descendrons et seront animés
		 * 			- IConfig.NO_MOVE : les messages ne bouge pas ils reste soit au dessus soit en dessous du perso
		 * @param t ici, il s'agit du temps en seconde * le nombre de FPS 
		 * @param pN La variable la plus importante , elle determine si plusieurs messages peuvent s'afficher en même temps
		 * 				- Si pN = -1 , un seul message a la fois
		 * 				- Si pN = 0 , Les messages s'afficherons tous en meme temps
		 * 				- Si pN > 0 , les messages auront un décalage de n seconde [ n étant letemps mis ]
		 */
		Message(int loc, String mes, Color color, char direction, int t,  int pN) {

			Point coord = new Point(loc % IConfig.LARGEUR_CARTE , (int) loc / IConfig.LARGEUR_CARTE );
			this.message = mes;
			this.color = color;
			this.timer = t;
			this.direction = direction;

			this.coord = ( coord.y == 0 || direction == IConfig.HAUT)
								? new Point( coord.x * IConfig.NB_PIX_CASE, (coord.y + 1 ) * IConfig.NB_PIX_CASE ) 
								: new Point( coord.x * IConfig.NB_PIX_CASE, (coord.y - 1) * IConfig.NB_PIX_CASE);					
			this.promptNext = pN;

		}	
		
		/* Mes automatiquement tout les temps du message a jours  [ pN et t ] */
		public void setTime() {
			if(this.promptNext != -1 && this.promptNext != 0)
				this.promptNext--;
			this.timer--;
		}
		
		/* Retourne les coordonnées de la position du message */
		public Point getPoint() {
			return this.coord;
		}
		
		/* Deplace le message en changeant également la ccouche alpha du message */
		public void setDeplacement() {
			int x = (int) (60.0 / IConfig.FPS); // Max de 60 FPS ; pour passer a plus de FPs il faudrait tout transformer en float
			int alpha =  color.getAlpha() - ( 2 * x ) ;
			this.coord.y = (this.direction == IConfig.HAUT) ? getPoint().y - x : getPoint().y + x;

			this.color = new Color(color.getRed(),color.getGreen(), color.getBlue(), alpha );
		}
	}
	
	/**
	 * Fonction statique permettant rajouter un message a la file de message 
	 * @param loc Numero de la case sur laquel le message doit être dessiné
	 * @param s Message a afficher
	 * @param color Couleur du message
	 * @param direction au choix entre :
	 * 			- IConfig.HAUT : les messages monterons et seront animés
	 * 			- IConfig.BAS : les messages descendrons et seront animés
	 * 			- IConfig.NO_MOVE : les messages ne bouge pas ils reste soit au dessus soit en dessous du perso
	 * @param t ici, il s'agit du temps en seconde d'affichage du message
	 * @param promptNext La variable la plus importante , elle determine si plusieurs messages peuvent s'afficher en même temps
	 * 				- Si promptNext = -1 , un seul message a la fois
	 * 				- Si promptNext = 0 , Les messages s'afficherons tous en meme temps
	 * 				- Si promptNext > 0 , les messages auront un décalage de n seconde [ n étant letemps mis ]
	 */
	public static void generalQueue(int loc, String s, Color color, char direction, int t, int promptNext) {
		File.add(new Message(loc, s, color, direction, (int) (t * IConfig.FPS), promptNext));
	}
	
	/** Surchage de la generalQueue */
	/* Si new message ne comporte que ses parametres la alors ,
	 * il s'agit d'un message ou seul la direction est donnée I.e : Le message bougera obligatoirement en utilisant 
	 * cette fonction
	 */
	public static void newMessage(int loc, String s, Color color, char direction, int promptNext) {
		generalQueue(loc, s, color, direction, 1, promptNext);
	}
	
	/** Surchage de la generalQueue */
	/* Si new message ne comporte que ses parametres la alors ,
	 * il s'agit d'un message statique ou le temps est donné,il correspond au temps d'affichage du message
	 */
	public static void newMessage(int loc, String s, Color color, int t, int promptNext) {
		generalQueue(loc, s, color, IConfig.NO_MOVE, t, promptNext);
	}
	
	/** Fonction d'auto gestion de la file de message 
	 * @param g Graphics dans lequel on va dessiner le message
	 */
	public static void dessiner(Graphics g) {

		int size = File.size();

		if(size == 0)
			return;

		int i = 0;
		while(size != 0 && i < size ) {

			Message m = File.get(i);
			
			if(m.timer == 0) { // si le temps d'affichage est a 0 ont retire le message de la file
				File.poll();
				size--;
				i--;
			}
			else {
				if(m.direction != IConfig.NO_MOVE)
					m.setDeplacement(); // ont fait le deplacement que si la  direction est donnée [ Haut ou Bas ]
				m.setTime();
			}
			
			dessinerText(g, m.getPoint().x , m.getPoint().y ,m.message, m.color);
			
			if(m.promptNext == -1 || m.promptNext > 0)
				return;
			i++;		
		}	
	}
	
	/**
	 * Fonction statique permettant de dessiner un message sur la carte
	 * @param g Graphics dans lequel on va dessiner l'infobulle
	 * @param x coordonnée x de la case de la carte
	 * @param y coordonnée y de la case de la carte
	 * @param message message à écrire dans l'infobulle 
	 * @param string_color couleur du texte
	 */
	public static void dessinerText(Graphics g, int x, int y, String message, Color string_color)
	{
		Color ancienne_couleur = g.getColor();
		Font ancienne_font = g.getFont();
		String[] lignes = message.split("\n");
		
		g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, g.getFont().getSize() + 3 ) );
		g.setColor(string_color);
		
		for (String ligne : lignes){
			g.drawString(ligne, x, y += g.getFontMetrics().getHeight()); /* On simule un saut de ligne */
		}
		
		g.setFont(ancienne_font);
		g.setColor(ancienne_couleur);  
	}
	
	
	/**
	 * Fonction statique permettant de dessiner une infobulle sur la carte
	 * @param g Graphics dans lequel on va dessiner l'infobulle
	 * @param x coordonnée x de la case de la carte
	 * @param y coordonnée y de la case de la carte
	 * @param message message à écrire dans l'infobulle 
	 * @param string_color couleur du texte
	 * @param background_color couleur d'arrière plan de l'infobulle
	 */
	public static void dessinerText(Graphics g, int x, int y, String message, Color string_color, Color background_color)
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
