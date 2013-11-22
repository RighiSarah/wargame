package wargame;

import java.awt.Point;

/**
 * Interface comprenant les méthodes et attributs d'une classe carte
 */
public interface ICarte {

	/** Déplace un soldat sur la carte.
	 * @param soldat    Soldat à deplacer.
	 * @param direction Direction du soldat.
	 * @param x         Offset X d'origine.
	 * @param y         Offset Y d'origine.
	 */
	void deplaceSoldat(Soldat soldat, char direction, int x, int y);

	/** Genere aléatoirement une carte. */
	void generer();
	
	/** Teste l'existence d'une case sur la carte.
	 * 
	 * @param x Coordonnée en X d'une case.
	 * @param y Coordonnée en Y d'une case.
	 * @return  true si la case existe, false sinon.
	 */
	boolean existe(int x, int y);
	
	/** Trouve une position vide aléatoirement sur la carte. 
	 * Utilisable pour placer des Soldats.
	 * @param type Type de Soldat (Soldat.HEROS ou Soldat.MONSTRE)
	 * @return     La position vide.
	 * */
	Point trouvePositionVide(char type);
	
	/** Sauvegarde une carte.
	 * @param num Numéro de la sauvegarde.
	 */
	void sauvegarde(int num);
	
	/** Charge une carte.
	 * @param num Numéro de la sauvegarde.
	 */
	void charge(int num);
}