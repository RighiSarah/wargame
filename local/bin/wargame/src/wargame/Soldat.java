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
	private boolean estMort = false;
	
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
	
	/** Mettre le statut du personnage à mort. */
	public void setMort() {
		this.estMort = true;
		this.direction = Direction.HAUT;
		this.animation = 0;
	    this.timer.setDelay(350);
	    
	}
	
	/** Teste si le personnage est mort. 
	 * @return true si mort, false sinon.
	 */
	public boolean estMort() {
		return estMort;
	}

	public boolean getAJoue() {
		return tourEffectue;
	}
	
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
    		if(estMort){
    			if(direction.augmenteDirection() == false){ // Si on a atteint la direction maximale
    				System.out.println("coucou");
    				estVisible = false;
    			}
    		}
    		if(seDeplace){
    			if(++animation >= N_ANIMATIONS)
    				animation = 0;
    		}
        }
	}
    
	/**
	 * Fonction permettant de crée un combat entre 2 soldats
	 * @param soldat instance du soldat a attaquer
	 * @param distance distance séparant soldat1 de soldat 2
	 */
	public void combat(Soldat soldat, int distance)
	{	
		System.out.println(distance);
		int degat = (distance == 1) ? Aleatoire.nombreAleatoire(1, this.getPuissance()) : this.getTir();
		int numCase = soldat.position.getNumCase();
		
		Infobulle.newMessage(numCase, "-" + degat, IConfig.MESSAGE_NEGATIF, IConfig.BAS, 30);
				
		int vie = soldat.getVie() - degat;
		soldat.setVie(vie);

		if(vie > 0) {
			if(soldat.getPortee() >= distance) {

				degat = (distance == 1) ? soldat.getPuissance() : soldat.getTir();
				FenetreJeu.information.setText("Un " + soldat.nom + " vous frappe, vous perdez " + degat + " points de vie");
				vie = this.getVie() - degat;
				numCase = this.position.getNumCase();
				
				Infobulle.newMessage(numCase, "-" + degat, IConfig.MESSAGE_NEGATIF, IConfig.BAS, -1);
				this.setVie(vie);
				
				if(vie <= 0) {
					this.setMort();
					if(soldat instanceof Heros)	
						Carte.nbHerosRestantDec();
					else 
						Carte.nbMonstresRestantDec();
				}				
			}
		}
		else {
			if(soldat instanceof Heros)	
				Carte.nbHerosRestantDec();
			else 
				Carte.nbMonstresRestantDec();
			
			soldat.setMort();	
		}
		
		this.setAJoue(true);
	}
	
	/**
	 * Fonction statique permettant de dessiner une infobulle sur la carte
	 * @param afficherMessage si afficher message vaut true, le message vie au max s'affichera 
	 * 						  sinon , seul les +X s'afficherons
	 */
	public void repos(boolean afficherMessage) {

		int regain = Aleatoire.nombreAleatoire(0, IConfig.REGEN_MAX);
		int vie = this.getVie();
		int case_courante = this.getPosition().getNumCase();
		
		/* Si la vie du soldat est déjà au max on considere pas qu'il a joué. Cependant on lui met un message */
		if(vie == this.getVieMax()) {
			
			if(!afficherMessage)
				return;
			
			FenetreJeu.information.setText( this.nom + 
										   "(" + this.position.x + "," + this.position.y + ")" + 
										   " a sa vie au maximum"
										   );
			
			Infobulle.newMessage(case_courante, "Vie au max", IConfig.MESSAGE_NEUTRE, 2, 0);
			return;
		}

		/* Définition du message et de sa couleur */
		Infobulle.newMessage(case_courante, "+ " + regain, IConfig.MESSAGE_POSITIF, IConfig.HAUT, 0);
		
		FenetreJeu.information.setText(this.nom + " se repose et regagne " + regain + " point de vie");
		
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
	
	/** Dessine la barre de vie du Héros.
	 * @param g : Zone de dessin. 
	 * @param x : Coordonnée X du personnage.
	 * @param y : Coordonnée Y du personnage.
	 */
	protected void dessineVie(Graphics g, int x, int y)
	{
		Color color;

		/* Couleur de la barre de vie. */
		int res = ((int)(100.0 * vie / (double)vieMax));
		
		if(res >= 70)
			color = Color.green;
		else if(res >= 40)
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
		String chaine = nom;
		chaine += "\nVie: " + this.vie + " /" + this.vieMax;
		chaine += "\nPuissance: " + this.puissance;
		chaine += "\nTir: " + this.tir;
		chaine += "\nPortee: " + this.portee;
		
		return chaine;
	}
}
