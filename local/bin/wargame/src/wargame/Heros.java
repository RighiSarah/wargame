package wargame;

public class Heros extends Soldat {
	
	
	Heros(TypesH type_heros){
		this.vie = type_heros.getPoints();
		this.portee = type_heros.getPortee();
		this.puissance = type_heros.getPuissance();
		this.tir = type_heros.getTir();
	}

}
