package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;


/**
 * Classe abstraite représentant la base d'un soldat
 */
public abstract class Soldat extends Charset implements ISoldat
{
	private static final long serialVersionUID = 1L;

	protected Point coord = null;
	
	/** Informations de base d'un soldat. */
	protected int vieMax;
	protected int vie, portee, puissance, tir;
	protected String nom;
	
	/** Numéro de la case où se situe le soldat. */
	private Position position;

	/** Est mort ? */
	private boolean mort = false;
	
	/** En train de se deplacer. */
	private boolean seDeplace = false;
	
	/** Le tour est effectué */
	private boolean tourEffectue = false;
	
	/** Offset utilisé pendant le déplacement. */
	protected int offsetX = 0;
	protected int offsetY = 0;

	Soldat() {}
	
	public int getVie() 
	{
		return vie;
	}
	
	public double getPourcentageVie(){
		return ((double)(vie/(double)vieMax)) * 100.;
	}
	
	public int getVieMax() 
	{
		return vieMax;
	}

	public void setVie(int vie) 
	{
		this.vie = ((vie > vieMax) ? vieMax : (vie < 0 ) ? 0 : vie);
	}
	
	public int getPortee() 
	{
		return portee;
	}

	public void setPortee(int portee) 
	{
		this.portee = portee;
	}

	public int getPuissance() 
	{
		return puissance;
	}

	public void setPuissance(int puissance) 
	{
		this.puissance = puissance;
	}

	public int getTir() 
	{
		return tir;
	}

	public void setTir(int tir) 
	{
		this.tir = tir;
	}
		
	public void setSeDeplace(boolean value)
	{
		this.seDeplace = value;
	}
	
	public boolean getSeDeplace()
	{
		return this.seDeplace;
	}
	
	/** Mettre le statut du personnage à mort.
	 * @param mort Vrai si le personnage est mort, faux sinon
	 */
	public void setMort(boolean mort) {
		if(mort){
			this.mort = true;
			this.direction = Direction.HAUT;
			this.animation = 0;
		    this.timer.setDelay(350);
		}
		else{
			this.mort = false;
		}
	}
	
	/** Teste si le personnage est mort. 
	 * @return true si mort, false sinon.
	 */
	public boolean estMort() {
		return mort;
	}

	/**
	 * Teste si le soldat a déjà joué
	 * @return Vrai si il a déjà joué, faux sinon
	 */
	public boolean getAJoue() {
		return tourEffectue;
	}
	
	/**
	 * Permet de dire si le soldat a joué ou non
	 * @param value Vrai si le soldat a déjà joué, faux sinon
	 */
	public void setAJoue(boolean value) {
		tourEffectue = value;
	}
	
	/** Met à jour le statut du soldat.
	 *  @param e Évènement appellant, le timer.
	 */
    public void actionPerformed(ActionEvent e)
    {    
    	/* Mise à jour du déplacement. */
    	if(seDeplace) {
    		if(offsetX > 0)      offsetX += IConfig.VITESSE_DEPLACEMENT;
    		else if(offsetX < 0) offsetX -= IConfig.VITESSE_DEPLACEMENT;
    		
    		if(offsetY > 0)      offsetY += IConfig.VITESSE_DEPLACEMENT;
    		else if(offsetY < 0) offsetY -= IConfig.VITESSE_DEPLACEMENT;
    		
    		if(Math.abs((int)offsetX) >= IConfig.NB_PIX_CASE || Math.abs((int)offsetY) >= IConfig.NB_PIX_CASE)
    		{
    			/* Mise à jour de la nouvelle position. */
    			int x = position.getNumCase() % IConfig.LARGEUR_CARTE;
    			int y = position.getNumCase() / IConfig.LARGEUR_CARTE;

    			if(offsetX > 0)
    				x++;
    			else if(offsetX < 0)
    				x--;

    			if(offsetY > 0)
    				y++;
    			else if(offsetY < 0)
    				y--;
    			
    			position.x = x;
    			position.y = y;
    			
    			/* Remise à zéro du déplacement. */
    			offsetX = offsetY = 0;
    			seDeplace = false;
    			animation = 0;
    		}
    	}
    	
    	/* Mise à jour de l'animation. */
        if(estVisible) {
    		if(this.mort){
    			/* On fait tourner le perso */
    			direction = direction.directionSuivante();
    			
    			if(direction == Direction.HAUT)
    				estVisible = false;
    		}
    		if(seDeplace){
    			if(++animation >= N_ANIMATIONS)
    				animation = 0;
    		}
        }
	}
    
