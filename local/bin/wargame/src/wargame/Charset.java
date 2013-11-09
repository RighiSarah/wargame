package wargame;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

/** Classe de chargement de Charset.
 *	@author ABHAMON Ronan 
 */
public class Charset implements ActionListener
{
	/** Nombre d'animations pour une direction. */
	public final static char N_ANIMATIONS = 4;
	
	/** Nombre de directions. */
	public final static char N_DIRECTIONS = 4;
	
	/** Directions possibles. */
	public final static char HAUT    = 0;
	public final static char DROITE  = 1;
	public final static char BAS     = 2;
	public final static char GAUCHE  = 3;

	/** Direction actuelle. */
	private char direction = BAS;
			
	/** Frame d'animation courante. */
	private char animation = 0;
	
	/** Délai entre chaque animation. */
	private int delai = 150;
			
	/** Timer. */
	Timer timer;
	
	/** Est mort ? */
	private boolean est_mort = false;
	
	/** Est visible ? */
	private boolean est_visible = true;
	
	/** Image du charset. */
	private BufferedImage image;
	
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
	
	/** Dessine le charset.
	 * 	Ne fait rien si l'image du charset n'existe pas encore.
	 * @param g Zone de dessin.
	 * @param x Destination sur la carte.
	 * @param y Destination sur la carte.
	 */ 
	public void dessiner(Graphics g, int x, int y)
	{
		if(image == null)
			return;

		int width = image.getWidth() / N_ANIMATIONS;
		int height = image.getHeight() / N_DIRECTIONS;
		
		int sx = animation * width;
		int sy = direction * height;
		int dx = x * IConfig.NB_PIX_CASE;
		int dy = y * IConfig.NB_PIX_CASE;
		
		/* Réglage du décalage de dx et dy. */
		dx += (IConfig.NB_PIX_CASE - width) / 2;
		dy += IConfig.NB_PIX_CASE - height;
		
		g.drawImage(image, dx, dy, dx + width, dy + height, 
					sx, sy, sx + width, sy + height, null);
	}
	
	/** Définir la direction du personnage. 
	 * @param direction Direction.
	 */
	public void setDirection(char direction)
	{
		if(direction < 0 || direction >= 4)
			return;
		
		this.direction = direction;
	}
	
	/** Mettre le status du personnage à mort. */
	public void setMort()
	{
		est_mort = true;
		direction = HAUT;
		animation = 0;
	    timer.setDelay(350);
	}
	
	/** Teste si le personnage est mort. 
	 * @return true si mort, false sinon.
	 */
	public boolean estMort()
	{
		return est_mort;
	}
	
	/** Teste si le personnage est visible.
	 * @return true si visible, false sinon.
	 */
	public boolean estVisible()
	{
		return est_visible;
	}
	
	/** Update le status du charset.
	 *  @param e Evenement appellant, le timer.
	 */
    public void actionPerformed(ActionEvent e)
    {    
    	/* Update seulement si le charset est affichable. */
    	if(est_visible)	{
    		/* Petite animation s'il est mort. */
    		if(est_mort) {
    			if(++direction >= N_DIRECTIONS)
    				est_visible = false;
    		}
    		
    		/* Affichage normal. */
    		else if(++animation >= N_ANIMATIONS)
    			animation = 0;
    	}
	}
}
