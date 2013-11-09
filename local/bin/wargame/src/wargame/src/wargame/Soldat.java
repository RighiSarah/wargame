package wargame;

import java.util.Scanner;

public class Soldat implements ISoldat, IConfig{
	Heros heros = new Heros();
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
							break;
						case 2 :
							// 1 X 1 Y
							break;
						case 3 : 
							// 0 X 1 Y
							break;
						case 4 : 
							// -1 X 1 Y
							break;
						case 5 : 
							// -1 X 0 Y
							break;
						case 6 : 
							// -1 X -1 Y
							break;
						case 7 : 
							// 0 X -1 Y
							break;
						default :
							break;
					
					}
					break;
				default :
					break;
			}
			
			
			
		}
		sc.close();
	}
	
	public void combat(Soldat soldat){
		
	}
	
	public void seDeplace(Position newPos){
		
	}

	
}
