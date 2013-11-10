package wargame;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.Timer;

/** Classe de la Carte du jeu.
*	@author ABHAMON Ronan 
*/
public class Carte extends JPanel implements ActionListener
{
	static final long serialVersionUID = 0;
	
	/** Nombre de FPS pour la carte. */
	private static final double FPS = 60.0;
	
	/** Tileset de la carte. */
	private Tileset tileset;
	
	/** Carte. */
	protected char []carte;
	
	/** Monstres. */
	private Monstre []monstre;
	
	/** Table de jeu de la carte. */
	private Soldat []soldat;
	
	/* Héros. */
	private Heros []heros;
	
	/** Timer. */
	Timer timer;
	
	/** Constructeur par défaut. */
	Carte()
	{
		/* Initialisation taux de rafraichissement. */
		timer = new Timer((int)(1000.0 * 1.0 / FPS), this);
	    timer.setInitialDelay(0);
	    timer.start();
	    
	    /* Création d'une carte vide. */
		carte = new char [IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE];
	}
	
	private void genererCarte()
	{
		int x, y;
		
		soldat = new Soldat[IConfig.LARGEUR_CARTE * IConfig.HAUTEUR_CARTE];
		
		/* Chargement du tileset. */
		if(tileset == null)
			try {
				tileset = new Tileset(IConfig.NOM_TILESET);
			} 
			catch(IOException e) {
				System.out.println(e);
				return;
			}	
		
		/* Couche d'herbe. */
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE; i++)
			tileset.setHerbe(this, i % IConfig.LARGEUR_CARTE, i / IConfig.LARGEUR_CARTE);
		
		/* Rochers. */
		for(int i = 0; i < IConfig.NB_ROCHERS; i++)
		{
			x = (int)(Math.random() * IConfig.LARGEUR_CARTE);
			y = (int)(Math.random() * IConfig.HAUTEUR_CARTE);
			tileset.setRocher(this, x, y);
		}
		
		/* Arbres. */
		for(int i = 0; i < IConfig.NB_ARBRES; i++)
		{
			x = (int)(Math.random() * IConfig.LARGEUR_CARTE);
			y = (int)(Math.random() * IConfig.HAUTEUR_CARTE);
			
			if(!tileset.setArbre(this, x, y)) i--;
		}

		/* Paille. */
		for(int i = 0; i < IConfig.NB_PAILLES; i++)
		{
			x = (int)(Math.random() * IConfig.LARGEUR_CARTE);
			y = (int)(Math.random() * IConfig.HAUTEUR_CARTE);
			
			if(!tileset.setPaille(this, x, y)) i--;
		}
	}
	
	private void genererSoldats()
	{
		/* Tableaux de soldats. */
		heros = new Heros[IConfig.NB_HEROS];
		monstre = new Monstre[IConfig.NB_MONSTRES];

		/* Chargement des soldats de base. */
		try {
			for(int i = 0; i < IConfig.NB_HEROS; i++)
				heros[i] = new Heros(ISoldat.TypesH.getTypeHAlea());
			for(int i = 0; i < IConfig.NB_MONSTRES; i++)
				monstre[i] = new Monstre(ISoldat.TypesM.getTypeMAlea());
		} 
		catch(IOException e) {
			System.out.println(e);
			return;
		}	

		/* Positionnement des soldats. */
		for(int i = 0; i < IConfig.NB_HEROS; i++) {
			Point point = trouvePositionVide(Soldat.HUMAIN);
			soldat[point.x + IConfig.LARGEUR_CARTE * point.y] = heros[i];
		}
				
		for(int i = 0; i < IConfig.NB_MONSTRES; i++) {
			Point point = trouvePositionVide(Soldat.MONSTRE);
			soldat[point.x + IConfig.LARGEUR_CARTE * point.y] = monstre[i];
		}
	}
	
	/** Genere aléatoirement une carte. */
	public void generer()
	{
		genererCarte();
		genererSoldats();
	}
	
	/* Teste si une case existe sur la Carte.
	 * @param x Coordonnée x.
	 * @param y Coordonnée y.
	 * @return  true si existe, false sinon.
	 */
	public boolean existe(int x, int y)
	{
		return !(x < 0 || y < 0 || x >= IConfig.LARGEUR_CARTE || y >= IConfig.HAUTEUR_CARTE);
	}
	
	/** Trouve une position vide aléatoirement sur la carte. 
	 * Utilisable pour placer des Soldats.
	 * @param type Type de Soldat (Soldat.HOMME ou Soldat.MONSTRE)
	 * @return     La position vide.
	 * */
	public Point trouvePositionVide(char type)
	{	
		int dec = type == Soldat.HUMAIN ? 1 : 0;
		int num_tile;
		Tile tile;
		Point point = new Point();

		do {
			point.setLocation(dec * (IConfig.LARGEUR_CARTE / 2) + (int)(Math.random() * (IConfig.LARGEUR_CARTE / 2)), 
					          dec * (IConfig.HAUTEUR_CARTE / 2)  + (int)(Math.random() * (IConfig.HAUTEUR_CARTE / 2)));
			
			num_tile = carte[point.x + IConfig.LARGEUR_CARTE * point.y];
			tile = tileset.getTile(num_tile);
		} while(soldat[num_tile] != null || !tile.estPraticable());

		return point;
	}
	
    protected void paintComponent(Graphics g) 
    {    	
		Point dest = new Point();
		Point src;

		/* Affichage de la carte. */
		for(int i = 0; i< IConfig.LARGEUR_CARTE; i++)
			for(int j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
				int num_tile = (int)carte[i + j * IConfig.LARGEUR_CARTE];
				dest.setLocation(i, j);
				src = tileset.getCoord(num_tile);
				tileset.dessiner(g, src, dest);
			}
		
		/* Affichage des personnages. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null)
			{
				int x = i % IConfig.LARGEUR_CARTE;
				int y = i / IConfig.LARGEUR_CARTE;
				soldat[i].dessiner(g, x, y);		
			}
		
		/* Affichage des barres de vie. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null)
			{
				int x = i % IConfig.LARGEUR_CARTE;
				int y = i / IConfig.LARGEUR_CARTE;				
				soldat[i].dessineVie(g, x, y);
			}
	}
    
	public void actionPerformed(ActionEvent e) 
	{
    	repaint();
	}
}
