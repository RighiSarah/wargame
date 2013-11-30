package wargame;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** Classe de chargement de Tileset. */
public class Tileset
{
	/** Image du tileset. */
	private BufferedImage image;
	
	/** Tableau de tiles. */
	private Tile tile[];
	
	/** Largeur du tileset en nombre de cases. */
	private final int LARGEUR;
	
	/** Hauteur du tileset en nombre de cases. */
	private final int HAUTEUR;
		
	/** Charge un tileset en mémoire.
	 *	@param filename Nom de l'image à charger. 
	 *	@throws IOException si l'image n'a pu être chargée.
	 */
	public Tileset(String filename) throws IOException
	{
		File f = new File(IConfig.CHEMIN_IMAGE + filename);	
		image = ImageIO.read(f); /* IOException ? */
		
		LARGEUR = image.getWidth() / IConfig.NB_PIX_CASE;
		HAUTEUR = image.getHeight() / IConfig.NB_PIX_CASE;
		
		tile = new Tile[LARGEUR * HAUTEUR];
		
		configure();
	}
		
	/** Configure le tileset. */
	private void configure()
	{
		/* Par défaut, aucune case n'est praticable, ni traversable. */
		for(int i = 0; i < LARGEUR * HAUTEUR; i++)
			tile[i] = new Tile(false, false);
		
		/* Herbe. */
		tile[0].setTraversable(true); tile[0].setPraticable(true);
		tile[1].setTraversable(true); tile[1].setPraticable(true);
		tile[8].setTraversable(true); tile[8].setPraticable(true);
		tile[9].setTraversable(true); tile[9].setPraticable(true);
		
		/* Eau. */
		tile[24].setTraversable(true);
		tile[25].setTraversable(true);
		tile[32].setTraversable(true);
		tile[33].setTraversable(true);
	}
	
	/** Ajoute une case d'herbe sur la carte.
	 * @param carte Carte du jeu
	 * @param p     Position où ajouter l'herbe
	 * @return      false si x, y ne font pas partis de la carte, true sinon.
	 */
	protected boolean setHerbe(Carte carte, Position p)
	{
		int res = (int)(Math.random() * 100);
		int tile = p.getNumCase();
		
		if(!p.estValide())
			return false;
		
		if(res > 60)
			carte.carte[tile] = 0;
		else if(res > 40)
			carte.carte[tile] = 1;
		else if(res > 20)
			carte.carte[tile] = 8;
		else
			carte.carte[tile] = 9;
		
		return true;
	}
	
	/** Ajoute une case de rochers sur la carte.
	 * @param carte Carte du jeu
	 * @param p     Position où ajouter le rocher
	 * @return      false si p n'est pas sur la carte, true sinon.
	 */
	protected boolean setRocher(Carte carte, Position p)
	{
		int res = (int)(Math.random() * 4);
		int tile = p.getNumCase();
		
		if(!p.estValide())
			return false;
		
		carte.carte[tile] = (char)(16 + res);
	
		return true;
	}
	
	/** Dessine une partie du tileset sur une carte.
	 * @param carte Carte où dessiner.
	 * @param src1 Coordonnées (x, y) sur le tileset.
	 * @param src2 Coordonnées (x2, y2) sur le tileset.
	 * @param dest Coordonnées de destination sur la carte.
	 * @return false si aucune place pour dessiner, true sinon.
	 */
	private boolean dessinerPartie(Carte carte, Point src1, Point src2, Point dest)
	{	
		/* Vérification qu'il n'existe que des blocs praticables. */
		for(int i = 0; i <= src2.x - src1.x; i++)
			for(int j = 0; j <= src2.y - src1.y; j++)
				if(!tile[carte.carte[(dest.x + i) + IConfig.LARGEUR_CARTE * (dest.y + j)]].estPraticable())
					return false;
		
		for(int i = src1.x; i <= src2.x; i++)
			for(int j = src1.y; j <= src2.y; j++)
			{
				/* Tile du tileset. */
				int num_tile = i + LARGEUR * j;
				
				/* Case sur la carte. */
				int x = dest.x + i - src1.x;
				int y = dest.y + j - src1.y;
				
				if(x < IConfig.LARGEUR_CARTE && y < IConfig.HAUTEUR_CARTE)
				{
					int tile = x + IConfig.LARGEUR_CARTE * y;
					carte.carte[tile] = (char)num_tile;
				}
			}
		
		return true;
	}
	
