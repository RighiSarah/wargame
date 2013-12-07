package wargame;

import java.awt.Point;

/**
 * Interface comprenant les méthodes et attributs d'une classe carte
 */
public interface ICarte {
	/** 
	 * Regenere le brouillard du au déplacement et deplace le soldat
	 * @param sold Soldat a deplacer
	 * @param nouvelle_position Case sur laquelle finira le soldat
	 */
	public void deplaceSoldat(Soldat sold, Position nouvelle_position);
	
	/** Genere aléatoirement une carte. */
	void generer();
	
	/** Trouve une position vide aléatoirement sur la carte. 
	 * Utilisable pour placer des Soldats.
	 * @param type Type de Soldat (Soldat.HEROS ou Soldat.MONSTRE)
	 * @return     La position vide.
	 * */
	Point trouvePositionVide(char type);
	
	/** Sauvegarde une carte.
	 * @param chemin Chemin de la sauvegarde.
	 */
	void sauvegarde(String chemin);

	/** Charge une carte.
	 * @param chemin Chemin de la sauvegarde.
	 */
	void charge(String chemin);
}