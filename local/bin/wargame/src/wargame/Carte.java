package wargame;

import java.awt.Color;
import java.awt.Font;
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
import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import wargame.Charset.Direction;

/** Classe de la Carte du jeu. */
public class Carte extends JPanel implements ICarte, ActionListener, Serializable
{	
	private static final long serialVersionUID = 1845646587235566472L;

	protected static int nbHerosRestant;
	protected static int nbMonstresRestant;
	protected static int nbSoldatAJouer; 
	protected static int tour = 0;
	
	private CarteListener carteListener;
	
	/** Tileset de la carte. */
	private Tileset tileset;

	/** Brouillard. */
	protected char []brouillard;
	private boolean brouillardActive = true;
	
	/** Carte. */
	protected char []carte;

	/** Monstres. */
	private ArrayList<Monstre> monstre;

	/** Héros. */
	private ArrayList<Heros> heros;

	/** Table de jeu de la carte. */
	private Soldat []soldat;

	/** La carte est-elle générée ? */
	private boolean generee = false;

	/** Indique la case courante sélectionnée.
	 * Correspond également au soldat selectionné dans le combat.
	 */
	private int caseActionnee = -1;
	
	/** Indique le soldat actuellement pointé par le curseur de la souris */
	private Soldat soldatPointe = null;

	/** Image de présentation, quand on a pas encore généré la carte */
	private JLabel imagePresentation;

	/** Timer. */
	private Timer timer;
	
	/** Controle si on doit ou non afficher l'historique */
	private String afficheHistorique = "";
	
	/** Est-ce au tour de joueur ? */
	private boolean tourJoueur;
	
	/** Controle l'affichage ou non du message de victoire ou défaite */
	private String stringFinJeu = ""; 

	private boolean armagedon = false;
	private boolean ajoutMonstre = false;
	private boolean ajoutHeros = false;
	
