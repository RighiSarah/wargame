package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JPanel;
import javax.swing.Timer;

/** Classe de la Carte du jeu.
*	@author ABHAMON Ronan 
*/
public class Carte extends JPanel implements ActionListener, Serializable
{	
	/** Nombre de FPS pour la carte. */
	private static final double FPS = 60.0;
	
	/** Tileset de la carte. */
	private Tileset tileset;
	
	/** Carte. */
	protected char []carte;
	
	/** Monstres. */
	private Monstre []monstre;
	
	/** Héros. */
	private Heros []heros;
	
	/** Table de jeu de la carte. */
	private Soldat []soldat;
	
	/** La carte est-elle générée ? */
	private boolean generer = false;
	
	/** Indique la case courante sélectionnée. */
	private int caseactionnee = -1;
	
	/** Indique le soldat actuellement pointé par le curseur de la souris */
	private Soldat soldat_pointe = null;
	
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
		
		/* Capture de la souris. */
		addMouseListener(new MouseAdapter() { 
			public void mouseClicked(MouseEvent e) { 
				if(generer == true) {
					int caseclick = (e.getX() / IConfig.NB_PIX_CASE) + 
								    IConfig.LARGEUR_CARTE * (e.getY() / IConfig.NB_PIX_CASE);
					
					if(soldat[caseclick] != null && soldat[caseclick] instanceof Heros && soldat[caseclick].estVisible())
						caseactionnee = caseclick;
					else {
						System.out.println("Case :" + caseclick); //affichage temporaire
						caseactionnee = -1;
					}
				}
			}
		});
		
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(generer == true){
					int coord_curseur = (e.getX() / IConfig.NB_PIX_CASE) + 
						    IConfig.LARGEUR_CARTE * (e.getY() / IConfig.NB_PIX_CASE);
					if(soldat[coord_curseur] != null && soldat[coord_curseur].estVisible())
						soldat_pointe = soldat[coord_curseur];
					else
						soldat_pointe = null;
						
				}
				
			}
		});
		
	}
	
	private int getNumCase(int x, int y)
	{
		return x + IConfig.LARGEUR_CARTE * y;	
	}
	
	private Point getCoordCase(int numCase)
	{
		return new Point(numCase % IConfig.LARGEUR_CARTE, numCase / IConfig.LARGEUR_CARTE);
	}
	
	private void chargerTileset()
	{
		if(tileset == null)
			try {
				tileset = new Tileset(IConfig.NOM_TILESET);
			} 
			catch(IOException e) {
				System.out.println(e);
				return;
			}		
	}
	
	private void genererCarte()
	{
		int x, y;
		
		soldat = new Soldat[IConfig.LARGEUR_CARTE * IConfig.HAUTEUR_CARTE];
		chargerTileset();
		
		/* Couche d'herbe. */
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE; i++)
			tileset.setHerbe(this, getCoordCase(i).x, getCoordCase(i).y);
		
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
		/* Mise à 0 de la carte. */
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.HAUTEUR_CARTE; i++)
			soldat[i] = null;
		
		/* Tableaux de soldats. */
		heros = new Heros[IConfig.NB_HEROS];
		monstre = new Monstre[IConfig.NB_MONSTRES];

		/* Chargement des soldats de base. */
		try {
			for(int i = 0; i < IConfig.NB_HEROS; i++)
			{
				heros[i] = new Heros(ISoldat.TypesH.getTypeHAlea());
				heros[i].setDirection(Charset.GAUCHE);
			}
			for(int i = 0; i < IConfig.NB_MONSTRES; i++)
			{
				monstre[i] = new Monstre(ISoldat.TypesM.getTypeMAlea());
				monstre[i].setDirection(Charset.DROITE);
			}
		} 
		catch(IOException e) {
			System.out.println(e);
			return;
		}	

		/* Positionnement des soldats. */
		for(int i = 0; i < IConfig.NB_HEROS; i++) {
			Point point = trouvePositionVide(Soldat.HUMAIN);
			int numCase = getNumCase(point.x, point.y);
					
			soldat[numCase] = heros[i];
			soldat[numCase].setNumCase(numCase);
		}
				
		for(int i = 0; i < IConfig.NB_MONSTRES; i++) {
			Point point = trouvePositionVide(Soldat.MONSTRE);
			int numCase = getNumCase(point.x, point.y);
			
			soldat[numCase] = monstre[i];
			soldat[numCase].setNumCase(numCase);
		}
	
	}

	/** Déplace un soldat sur la carte.
	 * 
	 * @param soldat Soldat à deplacer.
	 * @param x      Coordonnée X où poser le soldat.
	 * @param y      Coordonnée Y où poser le soldat.
	 * @return       true si case correcte, false sinon.
	 */
	boolean deplaceSoldat(Soldat soldat, int x, int y)
	{
		if(!existe(x, y))
			return false;
		
		soldat.setSeDeplace(true);
		
		return true;
	}

	/** Genere aléatoirement une carte. */
	public void generer()
	{
		generer = true;
		genererCarte();
		genererSoldats();
		caseactionnee = -1;
	}
	
	/* Teste si une case existe sur la Carte.
	 * @param x Coordonnée x.
	 * @param y Coordonnée y.
	 * @return  true si existe, false sinon.
	 */
	public boolean existe(int x, int y)
	{
		return (x >= 0 && y >= 0 && x < IConfig.LARGEUR_CARTE && y < IConfig.HAUTEUR_CARTE);
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
			point.setLocation(dec * (IConfig.LARGEUR_CARTE / 2) + dec + (int)(Math.random() * (IConfig.LARGEUR_CARTE / 2)), 
					          (int)(Math.random() * IConfig.HAUTEUR_CARTE));
			
			num_tile = carte[getNumCase(point.x, point.y)];
			tile = tileset.getTile(num_tile);
		} while(soldat[num_tile] != null || !tile.estPraticable());

		return point;
	}
	
	/** Sauvegarde une carte.
	 * @param num Numéro de la sauvegarde.
	 */
	public void sauvegarde(int num)
	{
		try {
			FileOutputStream fichier = new FileOutputStream(IConfig.NOM_SAUVEGARDE + num + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			
			oos.writeObject(carte);
			oos.writeObject(monstre);
			oos.writeObject(heros);
			oos.writeObject(soldat);

			oos.flush();
			oos.close();
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Charge une carte.
	 * @param num Numéro de la sauvegarde.
	 */
	void charge(int num)
	{
		try {
			FileInputStream fichier = new FileInputStream(IConfig.NOM_SAUVEGARDE + num + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fichier);
			
			carte   = (char[])ois.readObject();
			monstre = (Monstre[])ois.readObject();
			heros   = (Heros[])ois.readObject();
			soldat  = (Soldat[])ois.readObject();
			
			/** Les images ne sont pas sérializées... */
			for(int i = 0; i < IConfig.NB_HEROS; i++)
			{
				heros[i].setImage();
				heros[i].setDirection(Charset.GAUCHE);
			}
			
			for(int i = 0; i < IConfig.NB_MONSTRES; i++)
			{
				monstre[i].setImage();
				monstre[i].setDirection(Charset.DROITE);
			}
			
			chargerTileset(); // Charge uniquement si tileset null.
			generer = true;   // Au cas où aucune partie lancée depuis le lancement de l'application.
			
			ois.close();
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
    protected void paintComponent(Graphics g) 
    {    	
		Point dest = new Point();
		Point src;

		if(!generer)
			return;
		
		/* Affichage de la carte. */
		for(int i = 0; i< IConfig.LARGEUR_CARTE; i++)
			for(int j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
				int num_tile = (int)carte[getNumCase(i, j)];
				
				dest.setLocation(i, j);
				src = tileset.getCoord(num_tile);
				tileset.dessiner(g, src, dest);
			}
		
		/* Case sélectionnée. */
		if(caseactionnee != -1 ){
			Point coord = getCoordCase(caseactionnee);
			int dx = coord.x;
			int dy = coord.y;
			
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					int dxc = dx + i;
					int dyc = dy + j;
					int caseVoisine = dyc * IConfig.LARGEUR_CARTE + dxc;
					
					if(existe(dxc, dyc) && tileset.getTile(carte[caseVoisine]).estPraticable() && !(soldat[caseVoisine] instanceof Heros))
						soldat[caseactionnee].dessineDeplacement(g, dxc, dyc, Color.RED);
				}
			}
			
			soldat[caseactionnee].dessineDeplacement(g, dx, dy, Color.BLUE);
		}
		
		/* Affichage des personnages. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null && soldat[i].estVisible())
			{
				int x = getCoordCase(i).x;
				int y = getCoordCase(i).y;
				
				soldat[i].dessiner(g, x, y);		
			}
		
		/* Affichage des barres de vie. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null && !soldat[i].estMort())
			{
				int x = getCoordCase(i).x;
				int y = getCoordCase(i).y;
				
				soldat[i].dessineVie(g, x, y);
				
				if(soldat[i] == soldat_pointe){
					g.drawString("coucou", y, x);
					
				}
			}
	}
    
	public void actionPerformed(ActionEvent e) 
	{	
		repaint();
	}
}
