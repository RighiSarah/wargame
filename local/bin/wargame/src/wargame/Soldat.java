package wargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;


/**
 * Classe abstraite représentant la base d'un soldat
 */
public abstract class Soldat extends Charset implements ISoldat
{
	private static final long serialVersionUID = 1L;
	
	protected String message = "";
	protected Color couleurMessage = IConfig.MESSAGE_NEGATIF;
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
	
	public int getPourcentageVie(){
		return (vieMax/vie);
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
		this.direction = HAUT;
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
    		if(estMort)
    			if(++direction >= N_DIRECTIONS)
    				estVisible = false;
    		if(seDeplace)
    			if(++animation >= N_ANIMATIONS)
    				animation = 0;
        }
	}
	
	public void combat(Soldat soldat,int distance)
	{	
		System.out.println(distance);
		int degat = (distance == 1) ? Aleatoire.nombreAleatoire(1, this.getPuissance()) : this.getTir();
		int numCase = soldat.position.getNumCase();
		message = "-"+degat+"PV";
		coord = new Point(numCase % IConfig.LARGEUR_CARTE, numCase / IConfig.LARGEUR_CARTE);
		
		int vie = soldat.getVie() - degat;
		soldat.setVie(vie);
		System.out.println("VIE"+soldat.getVie());
		if(vie > 0) {
			soldat.setVie(vie);
			if( soldat.getPortee() >= distance ) {

				degat = (distance == 1) ? soldat.getPuissance() : soldat.getTir();
				FenetreJeu.gameInfo.setText("Tu t'es pris une putain de patate ( -"+degat+")");
				vie = this.getVie() - degat;
				numCase = this.position.getNumCase();
				message = "-"+degat+"PV";
				coord = new Point(numCase % IConfig.LARGEUR_CARTE, numCase / IConfig.LARGEUR_CARTE);
				if(vie > 0)
					this.setVie(vie);
				else {
					if(soldat instanceof Heros)	Carte.nbHerosRestantDec();
					else Carte.nbMonstresRestantDec();
					this.setMort();
				}
			}
		}
		else {
			if(soldat instanceof Heros)	Carte.nbHerosRestantDec();
			else Carte.nbMonstresRestantDec();
			Carte.setSoldat(numCase, null);
			soldat.setMort();		
		}
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

	protected void dessineDeplacement(Graphics g, int x, int y, Color c) 
	{
		Stroke s = ((Graphics2D) g).getStroke();        // Sauvegarde du trait courant.
		((Graphics2D) g).setStroke(new BasicStroke(3)); // Changement du trait.
		g.setColor(c);
		g.fillRect(x * IConfig.NB_PIX_CASE, y * IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
		
		((Graphics2D) g).setStroke(s); // Restauration du trait.
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
		if(message != null && coord != null)
			Infobulle.dessiner(g, coord.x, coord.y, message, couleurMessage, IConfig.ARRIERE_PLAN);
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