	/** Constructeur par défaut. 
	 * @throws MidiUnavailableException 
	 * @throws IOException 
	 * @throws InvalidMidiDataException 
	 */
	Carte() throws InvalidMidiDataException, IOException, MidiUnavailableException
	{		
		/* Initialisation taux de rafraichissement. */
		timer = new Timer((int)(1000.0 * 1.0 / IConfig.FPS), this);
		timer.setInitialDelay(0);
		timer.start();	             
		
		/* Création d'une carte vide. */
		carte = new char [IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE];	

		/* Création du brouillard */
		brouillard = new char [IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE];
		
		/* Image de présentation, avant donc que la carte ne soit générée */
		imagePresentation = new JLabel(new ImageIcon( IConfig.CHEMIN_IMAGE + "image_presentation.png"));
		this.add(imagePresentation);

		/* Capture d'évènements de la souris. */
		addMouseListener(new MouseAdapter() {
			/* Au clic */
			public void mouseClicked(MouseEvent e) { 
				/* Si la carte n'est pas générée ou que ce n'est pas au tour du joueur alors il n'y a rien à faire donc on quitte */
				if(!generee || tourJoueur == false)
					return;
				
				/* On crée une position aux coordonnées du clic de la souris pour connaitre la coordonnée de la case (et non pas du pixel) */
				Position pos = new Position(e.getX() / IConfig.NB_PIX_CASE , e.getY() / IConfig.NB_PIX_CASE);
				
				int case_cliquee = pos.getNumCase();
				
				/* Pour le mode armagedon */
				if(armagedon && soldat[case_cliquee] != null && !soldat[case_cliquee].estMort()) {
					
					if(soldat[case_cliquee] instanceof Heros) 
						nbHerosRestant--;
					else 
						nbMonstresRestant--;
					
					if(nbMonstresRestant == 0) 
						joueurGagne();
					else if(nbHerosRestant == 0) 
						joueurPerd();
					
					soldat[case_cliquee].setMort(true);
					
					return;
				}
				
				/* Pour le cheat d'ajout de monstre */
				if( (ajoutMonstre || ajoutHeros) && soldat[case_cliquee] == null && tileset.getTile(carte[case_cliquee]).estPraticable() ) {
					try {
						if(ajoutMonstre) {
							nbMonstresRestant++;
							Monstre monstre_cree = new Monstre(ISoldat.TypesM.getTypeMAlea());
							monstre_cree.setDirection(Direction.DROITE);
							monstre.add(monstre_cree);
							soldat[case_cliquee] = monstre_cree;
						}
						else {
							nbHerosRestant++;
							Heros heros_cree = new Heros(ISoldat.TypesH.getTypeHAlea());
							heros_cree.setDirection(Direction.GAUCHE);
							
							heros.add(heros_cree);
							soldat[case_cliquee] = heros_cree;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
				}
				
				/* On vient de cliquer sur la même case : on veut se reposer */
				if(case_cliquee == caseActionnee && !soldat[case_cliquee].getAJoue()) {
					faitReposer(soldat[case_cliquee], true);
					nbSoldatAJouer--;
				}

				/* On change de héros si la case n'est pas vide, qu'il s'agit bien d'un soldat et que le soldat est affiché. */
				if(soldat[case_cliquee] != null 
					&& soldat[case_cliquee] instanceof Heros 
					&& soldat[case_cliquee].estVisible() 
				) {
					caseActionnee = case_cliquee;
				}
				/* Sinon on va faire une action en rapport avec le soldat sélectionné. */
				else {
					if(caseActionnee != -1) {
						/* Distance entre la case cliquée et la case actionnée */
						int distance = new Position(caseActionnee).distance(new Position(case_cliquee));

						/* Si on clique sur un monste alors combat */
						if(soldat[case_cliquee] instanceof Monstre 
							&& distance <= soldat[caseActionnee].getPortee() 
							&& !soldat[caseActionnee].getAJoue() 
							&& soldat[case_cliquee].estVisible()
							&& !soldat[case_cliquee].estMort() 
						) {
							if(obstacleEntreCase(soldat[caseActionnee].getPosition(), soldat[case_cliquee].getPosition())){
								System.out.println("Impossible d'attaque ce soldat, un élément bloque la flêche");
								return;
							}
						
							faitCombattre(soldat[caseActionnee], soldat[case_cliquee], distance);
							nbSoldatAJouer--;

							return;
						}

						/* On a un soldat selectionné et on clique sur une case autour (à une distance de 1 autour du soldat)
						 * -> On veut se déplacer sur la nouvelle case.
						 */
						if(distance == 1 
							&& tileset.getTile(carte[case_cliquee]).estPraticable() 
							&& ( soldat[case_cliquee] == null || soldat[case_cliquee].estMort() ) && !soldat[caseActionnee].getAJoue()
						) {
							 deplaceSoldat(soldat[caseActionnee],case_cliquee);
						}
					}
					/* Fin deplacement on re-initialise la case */
					caseActionnee = -1;
				}
			}
		});

		/* Écouteur permettant de savoir si le curseur pointe sur un soldat */
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				/* On vérifie que la carte a bien été générée */
				if(generee){
					/* Récupération de la case de la carte correspondant aux coordonnées du curseur */
					int num_case_curseur = new Position((e.getX() / IConfig.NB_PIX_CASE), (e.getY() / IConfig.NB_PIX_CASE)).getNumCase();

					/* Si il y a bien un soldat pointé, alors on met ce dernier dans l'objet soldatPointe, sinon on le met à null (pas de soldat pointé) */
					if(soldat[num_case_curseur] != null && soldat[num_case_curseur].estVisible() && (!brouillardActive || brouillard[num_case_curseur] > 0) )
						soldatPointe = soldat[num_case_curseur];
					else
						soldatPointe = null;
				}
			}
		});
	}
	
	/** 
	 * Regenere le brouillard du au déplacement et deplace le soldat
	 * @param sold Soldat a deplacer
	 * @param caseArrivee Case sur laquel finira le soldat
	 */
	public void deplaceSoldat(Soldat sold,int caseArrivee) {
		
		carteListener.historique(sold.getNom() + " se deplace en " + new Position(caseActionnee) );

		/* On supprime le brouillard du perso */
		changeBrouillard(sold.getPosition(), sold.getPortee() , -1);		
		
		soldat[caseArrivee] = soldat[sold.getPosition().getNumCase()];
		soldat[sold.getPosition().getNumCase()] = null;

		/* On déplace le soldat à la nouvelle position */
		deplaceSoldat(soldat[caseArrivee], new Position(caseArrivee));
		
		/* on recré le brouillard associé au perso */
		
		/* Petit cheat sur new Position(case_cliquee) ;
		 * étant donné que le mouvement n'est pas encore finis , 
		 * la position n'est pas mis a jours, cependant ont sait qu'il sera a la position case_cliquee
		 */
		changeBrouillard(new Position(caseArrivee), soldat[caseArrivee].getPortee() , 1);
	}
	
	/**
	 * Méthode permettant d'associer un listener
	 * @param l Le listener
	 */
	protected void onStateRealized(CarteListener l)
	{
	 this.carteListener = l;
	}

	/** 
	 * Méthode permettant de reinitialiser le tour de tous les soldats
	 */
	public void reinitAJoue() {
		if(isGeneree() != true)
			return;
		
		joueMonstres();
		caseActionnee = -1;
		tour++;

		carteListener.historique("Début du tour " + tour);

		for(int i = 0; i < IConfig.HAUTEUR_CARTE * IConfig.LARGEUR_CARTE; i++){
			if(soldat[i] != null) {
				if(soldat[i] instanceof Heros && !soldat[i].getAJoue() && soldat[i].estVisible && soldat[i].estMort() == false ) {
					faitReposer(soldat[i], false);
				}
				soldat[i].setAJoue(false);	
			}
		}
		
		nbSoldatAJouer = nbHerosRestant;
	}

	/**
	 *  Méthode chargeant un nouveau Tileset
	 */
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

	/**
	 *  Méthode générant la carte 
	 */
	private void genererCarte()
	{
		soldat = new Soldat[IConfig.LARGEUR_CARTE * IConfig.HAUTEUR_CARTE];
		chargerTileset();

		/* Couche d'herbe. */
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE; i++){
			Position p = new Position(i);
			tileset.setHerbe(this, p);
		}

		/* Rochers. */
		for(int i = 0; i < IConfig.NB_ROCHERS; i++)
		{
			/* Une position aléatoire est générée */
			Position p = new Position();
			
			tileset.setRocher(this, p);
		}

		/* Arbres. */
		for(int i = 0; i < IConfig.NB_ARBRES; i++)
		{
			Position p = new Position();

			if(!tileset.setArbre(this, p)) i--;
		}

		/* Paille. */
		for(int i = 0; i < IConfig.NB_PAILLES; i++)
		{
			Position p = new Position();

			if(!tileset.setPaille(this, p)) i--;
		}
		
		/* Eau. */
		for(int i = 0; i < IConfig.NB_EAU; i++)
		{
		     Position p = new Position();
		     if(!tileset.setEau(this, p)) i--;
		}
	}

	/**
	 *  Méthode générant tous les soldats 
	 */
	private void genererSoldats()
	{
		/* Mise à 0 de la carte. */
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.HAUTEUR_CARTE; i++)
			soldat[i] = null;

		/* Tableaux de soldats. */
		//heros = new Heros[IConfig.NB_HEROS];
		//monstre = new Monstre[IConfig.NB_MONSTRES];
		heros = new ArrayList<Heros>(); 
		monstre = new ArrayList<Monstre>(); 

		/* Chargement des soldats de base. */
		try {
			for(int i = 0; i < IConfig.NB_HEROS; i++)
			{
				heros.add(i, new Heros(ISoldat.TypesH.getTypeHAlea()));
				heros.get(i).setDirection(Direction.GAUCHE);

//				heros[i] = new Heros(ISoldat.TypesH.getTypeHAlea());
//				heros[i].setDirection(Direction.GAUCHE);
			}
			for(int i = 0; i < IConfig.NB_MONSTRES; i++)
			{
				monstre.add(i, new Monstre(ISoldat.TypesM.getTypeMAlea()));
				monstre.get(i).setDirection(Direction.GAUCHE);
//				monstre[i] = new Monstre(ISoldat.TypesM.getTypeMAlea());
//				monstre[i].setDirection(Direction.DROITE);
			}
		} 
		catch(IOException e) {
			System.out.println(e);
			return;
		}	

		/* Positionnement des soldats. */
		for(int i = 0; i < IConfig.NB_HEROS; i++) {
			Position pos = new Position(this.trouvePositionVide(Soldat.HEROS));
			soldat[pos.getNumCase()] = heros.get(i);
//			soldat[pos.getNumCase()] = heros[i];
			soldat[pos.getNumCase()].setPosition(pos);
		}

		for(int i = 0; i < IConfig.NB_MONSTRES; i++) {
			Position pos = new Position(this.trouvePositionVide(Soldat.MONSTRE));
			soldat[pos.getNumCase()] = monstre.get(i);
//			soldat[pos.getNumCase()] = monstre[i];
			soldat[pos.getNumCase()].setPosition(pos);
		}
	}

	/**
	 *  Méthode générant et initialisant le brouillard 
	 */
	private void genererBrouillard() {

		/*initialisation du brouillard [ toutes les cases sont a 0 */
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE; i++)
			brouillard[i] = 0;
		
		/* Ensuite pour chaque héros [ présent dans le tableau héros on crée le brouillard associé */
		for(int i = 0; i < IConfig.NB_HEROS; i++) {
			Heros h = heros.get(i);
			if(h.estVisible())
				changeBrouillard(h.getPosition(), h.getPortee(), 1);
		}
