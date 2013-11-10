package wargame;

import java.awt.Point;

public interface ICarte {
 //	Element getElement(Position pos);
/*

	Heros trouveHeros(); // Trouve aléatoirement un héros sur la carte
	Heros trouveHeros(Position pos); // Trouve un héros choisi aléatoirement parmi les 8 positions adjacentes de pos

	boolean deplaceSoldat(Position pos, Soldat soldat);
	void mort(Soldat perso);
	boolean actionHeros(Position pos, Position pos2);
//	void jouerSoldats(PanneauJeu pj);	
	*/
	
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
	 * @param type Type de Soldat (Soldat.HOMME ou Soldat.MONSTRE)
	 * @return     La position vide.
	 * */
	Point trouvePositionVide(char type);
}