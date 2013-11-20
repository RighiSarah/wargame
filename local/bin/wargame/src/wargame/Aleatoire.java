package wargame;

/**
 * Classe permettant de générer des nombres aléatoires
 */
public final class Aleatoire {
	
	private Aleatoire(){}
	
	/**
	 * Méthode statique permettant de générer des nombres aléatoires entre deux bornes
	 * @param min Borne inférieure
	 * @param max Borne supérieure
	 * @return Nombre aléatoire compris entre min et max
	 */
	public static final int nombreAleatoire (int min , int max) {
		return min + (int)(Math.random() * (max - min + 1));
	}
}
