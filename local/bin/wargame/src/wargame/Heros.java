package wargame;

public class Heros extends Soldat implements ICarte
{	
	Heros(TypesH type_heros)
	{
		vie = type_heros.getPoints();
		portee = type_heros.getPortee();
		puissance = type_heros.getPuissance();
		tir = type_heros.getTir();
		//position = trouvePositionVide();
	}
	
	/* Crée un heros personnalisable. */
	Heros(int vie, int portee, int puissance, int tir)
	{
		this.vie = vie;
		this.portee = portee;
		this.puissance = puissance;
		this.tir = tir;
	//	this.position = trouvePositionVide();
	}

}
