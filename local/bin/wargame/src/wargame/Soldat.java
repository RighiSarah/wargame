package wargame;

import java.util.Scanner;

public class Soldat extends Position implements ISoldat, IConfig{
	protected int vie, portee, puissance, tir; 
	protected Position position;

	public void joueTour(int tour){
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < IConfig.NB_HEROS; i++) {
			System.out.println("Que voulez vous faire ?");
			System.out.println("[1] - Repos");
			System.out.println("[2] - Combattre");
			System.out.println("[3] - Déplacement");
			int choix = sc.nextInt();
			switch (choix) {
				case 1 :
					System.out.println("Vous vous arretez sur un petit rocher et faite une pause.");
					break;
				case 2 :
					//this.combat(this.monstre);
					break;
				case 3 :
					do {
						int deplacement = 0;
						System.out.println("[1] - Nord");
						System.out.println("[2] - Nord Est");
						System.out.println("[3] - Est");
						System.out.println("[4] - Sud Est");
						System.out.println("[5] - Sud");
						System.out.println("[6] - Sud Ouest");
						System.out.println("[7] - Ouest");
						int choix = sc.nextInt();
						switch (choix) {
							case 1 :
								//+1 X 0 Y
								deplacement = this.seDeplace( new Position( position.getX() + 1, position.getY() );
								break;
							case 2 :
								// 1 X 1 Y
								deplacement = this.seDeplace( new Position( position.getX() + 1, position.getY() + 1 );
								break;
							case 3 : 
								// 0 X 1 Y
								deplacement = this.seDeplace( new Position( position.getX(), position.getY() + 1 );
								break;
							case 4 : 
								// -1 X 1 Y
								deplacement = this.seDeplace( new Position( position.getX() - 1, position.getY() + 1 );
								break;
							case 5 : 
								// -1 X 0 Y
								deplacement = this.seDeplace( new Position( position.getX() - 1, position.getY() );
								break;
							case 6 : 
								// -1 X -1 Y
								deplacement = this.seDeplace( new Position( position.getX() - 1, position.getY() - 1 );
								break;
							case 7 : 
								// 0 X -1 Y
								deplacement = this.seDeplace( new Position( position.getX(), position.getY() - 1 );
								break;
							default :
								System.out.println("Déplacement invalide");
								break;
						}
						if(deplacement == 0)
							System.out.println("Erreur la case est occupée ou est hors map");
						
					}while(deplacement != 1);
					break;
				default :
					break;
			}
			
			
			
		}
		sc.close();
	}
	
	public int getVie() {
		return vie;
	}

	public void setVie(int vie) {
		this.vie = vie;
	}

	public int getPortee() {
		return portee;
	}

	public void setPortee(int portee) {
		this.portee = portee;
	}

	public int getPuissance() {
		return puissance;
	}

	public void setPuissance(int puissance) {
		this.puissance = puissance;
	}

	public int getTir() {
		return tir;
	}

	public void setTir(int tir) {
		this.tir = tir;
	}

	public void combat(Soldat soldat){
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
		if ( 1 ) { // position de la personne mettre la fonction
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

		while ( vie_defenseur > 0 && vie_attaquant > 0) {
			( tour == 0 ) ? 
					(type_combat == 0) ? degat = alea(0,attaque_attaquant) : degat = attaque_attaquant //attaquant
			:		(type_combat == 0) ? degat = alea(0,attaque_defenseur) : degat = attaque_defenseur; // défenseur
			// Affichage du tour
			System.out.println("Attaque"+(tour == 0) ? "l'attaquant" : "du défenseur");
			// nombre de dégat fait 
			System.out.println("Degat fait par " + (tour == 0) ? "l'attaquant : " : "le défenseur : " + degat);
			// calcule des dégats
			(tour == 0 ) ? vie_defenseur -= degat : vie_attaquant -= degat;
			// affichage de la vie de la personne apres degat 
			System.out.println("Vie restant : "+(tour == 0) ? vie_def : vie_attaquant);
			// changement du tour
			(tour == 0) ? tour = 1 : tour = 0;
		}
		
		soldat.setVie(vie_defenseur);
		this.setVie(vie_attaquant);
	}
	
	public void seDeplace(Position newPos){
		if ( this.estValide() && this.estvide() ) { // définir est vide
			this.position = newPosition;
			return 1;
		}
		return 0;
		
	}
	
	public int alea (int min , int max) {
		return min + (int)(Math.random() * (max - min + 1));
	}

	
}
