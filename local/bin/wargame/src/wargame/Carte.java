package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	private static final long serialVersionUID = 1845646587235566472L;

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
	
	/** Indique la case courante sélectionnée.
	 * Correspond également au soldat selectionné dans le combat.
	 */
	private int caseactionnee = -1;
	
	/** Message a afficher */
	private String message = "";
	private Color couleurMessage = Color.BLACK;;
	
	/** Indique le soldat actuellement pointé par le curseur de la souris */
	private Soldat soldatPointe = null;
	
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
			if(!generer)
				return;
				int caseclick = getNumCase(e.getX() / IConfig.NB_PIX_CASE , e.getY() / IConfig.NB_PIX_CASE);
									
				if(caseclick == caseactionnee && !soldat[caseclick].getAJoue()) {
					int regen = alea(1,IConfig.REGEN_MAX);
					int vie = soldat[caseclick].getVie();
					
					if(vie == soldat[caseclick].getVieMax()) {
						message = "Repos Impossible\nVie max atteinte";
						couleurMessage = IConfig.MESSAGE_NEUTRE;
						return;
					}
					
					soldat[caseclick].setAJoue(true);
					soldat[caseclick].setVie(vie + regen);
					message = "+"+regen+"PV";
					couleurMessage = IConfig.MESSAGE_POSITIF;
					//Infobulle.dessiner(g, getint from 1 over 4 to 1 y_1 + y_2 dy_2 dy_1CoordCase(soldat[caseclick].getNumCase()).x, getCoordCase(soldat[caseclick].getNumCase()).y, "REPOS GAIN DE : "+regen+" PV", Color.RED);
					//System.out.println("REPOS GAIN DE : "+regen+" PV");
				}
	
				if(soldat[caseclick] != null && soldat[caseclick] instanceof Heros && soldat[caseclick].estVisible() ) {
					//if(!soldat[caseclick].getAJoue())
					caseactionnee = caseclick;
				}
				else {
					/* MERCI DE NE PAS TOUCHER PEU IMPORTE LA MODIF */
					//HESITATION ICI EN PARLER 
					if(caseactionnee != -1) {
						int distance = getDistance(caseactionnee,caseclick);
						if(soldat[caseclick] instanceof Monstre && distance <= soldat[caseactionnee].getPortee()) {
							if(distance == 1) {
								message = "- 5 Pv";
								couleurMessage = IConfig.MESSAGE_NEGATIF;
							}
							else {
								message = "- 10 Pv"	;
								couleurMessage = IConfig.MESSAGE_NEGATIF;
							}
						}
						//Deplacement temporaire
						//Si la distance a laquel on a cliqué est de 1 et que la case est praticable , qu'il n'y a ni monstre ni heros dessus et que le soldat a pas joué.
						if(distance == 1 && tileset.getTile(carte[caseclick]).estPraticable() && soldat[caseclick] == null && !soldat[caseactionnee].getAJoue() ) {
							
							soldat[caseactionnee].setAJoue(true);
							soldat[caseclick] = soldat[caseactionnee];
							soldat[caseactionnee] = null;
								
							int sx = getCoordCase(caseactionnee).x;
							int sy = getCoordCase(caseactionnee).y;
							int dx = getCoordCase(caseclick).x;
							int dy = getCoordCase(caseclick).y;
							int x = 0;
							int y = 0;
							
							char direction = Charset.HAUT;
							
							if(dx > sx) {
								x = 2; 
								direction = Charset.DROITE;
							}
							else if(dx < sx) {
								x = -2;
								direction = Charset.GAUCHE;
							}
								if(dy > sy) {
								y = 2; 
								direction = Charset.BAS;
							}
							else if(dy < sy) {
								y = -2;
								direction = Charset.HAUT;
							}
							
							deplaceSoldat(soldat[caseclick], direction, x, y);
						}
					}
					caseactionnee = -1;
					message = "";
					couleurMessage = Color.BLACK;
					/* MERCI DE NE PAS TOUCHER PEUT IMPORTE LA MODIF */
				}
			}
		});
		
		/* Écouteur permettant de savoir si le curseur pointe sur un soldat */
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				/* On vérifie que la carte a bien été générée */
				if(generer){
					/* récupération de la case de la carte correspondant aux coordonnées du curseur */
					int coord_curseur = (e.getX() / IConfig.NB_PIX_CASE) + 
						    IConfig.LARGEUR_CARTE * (e.getY() / IConfig.NB_PIX_CASE);
					
					if(soldat[coord_curseur] != null && soldat[coord_curseur].estVisible())
						soldatPointe = soldat[coord_curseur];
					else
						soldatPointe = null;	
				}
			}
		});
	}
	
	
	/* Méthode récupérant la distance (en terme de nombre de cases) entre case1 et case2 */
	private int getDistance(int case1, int case2)
	{
		if (case1 == -1) return 0;
		Point coordCase1 = getCoordCase(case1);	
		Point coordCase2 = getCoordCase(case2);
		
		int dx = Math.abs(coordCase1.x - coordCase2.x);
		int dy = Math.abs(coordCase1.y - coordCase2.y);
	
		if(dx == 1 && dy == 1)
			return 1;
		
		return dx + dy;
	}
	
	/* Méthode récupérant le numéro de la case correspondant aux coordonnées x et y d'une case */
	private int getNumCase(int x, int y)
	{
		return x + IConfig.LARGEUR_CARTE * y;	
	}
	
	/* Méthode donnant les coordonnées de la case numéro numCase */
	private Point getCoordCase(int numCase)
	{
		return new Point(numCase % IConfig.LARGEUR_CARTE, numCase / IConfig.LARGEUR_CARTE);
	}
	
	/* Méthode chargeant un nouveau Tileset */
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
	
	/* Méthode générant la carte */
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
	
	/* Méthode générant tous les soldats */
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
	 * @param soldat    Soldat à deplacer.
	 * @param direction Direction du soldat.
	 * @param x         Offset X d'origine.
	 * @param y         Offset Y d'origine.
	 */
	void deplaceSoldat(Soldat soldat, char direction, int x, int y)
	{
		soldat.setSeDeplace(true);
		soldat.setDirection(direction);
		
		soldat.offsetX = x;
		soldat.offsetY = y;
	}

	/** Genere aléatoirement une carte. */
	public void generer()
	{
		generer = true;
		genererCarte();
		genererSoldats();
		caseactionnee = -1;		
	}
	
	/** Teste si une case existe sur la Carte.
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
			
			/* Les images ne sont pas sérializées... */
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
			
			if(soldat[caseactionnee].getAJoue())
				soldat[caseactionnee].dessineDeplacement(g, dx, dy, IConfig.SOLDAT_UTILISE, IConfig.DEFAULT_ALPHA);
			else {
				for(int i = -1; i <= 1; i++) {
					for(int j = -1; j <= 1; j++) {
						int dxc = dx + i;
						int dyc = dy + j;
						int caseVoisine = dyc * IConfig.LARGEUR_CARTE + dxc;
						
						if(existe(dxc, dyc) && tileset.getTile(carte[caseVoisine]).estPraticable() && !(soldat[caseVoisine] instanceof Heros))
							soldat[caseactionnee].dessineDeplacement(g, dxc, dyc, IConfig.SOLDAT_DEPLACEMENT_POSSIBLE, IConfig.DEFAULT_ALPHA);
					}
				}
				
				soldat[caseactionnee].dessineDeplacement(g, dx, dy, IConfig.SOLDAT_SELECTIONNEE, IConfig.DEFAULT_ALPHA);
			}
		}
		
		/* Affichage des personnages. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null && soldat[i].estVisible())
			{
				Point p = getCoordCase(soldat[i].getNumCase());
				
				soldat[i].dessinerAvecOffset(g, p.x, p.y, soldat[i].offsetX, soldat[i].offsetY);
			}
		
		/* Affichage des barres de vie. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null && !soldat[i].estMort())
			{
				Point p = getCoordCase(soldat[i].getNumCase());
				
				soldat[i].dessineVie(g, p.x, p.y);
			}
		
		/* Affichage de l'infobulle si un soldat est pointé */
		if(soldatPointe != null){ 
			Point p = getCoordCase(soldatPointe.getNumCase());
			Infobulle.dessiner(g, p.x, p.y, soldatPointe.toString(), Color.BLACK, IConfig.MESSAGE_NEUTRE);
		}
		
		if (message != null)
			Infobulle.dessiner(g, getCoordCase(caseactionnee).x , getCoordCase(caseactionnee).y, message, couleurMessage, Color.YELLOW); // Attention, redéfinir les couleurs car je l'ai modifié
	}
    
	public void actionPerformed(ActionEvent e) 
	{	
		repaint();
	}
	
	public int alea (int min , int max) {
		return min + (int)(Math.random() * (max - min + 1));
	}
}