//			if(heros[i].estVisible()) /* seulement si il est visible */
//				changeBrouillard(heros[i].getPosition(), heros[i].getPortee(), 1);

	}
	
	/** Méthode permetant de crée ( ou retirer ) le brouillard autour d'un soldat
	 * 
	 * @param numCase Position du soldat
	 * @param distance Distance a laquel le soldat peut voir
	 * @param inc Ici plusieurs cas :
	 * 					- Si inc = 1, alors on crée du brouillard
	 * 					- sinon si inc = -1, on retire du brouillard
	 */
	private void changeBrouillard(Position pos, int distance, int inc) {
		int i = 0;
		int j = 0;
		
		Position tmp = null;
		brouillard[pos.getNumCase()] += inc;
		for(j = distance; j >= - distance ; j--) 
			for(i = distance ; i >= -distance ; i--) {
				tmp = new Position(pos.x + i ,pos.y + j);
				if(tmp.estValide())
					brouillard[tmp.getNumCase()] += inc;
			}
		
	}
	/** Déplace un soldat sur la carte.
	 * @param soldat    Soldat à deplacer.
	 * @param direction Direction du soldat.
	 * @param x         Offset X d'origine.
	 * @param y         Offset Y d'origine.
	 */
	private void deplaceSoldat(Soldat soldat, Position nouvelle_position)
	{
		Son.joueCourir();
		soldat.setAJoue(true);
		nbSoldatAJouer--;
		int sx = soldat.getPosition().x;
		int sy = soldat.getPosition().y;
		int dx = nouvelle_position.x;
		int dy = nouvelle_position.y;
		int x = 0;
		int y = 0;

		Direction direction = Direction.HAUT;

		if(dx > sx) {
			x = 2;
			direction = Direction.DROITE;
		}
		else if(dx < sx) {
			x = -2;
			direction = Direction.GAUCHE;
		}
		if(dy > sy) {
			y = 2;
			direction = Direction.BAS;
		}
		else if(dy < sy) {
			y = -2;
			direction = Direction.HAUT;
		}

		soldat.setSeDeplace(true);
		soldat.setDirection(direction);

		soldat.offsetX = x;
		soldat.offsetY = y;
	}


	/**
	 *  Retourne la position du premier héros trouvé aux alentours de p. Null si il n'y en as pas 
	 *  @param pos Position du soldat où chercher aux alentours
	 *  @param portee Nombre de cases du rayon où on cherche
	 */
	private Position herosAlentour(Position p, int portee){
		for(int x = p.x - portee; x <= p.x + portee; x++){
			for(int y = p.y - portee; y <= p.y + portee; y++){
				Position pos = new Position(x, y);

				if(pos.estValide()){
					/* Si il y a un héros à la case indiquée, qu'il est visible et pas mort */
					if(soldat[pos.getNumCase()] != null 
						&& soldat[pos.getNumCase()] instanceof Heros 
						&& soldat[pos.getNumCase()].estVisible() && soldat[pos.getNumCase()].estMort() == false
						&& !obstacleEntreCase(pos, p)
					){
						return pos;
					}
				}
			}
		}

		return null;
	}

	/**
	 *  Déplace tous les monstres 
	 */
	public void joueMonstres()
	{
		carteListener.deplaceMonstre();
		tourJoueur = false;
			
//		for(int i=0; i < monstre.length; i++) 
		for(int i=0; i < monstre.size(); i++) 
		{
			Monstre m = monstre.get(i);
//			Monstre m = monstre[i];
					
			if(m != null && !m.estMort() && m.estVisible())
			{					
				Position p;
				/* Repos */
				if(m.getPourcentageVie() < 10)
					m.repos(true);
				/* Combat avec un héros aux alentours */
				else if((p = herosAlentour(m.getPosition(), m.getPortee())) != null){
					if(faitCombattre(m, soldat[p.getNumCase()], p.distance(m.getPosition())))
						return;
				}
				/* Sinon déplacement */
				else {							
					Position positions[] = new Position[9];
					Position temp = new Position();
					
					int nb_position_trouve = 0;
					
					/* On parcourt toutes les positions possibles autour du soldat */
					for(int x = -1; x <= 1; x++)
						for(int y = -1; y <= 1; y++)							
						{					
							temp.x = m.getPosition().x + x;
							temp.y = m.getPosition().y + y;
							
							/* Si la position est valide (dans la carte)
							 * Et si il n'y a pas déjà de soldat sur cette position
							 * Et si la case est praticable (pas d'arbre ou de rocher)
							 * Le soldat ne pourra pas fais du surplace car soldat[temp.getNumCase()] ne renverra pas null */
							if (temp.estValide() && soldat[temp.getNumCase()] == null && 
									tileset.getTile(carte[temp.getNumCase()]).estPraticable())
							{
								positions[nb_position_trouve] = new Position(temp.getNumCase());
								nb_position_trouve++;
							}
						}
					
					/* Si on a trouvé des positions valides, on en choisit une au hasard */
					if(nb_position_trouve > 0)
					{
						temp = positions[Aleatoire.nombreAleatoire(0, nb_position_trouve - 1)];
						soldat[m.getPosition().getNumCase()] = null;
						soldat[temp.getNumCase()] = m;
						deplaceSoldat(m, temp);
					}
				}
		
			}
		}
		
	   Thread t = new Thread() {
	          public void run() {
//	      		for(int i = 0; i < monstre.length; i++)
//	    			while(monstre[i].getSeDeplace() || Infobulle.aTermine() == false)
		        for(int i = 0; i < monstre.size() ; i++)
		        	while(monstre.get(i).getSeDeplace() || Infobulle.aTermine() == false)
	    			{
	    				try {
							Thread.currentThread();
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
	    			}
	    		
	    		tourJoueur = true;
	    		
	    		carteListener.deplaceMonstre();
	          }
	        };
	       
	   t.start();

	}

	/** Genere aléatoirement une carte. */
	public void generer()
	{
		generee = true;
		tourJoueur = true;
		nbMonstresRestant = IConfig.NB_MONSTRES;
		nbHerosRestant = IConfig.NB_HEROS;
		nbSoldatAJouer = nbHerosRestant - 1;
		
		stringFinJeu = "";
		genererCarte();
		genererSoldats();
		genererBrouillard();
		caseActionnee = -1;	

		if(imagePresentation != null){
			imagePresentation.getParent().remove(imagePresentation);
			imagePresentation = null;
		}
	}

	/** Trouve une position vide aléatoirement sur la carte. 
	 * Utilisable pour placer des Soldats.
	 * @param type Type de Soldat (Soldat.HEROS ou Soldat.MONSTRE)
	 * @return     La position vide.
	 * */
	public Position trouvePositionVide(char type)
	{	
		int dec = type == Soldat.HEROS ? 1 : 0;
		int num_tile;
		Tile tile;
		Position pos = new Position();

		do {
			pos.x = dec * (IConfig.LARGEUR_CARTE / 2) + dec + (int)(Math.random() * (IConfig.LARGEUR_CARTE / 2));
			pos.y = (int)(Math.random() * IConfig.HAUTEUR_CARTE);


			num_tile = carte[pos.getNumCase()];
			tile = tileset.getTile(num_tile);
		} while(!pos.estValide() || soldat[pos.getNumCase()] != null || !tile.estPraticable());

		return pos;
	}

	/** Sauvegarde une carte.
	 * @param chemin Chemin vers le fichier de sauvegarde.
	 */
	public void sauvegarde(String chemin)
	{
		try {
				FileOutputStream fichier = new FileOutputStream(chemin);
				ObjectOutputStream oos = new ObjectOutputStream(fichier);
				
				oos.write(tour);
				oos.write(nbHerosRestant);
				oos.write(nbMonstresRestant);
				oos.writeObject(carte);
				oos.writeObject(monstre);
				oos.writeObject(heros);
				oos.writeObject(soldat);
				oos.writeObject(brouillard);
	
				oos.flush();
				oos.close();
		}
		catch(java.io.IOException e) {
			e.printStackTrace();
		}
	}

	/** Charge une carte.
	 * @param chemin Chemin vers la sauvegarde a charger.
	 */
	@SuppressWarnings("unchecked")
	public void charge(String chemin)
	{

		if(imagePresentation != null){
			imagePresentation.getParent().remove(imagePresentation);
			imagePresentation = null;
		}

		try {
			FileInputStream fichier = new FileInputStream(chemin);
			ObjectInputStream ois = new ObjectInputStream(fichier);

			tour = (int)ois.read();
			nbHerosRestant = (int)ois.read();
			nbMonstresRestant = (int)ois.read();
			carte   = (char[])ois.readObject();
			monstre = (ArrayList<Monstre>)ois.readObject();
			heros   = (ArrayList<Heros>)ois.readObject();
			soldat  = (Soldat[])ois.readObject();
			brouillard = (char[])ois.readObject();

			/* Les images ne sont pas sérializées... */
			for(int i = 0; i < IConfig.NB_HEROS; i++)
			{
				heros.get(i).setImage();
				heros.get(i).setDirection(Direction.GAUCHE);
			}

			for(int i = 0; i < IConfig.NB_MONSTRES; i++)
			{
				monstre.get(i).setImage();
				monstre.get(i).setDirection(Direction.DROITE);
			}
			chargerTileset(); // Charge uniquement si tileset null.
			generee = true;   // Au cas où aucune partie lancée depuis le lancement de l'application.

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

		if(!generee)
			return;

		/* Affichage de la carte. */
		for(int i = 0; i< IConfig.LARGEUR_CARTE; i++)
			for(int j = 0; j < IConfig.HAUTEUR_CARTE; j++) {
				Position pos = new Position(i, j);
				int num_tile = (int)carte[pos.getNumCase()];

				dest.setLocation(i, j);
				src = tileset.getCoord(num_tile);
				tileset.dessiner(g, src, dest);
			}

		/* Case sélectionnée. */
		if(caseActionnee != -1){
			Position coord = new Position(caseActionnee);
			int dx = coord.x;
			int dy = coord.y;
			
			if(!(soldat[caseActionnee] instanceof Heros))
				return;
				
			if(soldat[caseActionnee].getAJoue())
				dessineRectangle(g, dx, dy, IConfig.SOLDAT_UTILISE);
			else {
				for(int i = -1; i <= 1; i++) {
					for(int j = -1; j <= 1; j++) {
						int dxc = dx + i;
						int dyc = dy + j;
						int caseVoisine = dyc * IConfig.LARGEUR_CARTE + dxc;

						if(new Position(dxc, dyc).estValide() && tileset.getTile(carte[caseVoisine]).estPraticable() && soldat[caseVoisine] == null)
							dessineRectangle(g, dxc, dyc, IConfig.SOLDAT_DEPLACEMENT_POSSIBLE);
					}
				}

				dessineRectangle(g, dx, dy, IConfig.SOLDAT_SELECTIONNEE);
			}
		}

		/* Affichage des personnages. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null && soldat[i].estVisible())
			{
				Position pos = soldat[i].getPosition();
				soldat[i].dessinerAvecOffset(g, pos.x, pos.y, soldat[i].offsetX, soldat[i].offsetY);
			}

		/* Affichage des barres de vie. */
		for(int i = 0; i < soldat.length; i++)
			if(soldat[i] != null && !soldat[i].estMort())
				soldat[i].dessineVie(g);

		/* Affichage du brouillard, que l'on affiche avant l'infobulle */
		dessinerBrouillard(g, brouillard);
		
		if(afficheHistorique != "") {
			Infobulle.dessinerText(g, IConfig.LARGEUR_CARTE, IConfig.HAUTEUR_CARTE, afficheHistorique, Color.BLUE, Color.LIGHT_GRAY );
		}
		/* Affichage de l'infobulle si un soldat est pointé */
		if(soldatPointe != null ){ 
			Position pos = soldatPointe.getPosition();
			if(soldatPointe instanceof Heros)
				Infobulle.dessinerText(g, pos.x, pos.y, soldatPointe.toString(), IConfig.MESSAGE_INFOBULLE, IConfig.ARRIERE_PLAN_HEROS);
			else
				Infobulle.dessinerText(g, pos.x, pos.y, soldatPointe.toString(), IConfig.MESSAGE_INFOBULLE, IConfig.ARRIERE_PLAN_MONSTRE);
		}

		/* Auto gestion de l'affichage de la file de message */
		Infobulle.dessiner(g);
		if(!stringFinJeu.isEmpty()) {
			g.setFont(new Font("calibri", Font.BOLD, 100));
			g.drawString(stringFinJeu, (IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE ) - (int)( g.getFontMetrics().stringWidth(stringFinJeu) * 1.25) , (IConfig.HAUTEUR_CARTE  * IConfig.NB_PIX_CASE )/ 2);

		}
		carteListener.information(nbMonstresRestant + " Monstres restant - " + nbHerosRestant + " Heros restant");
	}

	public void actionPerformed(ActionEvent e) 
	{	

		if(nbSoldatAJouer == 0 && nbHerosRestant > 0)	
			reinitAJoue();
		
		repaint();
	}

	/** 
	 * Dessine un rectangle de hauteur IConfig.NB_PIX_CASE de la couleur c indiquée
	 * @param g Graphics dans lequel on va dessiner le rectangle
	 * @param x Point de départ x du rectangle
	 * @param y Point de départ y du rectangle
	 * @param c Couleur du rectangle a dessiner
	 */
	protected void dessineRectangle(Graphics g, int x, int y, Color c) 
	{
		g.setColor(c);
		g.fillRect(x * IConfig.NB_PIX_CASE, y * IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
	}
	
	/** 
	 * Méthode dessinant le brouillard 
	 * @param g Graphics ou sera dessiner le brouillard
	 * @param brouillard tableau de char contenant le brouillard 
	 */
	protected void dessinerBrouillard(Graphics g, char[] brouillard) {
		for(int i = 0; i < IConfig.LARGEUR_CARTE * IConfig.LARGEUR_CARTE; i++) {
			if(brouillard[i] == 0 && brouillardActive) {
				Position coord = new Position(i);
				dessineRectangle(g, coord.x, coord.y, IConfig.COULEUR_BROUILLARD);
			}
		}
	}

	
	/**
	 * Méthode permettant de savoir si la carte a été générée ou non
	 * @return vrai si elle a été générée, faux sinon
	 */
	public boolean isGeneree(){
		return this.generee;
	}
	
	
	public void joueurGagne(){
		this.carteListener.joueurGagne();
		
		/* Le joueur ne peut plus jouer */
		tourJoueur = false;
		carteListener.historique("Vous avez gagné !");
		stringFinJeu = "You win !";
		System.out.println("Vous avez gagné !");
	}
	
	public void joueurPerd(){
		this.carteListener.joueurPerd();
		
		/* Le joueur ne peut plus jouer */
		tourJoueur = false;
		caseActionnee = -1;
		brouillardActive = false;
		carteListener.historique("Vous avez perdu !");
		stringFinJeu = "Game Over !";
		System.out.println("Vous avez perdu !");
	}
	
	/**
	 * Méthode permettant de faire combattre 2 soldats et vérifie ensuite si une armée a gagné
	 * @param attaquant Le soldat qui attaque
	 * @param defenseur Le soldat qui défend
	 * @param distance La distance qui sépare les deux soldats
	 * @return Vrai si une des deux armées a gagné, faux sinon
	 */
	public boolean faitCombattre(Soldat attaquant, Soldat defenseur, int distance){
		boolean retour = false;
		
		/* On commence à faire tourner l'attaquant dans la direction du defenseur */
		attaquant.setDirection(attaquant.getPosition().direction(defenseur.getPosition()));
		
		int v = attaquant.combat(defenseur, distance);
		
		if(attaquant instanceof Monstre){
			if(v == -1){
				nbMonstresRestant--;	
			}
			else if(v == 1) {
				nbHerosRestant--;
				caseActionnee = -1;
				changeBrouillard(defenseur.getPosition(), defenseur.getPortee() , -1);
			}
		}
		else{
			if(v == -1) {
				nbHerosRestant--;
				caseActionnee = -1;
				changeBrouillard(attaquant.getPosition(), attaquant.getPortee() , -1);
			}
			else if(v == 1){
				nbMonstresRestant--;
			}
		}
		
		if(nbMonstresRestant <= 0){
			joueurGagne();	
			retour = true;
		}
		else if(nbHerosRestant <= 0){
			joueurPerd();
			retour = true;
		}
		
		return retour;
	}
		
	/** 
	 * Change la valeur de la caseActionnée en fonction du numéro d'un héros
	 * @param numHeros Case Actuel du héros séléctionnée
	 * @return numéro de la case du nouveau héros
	 */
	public int trouverProchainHeros(int num_heros) {
		if(++num_heros >= nbHerosRestant)
			num_heros = 0;
		
		// La boucle s'arretera car , si on a plus de héros qui peuvent jouer, le tour s'arrete
		// si jamais on veut avoir tout les perso, retirer entierement la boucle si dessous
		while(heros.get(num_heros).getAJoue())
			if(++num_heros >= nbHerosRestant)
				num_heros = 0;
		
		caseActionnee = heros.get(num_heros).getPosition().getNumCase();
		
		return num_heros;
	}
	

	/**
	 * Méthode permettant de savoir s'il y a un obstacle entre deux positions en prenant un chemin linéaire
	 * @param position1 La position
	 * @param position2 L'autre position
	 * @return Vrai si il y a un obstacle, faux sinon
	 */
	public boolean obstacleEntreCase(Position position1, Position position2){
		boolean obstacle = false;
		
		/* On prend la coordonnée haut gauche du pixel de la case position */
		Point p1 = position1.getCoordPixel();
		Point p2 = position2.getCoordPixel();
		
		/* On se place au centre de la case */
		p1.x -= IConfig.NB_PIX_CASE/2;
		p1.y -= IConfig.NB_PIX_CASE/2;
		p2.x -= IConfig.NB_PIX_CASE/2;
		p2.y -= IConfig.NB_PIX_CASE/2;	
		
		/* Si les deux positions sont sur le même axe vertical, on vérifie juste les cases une à une entre les deux positions */
		if(position1.x == position2.x){
			
			/* On choisit la position la plus en dessous de l'autre */
			Position dessus, dessous;
			if(position1.y < position2.y){
				dessus = position2;
				dessous = position1;
			}
			else{
				dessus = position1;
				dessous = position2;
			}
			
			/* On se place sur la case juste au dessus de la position du dessous */
			for(int y=dessous.y + 1; y<dessus.y && obstacle == false; y++){
				/* Et on vérifie que chaque case entre les deux positions est traversable */
				Position position_en_cours = new Position(dessus.x, y);
				int num_tile = carte[position_en_cours.getNumCase()];
				Tile tile = tileset.getTile(num_tile);
				
				if(!position_en_cours.estValide() || soldat[position_en_cours.getNumCase()] != null || !tile.estTraversable()){
					obstacle = true;
				}
			}
		}
		else{
			/* On regarde la position des deux points l'un par rapport à l'autre */
			Point droite, gauche;
			if(p1.x < p2.x){
				droite = p2;
				gauche = p1;
			}
			else{
				droite = p1;
				gauche = p2;
			}
			
			/* On construit l'équation de notre fonction, rappelons que y = ax + b */
			double a = (double)(droite.y - gauche.y)/(double)(droite.x - gauche.x);
			double b = gauche.y - a * gauche.x;
						
			/* Pour chaque pixel entre les deux positions */
			for(int x = gauche.x; x < droite.x; x++){
				/* On calcule le y */
				int y = (int) (a * x + b);
				
				/* On calcule dans quelle case est le pixel */
				double x_case = Math.round((double)x / (double)IConfig.NB_PIX_CASE);
				double y_case = Math.round((double)y / (double)IConfig.NB_PIX_CASE);
								
				Position position_en_cours = new Position((int)x_case, (int)y_case);	
				
				int num_tile = carte[position_en_cours.getNumCase()];
				Tile tile = tileset.getTile(num_tile);

				/* Et on vérifie que cette case est traversable.
				 * Il faut aussi vérifier que le pixel n'est pas dans la case d'une des deux positions (car on ne veut voir si il n'y a pas d'obstacle seulement ENTRE les deux positons)
				 */
				if(!position_en_cours.equals(position1) && !position_en_cours.equals(position2) && 
					(!position_en_cours.estValide() || soldat[position_en_cours.getNumCase()] != null || !tile.estTraversable())){
					obstacle = true;
				}
			}
		}
		
		return obstacle;
	}
	
	/**
	 * Méthode permettant de faire reposer un soldat avec évènement, ce dernier précisant dans une phrase le regain du joueur
	 * @param s Le soldat à reposer
	 * @param afficher_message Afficher un message sur la carte ? (infobulle)
	 */
	public void faitReposer(Soldat s, boolean afficher_message){
		int regain = s.repos(afficher_message);
		
		if(regain == -1){
			carteListener.historique(s.getNom()+ " " + s.getPosition().toString() + " a sa vie au maximum");
		}
		else{
			carteListener.historique(s.getNom() + " se repose et regagne " + regain + " points de vie");
		}
	}
	
	/* Rappel sur l'ordre du tableau :
	 * 	HAUT, BAS, GAUCHE, DROITE
	 */
	/** Change la position d'un soldat en fonction des touches du tableau définit dans FenetreJeu */
	public void changePos(boolean[] tabKey) {
		if(caseActionnee == -1 || !generee )
			return;
			
		if(!(soldat[caseActionnee] instanceof Heros))
			return;
			
		Position pos = soldat[caseActionnee].getPosition();
		Position caseArrivee = null;
		
		if(tabKey[0]) {			// Si on a haut
			if(tabKey[2]) 		{ caseArrivee = new Position(pos.x - 1, pos.y - 1); } 	// Si Haut - Gauche
			else if(tabKey[3]) 	{ caseArrivee = new Position(pos.x + 1, pos.y - 1); } 	// Si Haut - Droite
			else				  caseArrivee = new Position(pos.x, pos.y - 1);  		// Si Haut
		}
		else if(tabKey[1]) {	// Sinon si on a bas
			if(tabKey[2]) 		{ caseArrivee = new Position(pos.x - 1, pos.y + 1); } 	// Si Bas - Gauche
			else if(tabKey[3]) 	{ caseArrivee = new Position(pos.x + 1, pos.y + 1); } 	// Si Bas - Droite
			else				  caseArrivee = new Position(pos.x, pos.y + 1);			// Si Bas
		}
		else if(tabKey[2]) 		  caseArrivee = new Position(pos.x - 1, pos.y);			// Si Gauche
		else if(tabKey[3]) 		  caseArrivee = new Position(pos.x + 1, pos.y);			// Si Droite	
		else					  return;
		
		if( caseArrivee.estValide() 
				&& tileset.getTile(carte[caseArrivee.getNumCase()]).estPraticable() 
				&& soldat[caseArrivee.getNumCase()] == null
				&& !soldat[caseActionnee].getAJoue()) {
			deplaceSoldat(soldat[caseActionnee], caseArrivee.getNumCase());
			caseActionnee = -1;
		}
	}
	

	public void setAffichageHistorique(String affichage) {
		afficheHistorique = affichage;
	}
		
	public void setBrouillardActive(boolean active) {
		brouillardActive = active;
	}
	
	public boolean getBrouillardActive() {
		return brouillardActive;
	}

	public void setArmagedon(boolean active) {
		armagedon = active;
	}
	
	public boolean getArmagedon() {
		return armagedon;
	}
	
	public void setAjoutMonstre(boolean active) {
		ajoutMonstre = active;
	}
	
	public boolean getAjoutMonstre() {
		return ajoutMonstre;
	}
	
	public void setAjoutHeros(boolean active) {
		ajoutHeros = active;
	}
	
	public boolean getAjoutHeros() {
		return ajoutHeros;
	}
}

