package wargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;

public abstract class Soldat extends Charset implements ISoldat, IConfig
{
	protected int vie, portee, puissance, tir;
	protected String nom;
	
	/** Est mort ? */
	protected boolean est_mort = false;
	
	Soldat() {}

	abstract protected void dessineVie(Graphics g, int x, int y);
	
	public int getVie() 
	{
		return vie;
	}

	public void setVie(int vie) 
	{
		this.vie = vie;
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

	/** Update le status du soldat.
	 *  @param e Evenement appellant, le timer.
	 */
    public void actionPerformed(ActionEvent e)
    {    
    	if(est_visible)	{
    		if(est_mort) {
    			if(++direction >= N_DIRECTIONS)
    				est_visible = false;
    		}
    		else if(++animation >= N_ANIMATIONS)
    			animation = 0;
    	}
	}
	
	public void combat(Soldat soldat)
	{
		/* Récupération des variables */
		int vie_attaquant = this.getVie();
		int vie_defenseur = soldat.getVie();
		
		int attaque_attaquant = 0;
		int attaque_defenseur = 0;
		
		int tour = 0; //0 - attaquant ( il attaque toujours en premier )  | 1 - def
		int type_combat = 0; // 0 pour corps a corps - 1 pour distance
					/*******************************************/
					/* A CORRIGER - VERIFIER POSITION PERSONNE */
					/*******************************************/
		if ( true ) { // position de la personne mettre la fonction
			type_combat = 1;
			attaque_attaquant = this.getTir();
			attaque_defenseur = soldat.getTir();
		}
		else {
			type_combat = 0;
			attaque_attaquant = this.getPuissance();
			attaque_defenseur = soldat.getPuissance();
		}
		
		//penser a faire le rand d'attaque entre 0 et la puissance a chaque coup

//		while ( vie_defenseur > 0 && vie_attaquant > 0) {
//			( tour == 0 ) ? 
//					(type_combat == 0) ? degat = alea(0,attaque_attaquant) : degat = attaque_attaquant //attaquant
//			:		(type_combat == 0) ? degat = alea(0,attaque_defenseur) : degat = attaque_defenseur; // défenseur
//			// Affichage du tour
//			System.out.println("Attaque"+(tour == 0) ? "l'attaquant" : "du défenseur");
//			// nombre de dégat fait 
//			System.out.println("Degat fait par " + (tour == 0) ? "l'attaquant : " : "le défenseur : " + degat);
//			// calcule des dégats
//			(tour == 0 ) ? vie_defenseur -= degat : vie_attaquant -= degat;
//			// affichage de la vie de la personne apres degat 
//			System.out.println("Vie restant : "+(tour == 0) ? vie_def : vie_attaquant);
//			// changement du tour
//			(tour == 0) ? tour = 1 : tour = 0;
//		}
		
		soldat.setVie(vie_defenseur);
		this.setVie(vie_attaquant);
	}
	
	public void seDeplace(Point newPos)
	{
	
		
	}
	
	public int alea (int min , int max) {
		return min + (int)(Math.random() * (max - min + 1));
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
		g.setColor( new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)(c.getAlpha() / 2.5) ));
		g.fillRect(x * IConfig.NB_PIX_CASE, y * IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE, IConfig.NB_PIX_CASE);
		((Graphics2D) g).setStroke(s); // Restauration du trait.
	}
}
