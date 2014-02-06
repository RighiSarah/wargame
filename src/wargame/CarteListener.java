package wargame;

/**
 * Interface permettant d'appliquer des écouteurs sur Carte
 */
public interface CarteListener {
	/**
	 * Événement lorsqu'un joueur perd
	 */
	void joueurPerd();
	/**
	 * Événement lorsqu'un joueur gagne
	 */
	void joueurGagne();
	/**
	 * Événement lorsque les monstres commencent à jouer ou ont fini de jouer
	 */
	void deplaceMonstre();
	/**
	 * Événement lorsqu'une action déclenche une phrase d'historique
	 * @param s La phrase d'historique
	 */
	void historique(String s);
	/**
	 * Événement lorsqu'une action déclenche la modification de l'information
	 * @param s La phrase d'information
	 */
	void information(String s);
}
