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

	/**draw
	 * Constructeur privé, ce n'est pas une classe faite pour être instanciée
	 */
	private Infobulle(){}
	
	
	static class Message{
		
		private Point coord;
		private String message;
		private Color color;
		private char direction;
		private int timer;
		private int promptNext;
				
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
		
		public void setTime() {
			if(this.promptNext != -1 && this.promptNext != 0)
				this.promptNext--;
			this.timer--;
		}
		
		public Point getPoint() {
			return this.coord;
		}
		
		public void setDeplacement() {
			int x = (int) (60.0 / IConfig.FPS); // Max de 60 FPS ; pour passer a plus de FPs il faudrait tout transformer en float
			int alpha =  color.getAlpha() - ( 2 * x ) ;
			this.coord.y = (this.direction == IConfig.HAUT) ? getPoint().y - x : getPoint().y + x;

			this.color = new Color(color.getRed(),color.getGreen(), color.getBlue(), alpha );
		}
	}
	
	
	public static void generalQueue(int loc, String s, Color color, char direction, int t, int promptNext) {
		File.add(new Message(loc, s, color, direction, (int) (t * IConfig.FPS), promptNext));
		System.out.println("ADD"+s);
	}
	
	public static void newMessage(int loc, String s, Color color, char direction, int promptNext) {
		generalQueue(loc, s, color, direction, 1, promptNext);
	}
	
	public static void newMessage(int loc, String s, Color color, int t, int promptNext) {
		generalQueue(loc, s, color, IConfig.NO_MOVE, 1, promptNext);
	}
	
	public static void dessiner(Graphics g, String message, int x, int y) {

		int size = File.size();

		if(size == 0) {
			if(message == "")
				return;
			dessinerText(g, x, y, message, IConfig.MESSAGE_NEUTRE, IConfig.ARRIERE_PLAN);
			return;
		}
		
		int i = 0;
		while(size != 0 && i < size ) {

			Message m = File.get(i);
			
			if(m.timer == 0) {
				File.poll();
				size--;
				i--;
			}
			else {
				if(m.direction != IConfig.NO_MOVE)
					m.setDeplacement();
				m.setTime();
			}
			
			dessinerText(g, m.getPoint().x , m.getPoint().y ,m.message, m.color);
			
			if(m.promptNext == -1 || m.promptNext > 0)
				return;
			i++;		
		}	
	}
	
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
