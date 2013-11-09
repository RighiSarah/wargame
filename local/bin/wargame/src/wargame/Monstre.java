package wargame;

public class Monstre extends Soldat {
	
	
	Monstre(TypesM type_monstre){
		this.vie = type_monstre.getPoints();
		this.portee = type_monstre.getPortee();
		this.puissance = type_monstre.getPuissance();
		this.tir = type_monstre.getTir();
	//	this.position = trouvePositionVide();
	}
	/* Crée un monstre perso */
	Monstre(int heal, int range, int power, int shoot){
		this.vie = heal;
		this.portee = range;
		this.puissance = power;
		this.tir = shoot;
	}
}