	/** Dessine de la paille sur la carte.
	 * @param carte Carte où dessiner.
	 * @param p		Position où ajouter la paille.
	 * @return      false si p ne fait pas parti de la carte, true sinon.
	 */
	protected boolean setPaille(Carte carte, Position p)
	{
		if(!p.estValide())
			return false;
		
		return dessinerPartie(carte, new Point(2, 0), new Point(3, 1), p);
	}
	
	/** Dessine un arbre sur la carte.
	 * @param carte Carte où dessiner.
	 * @param p		Position où ajouter l'arbre.
	 * @return      false si p ne fait pas parti de la carte, true sinon.
	 */
	protected boolean setArbre(Carte carte, Position p)
	{
		if(!p.estValide())
			return false;
		
		return dessinerPartie(carte, new Point(4, 0), new Point(7, 3), p);
	}
	
	/** Dessine une flaque d'eau sur la carte.
	 * @param carte Carte où dessiner.
	 * @param p		Position où ajouter l'eau.
	 * @return      false si p ne fait pas parti de la carte, true sinon.
	 */
	protected boolean setEau(Carte carte, Position p)
	{
		if(!p.estValide())
			return false;
		
		return dessinerPartie(carte, new Point(0, 3), new Point(1, 4), p);
	}
	
	/** Dessine un tile du Tileset.
	 * @param g    Zone de dessin.
	 * @param src  Source du tile à afficher.
	 * @param dest Destination du tile à afficher.
	 */ 
	public void dessiner(Graphics g, Point src, Point dest)
	{
		int sx = src.x * IConfig.NB_PIX_CASE;
		int sy = src.y * IConfig.NB_PIX_CASE;
		int dx = dest.x * IConfig.NB_PIX_CASE;
		int dy = dest.y * IConfig.NB_PIX_CASE;
		
		g.drawImage(image, dx, dy, dx + IConfig.NB_PIX_CASE, dy + IConfig.NB_PIX_CASE, 
					sx, sy, sx + IConfig.NB_PIX_CASE, sy + IConfig.NB_PIX_CASE, null);
	}
	
	/** Retourne un tile du tileset.
	 * @param  x Coordonnée x du Tile.
	 * @param  y Coordonnée y du Tile.
	 * @return Le tile ou null si non trouvé.
	 */
	public Tile getTile(int x, int y)
	{
		if(!existe(x, y))
			return null;
		
		return tile[x + LARGEUR * y];
	}
	
	/** Retourne un tile du tileset.
	 * @param num Numéro du tile.
	 * @return    Le tile ou null si non trouvé.
	 */
	public Tile getTile(int num)
	{
		if(num >= LARGEUR * HAUTEUR)
			return null;
		
		return tile[num];	
	}
	
	/** Teste si un Tile existe dans le tileset.
	 * @param x Coordonnée x du Tile.
	 * @param y Coordonnée y du Tile.
	 * @return  true si existe, false sinon.
	 */
	public boolean existe(int x, int y)
	{
		return (x >= 0 && y >= 0 && x < LARGEUR && y <= HAUTEUR);
	}
	
	/** Retourne les coordonnées d'un numéro de tile du tileset. 
	 * 
	 * @param numTile Numéro de la tuile.
	 * @return Les coordonnées ou null sinon.
	 */
	public Point getCoord(int numTile)
	{
		if(numTile >= LARGEUR * HAUTEUR)
			return null;
		
		return new Point(numTile % LARGEUR, numTile / LARGEUR);
	}
}
