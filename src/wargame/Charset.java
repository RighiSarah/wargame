package wargame;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

/** Classe de chargement de Charset. */
public class Charset implements ActionListener
{
	/** Nombre d'animations pour une direction. */
	public final static char N_ANIMATIONS = 4;
	
	/** Nombre de directions. */
	public final static char N_DIRECTIONS = 4;
	
	/** Directions possibles. */
	public enum Direction { 
		HAUT(0),
		DROITE(1),
		BAS(2),
		GAUCHE(3); 
		
		private final int VALEUR;
		
		
		Direction(int valeur){
			this.VALEUR = valeur;
		}
		
		/**
		 * Permet d'augmenter la direction de 1
		 * @return Vrai si on a atteint la direction maximale (arbitrairement GAUCHE), faux sinon
		 */
		public Direction directionSuivante(){
			switch(this){
				case HAUT : return(Direction.DROITE);
				case DROITE : return(Direction.BAS);
				case BAS : return(Direction.GAUCHE);
				case GAUCHE : return(Direction.HAUT);
				default : return(Direction.HAUT);
			}
		}
		
		/**
		 * Retourne le numéro associé à la direction (arbitrairement, haut = 0)
		 * @return le numéro associé à la direction
		 */
		public int getValeur(){
			return this.VALEUR;
		}
		
		/**
		 * Redéfinition de méthode permettant d'afficher la direction en toutes lettres
		 */
		public String toString(){
			String[] t = new String[N_DIRECTIONS];
			t[0] = "HAUT";
			t[1] = "DROITE";
			t[2] = "BAS";
			t[3] = "GAUCHE";
			
			return t[this.VALEUR];
		}
	};

	/** Offset sur l'axe Y. */
	private int offset = 0;
	
	/** Direction actuelle. */
	protected Direction direction = Direction.BAS;
			
	/** Frame d'animation courante. */
	protected char animation = 0;
	
	/** Délai entre chaque animation. */
	protected int delai = 100;
			
	/** Timer. */
	Timer timer;
	
	/** Est visible ? */
	protected boolean estVisible = true;
	
	/** Image du charset. */
	protected BufferedImage image;
	
	/** Crée un charset vide. */
	public Charset()
	{
		image = null;
		timer = new Timer(delai, this);
	    timer.setInitialDelay(0);
	    timer.start();	    
	}
	
	/** Charge un charset en mémoire.
	 *	@param filename Nom de l'image à charger. 
	 *	@throws IOException si l'image n'a pu être chargée.
	 */
	public Charset(String filename) throws IOException
	{
		this();
		
		File f = new File(filename);	
		image = ImageIO.read(f); /* IOException ? */
	}
	
	/** Dessine le charset avec un offset x et y.
	 * 	Ne fait rien si l'image du charset n'existe pas encore.
	 * @param g Zone de dessin.
	 * @param x Destination sur la carte.
	 * @param y Destination sur la carte.
	 * @param offsetX Offset x
	 * @param offsetY Offset dy
	 */ 
	protected void dessinerAvecOffset(Graphics g, int x, int y, int offsetX, int offsetY)
	{
		if(image == null)
			return;

		int width = image.getWidth() / N_ANIMATIONS;
		int height = image.getHeight() / N_DIRECTIONS;
		
		int sx = animation * width;
		int sy = direction.getValeur() * height;
		int dx = x * IConfig.NB_PIX_CASE;
		int dy = y * IConfig.NB_PIX_CASE;
		
		/* Réglage du décalage de dx et dy. */
		dx += (IConfig.NB_PIX_CASE - width) / 2;
		dy += IConfig.NB_PIX_CASE - height;
		
		g.drawImage(image, dx + offsetX, 
				           dy + offsetY + offset, 
					       dx + offsetX + width, 
					       dy + offsetY + offset + height, 
					       sx, sy, sx + width, sy + height, null);
	}
	
	/** Dessine le charset.
	 * 	Ne fait rien si l'image du charset n'existe pas encore.
	 * @param g Zone de dessin.
	 * @param x Destination sur la carte.
	 * @param y Destination sur la carte.
	 */ 
	protected void dessiner(Graphics g, int x, int y)
	{
		dessinerAvecOffset(g, x, y, 0, 0);
	}
	
	/** Définir la direction du charset. 
	 * @param direction Direction.
	 */
	public void setDirection(Direction direction)
	{
		if(direction.getValeur() < 0 || direction.getValeur() >= N_DIRECTIONS)
			return;
		
		this.direction = direction;
	}
	
	/** Modifie l'offset du charset.
	 * @param y Offset.
	 */
	public void setOffset(int y)
	{
		offset = y;
	}
	
	/** Teste si le personnage est visible.
	 * @return true si visible, false sinon.
	 */
	public boolean estVisible()
	{
		return estVisible;
	}
	
	/** Update le status du charset.
	 *  @param e Evenement appellant, le timer.
	 */
    public void actionPerformed(ActionEvent e)
    {    
    	/* Update seulement si le charset est affichable. */
    	if(estVisible)	{
    		/* Affichage normal. */
    		if(++animation >= N_ANIMATIONS)
    			animation = 0;
    	}
	}
}
