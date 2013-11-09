package wargame;

public class Heros extends Soldat implements ICarte{
	
	
	Heros(TypesH type_heros){
		this.vie = type_heros.getPoints();
		this.portee = type_heros.getPortee();
		this.puissance = type_heros.getPuissance();
		this.tir = type_heros.getTir();
		this.position = trouvePositionVide(); //ICarte pour la définition de la fonction
		
	}
	
	/* Crée un hero perso */
	Heros(int heal, int range, int power, int shoot){
		this.vie = heal;
		this.portee = range;
		this.puissance = power;
		this.tir = shoot;
		this.position = trouvePositionVide();
	}

}