	/**
	 * Fonction permettant de crée un combat entre 2 soldats
	 * @param soldat Instance du soldat à attaquer
	 * @param distance Distance séparant soldat1 de soldat 2
	 * @return -1 si le soldat qui attaque est mort, 1 si le soldat attaqué est mort, 2 si les deux soldats sont morts et 0 sinon
	 */
	public int combat(Soldat soldat, int distance)
	{	
		int valeur_retour = 0;
		
		/* On joue le bruitage approprié */
		if(distance > 1)
			Son.joueArc();
		else
			Son.joueEpee();
		
		/* On calcule un dégat aléatoire, selon si on est côté à côté ou éloigné du soldat attaque */
		int degat = (distance == 1) ? Aleatoire.nombreAleatoire(1, this.getPuissance()) : this.getTir();
		int numCase = soldat.position.getNumCase();
		
		Infobulle.newMessage(numCase, "-" + degat, IConfig.MESSAGE_NEGATIF, IConfig.MOUV_INFOBULLE_BAS, 30);
				
		/* On met à jour la nouvelle vie du soldat attaqué */
		int vie = soldat.getVie() - degat;
		soldat.setVie(vie);

		/* S'il lui reste encore de la vie */
		if(vie > 0) {
			/* Et si le soldat a une portée assez grande pour répliquer */
			if(soldat.getPortee() >= distance) {
				/* Alors une réplique est crée */
				degat = (distance == 1) ? soldat.getPuissance() / IConfig.COEFFICIENT_REDUC : soldat.getTir() / IConfig.COEFFICIENT_REDUC;
				FenetreJeu.information.setText("Un " + soldat.nom + " vous frappe, vous perdez " + degat + " points de vie");
				vie = this.getVie() - degat;
				numCase = this.position.getNumCase();
				
				Infobulle.newMessage(numCase, "-" + degat, IConfig.MESSAGE_NEGATIF, IConfig.MOUV_INFOBULLE_BAS, 0);
				this.setVie(vie);
				
				/* Si la réplique est fatale, le soldat qui a attaqué meurt */
				if(vie <= 0) {
					Son.joueMourir(this);
					this.setMort(true);
					valeur_retour = -1;
				}				
			}
		}
		else {
			Son.joueMourir(soldat);
			soldat.setMort(true);	
			
			//if(valeur_retour == -1)
			//	valeur_retour = 2;
			//else
			// J'AI COMPRIS LE CODE CEPENDANT CECI EST IMPOSSIBLE !
				valeur_retour = 1;
			
		}
		
		this.setAJoue(true);
		
		return(valeur_retour);
	}
	
	/**
	 * Méthode permettant de mettre en repos le soldat
	 * @param afficherMessage Si vrai, un message sera affiché
	 */
	public void repos(boolean afficherMessage) {

		int regain = Aleatoire.nombreAleatoire(0, IConfig.REPOS_MAX);
		int case_courante = this.getPosition().getNumCase();
		
		/* Si la vie du soldat est déjà au max on considere pas qu'il a joué. Cependant on lui met un message */
		if(vie == this.getVieMax()) {
			
			if(!afficherMessage)
				return;
			
			FenetreJeu.information.setText(this.nom + " " + position.toString() + " a sa vie au maximum");
			
			Infobulle.newMessage(case_courante, "Vie au max", IConfig.MESSAGE_NEUTRE, 2, 0);
			return;
		}

		/* Définition du message et de sa couleur */
		Infobulle.newMessage(case_courante, "+ " + regain, IConfig.MESSAGE_POSITIF, IConfig.MOUV_INFOBULLE_HAUT, 0);
		
		FenetreJeu.information.setText(this.nom + " se repose et regagne " + regain + " points de vie");
		
		/* On met a jour sa vie et on indique qu'il a joué */
		this.setVie(vie + regain);
		this.setAJoue(true);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public int getPoints() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTour() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void joueTour(int tour) {
		// TODO Auto-generated method stub
		
	}

	/** Dessine la barre de vie du Héros.
	 * @param g : Zone de dessin. 
	 * @param x : Coordonnée X du personnage.
	 * @param y : Coordonnée Y du personnage.
	 */
	protected void dessineVie(Graphics g, int x, int y)
	{
		Color color;
		int pourcentage_vie = (int) getPourcentageVie();
		if(pourcentage_vie >= 70)
			color = Color.green;
		else if(pourcentage_vie >= 40)
			color = Color.orange;
		else
			color = Color.red;
		
		int dx = x * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE;
		int dy = y * IConfig.NB_PIX_CASE + 2;
		
		/* Contenant. */
		g.setColor(Color.black);
		g.drawRect(dx + offsetX, dy + offsetY, 4, IConfig.NB_PIX_CASE - 2);
		
		/* Contenu. */
		int offset = (int)(IConfig.NB_PIX_CASE * vie / (double)vieMax);
		g.setColor(color);
		g.fillRect(dx + 1 + offsetX , dy + 1 + offsetY + IConfig.NB_PIX_CASE - offset, 3, offset - 3);

	}
	
	/**
	 * Caractéristiques d'un soldat
	 * @return La chaine formatée comprenant les caractéristiques du soldat
	 */
	public String toString(){
		String chaine = nom + " " + getPosition().toString();
		chaine += "\nVie: " + this.vie + " /" + this.vieMax;
		chaine += "\nPuissance: " + this.puissance;
		chaine += "\nTir: " + this.tir;
		chaine += "\nPortee: " + this.portee;
		
		return chaine;
	}
}
