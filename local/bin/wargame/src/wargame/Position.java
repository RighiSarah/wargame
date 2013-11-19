package wargame;

import java.awt.Point;

import wargame.Charset.Direction;

/**
 * Classe permettant de gérer les positions sur la carte
 */
public class Position extends Point implements IConfig{
	private static final long serialVersionUID = 4537495200581219261L;

	/**
	 * Constructeur de base, initialise une position sans rien
	 */
	Position(){
		super();
	}

	/**
	 * Constructeur initialisant une position à partir d'un point
	 * @param p Un point
	 */
	Position(Point p){
		super(p);
	}

	/**
	 * Constructeur initialisant une position à partir d'un x et d'un y
	 * @param x Coordonnée x
	 * @param y Coordonnée y
	 */
	Position(int x, int y) {
		super(x, y);
	}

	/**
	 * Constructeur initialisant une position à partir d'un numéro de case de la carte
	 * @param num_case Numéro de la case de la carte
	 */
	Position(int num_case){
		super();
		this.setNumCase(num_case);
	}
	
	/**
	 * Méthode permettant de renvoyer non pas les coordonnées d'une case, mais les coordonnées des pixels de p
	 * @param p Position de la case
	 * @return Un point avec pour coordonnées le x et y du pixel en haut à gauche de la case
	 */
	public Point getCoordPixel(Position p){
		return (new Point(x * IConfig.NB_PIX_CASE, y * IConfig.NB_PIX_CASE));
	}
	
	/**
	 * Méthode permettant de récupérer le numéro de la case dans la carte
	 * @return Le numéro de la case dans la carte
	 */
	public int getNumCase(){
		return x + IConfig.LARGEUR_CARTE * y;
	}
	
	/**
	 * Méthode permettant de spécifier le numéro de case de la position. Met donc à jour x et y
	 * @param num Numéro de la case dans la carte
	 */
	public void setNumCase(int num){
		x = num % IConfig.LARGEUR_CARTE;
		y = num / IConfig.LARGEUR_CARTE;
	}
	
	/**
	 * Méthode permettant de vérifier que la position est valide, c'est à dire qu'elle est bien dans la carte
	 * @return Vrai si la position est dans la carte, faux sinon
	 */
	public boolean estValide() {
		if (x < 0 || x >= IConfig.LARGEUR_CARTE || y < 0 || y >= IConfig.HAUTEUR_CARTE) 
			return false; 
		
		return true;
	}
	
	/**
	 * Méthode permettant de vérifier que la position est voisine d'une autre position
	 * @param pos L'autre position
	 * @return Vrai si la position est voisine avec l'autre position, faux sinon
	 */
	public boolean estVoisine(Position pos) {
		return ((Math.abs(x-pos.x) <= 1) && (Math.abs(y-pos.y) <= 1));
	}
	
	/**
	 * Méthode permettant de calculer la distance avec une autre position
	 * @param pos L'autre position
	 * @return La distance entre les deux positions (en terme de nombre de cases de la carte)
	 */
	public int distance(Position pos){
		if (this.getNumCase() == -1 || pos.getNumCase() == -1) return 0;
		
		int dx = Math.abs(this.x - pos.x);
		int dy = Math.abs(this.y - pos.y);

		if(dx == 1 && dy == 1)
			return 1;

		return dx + dy;
	}
	

	/**
	 * Méthode toString affichant les coordonnées de la position
	 */
	public String toString() { 
		return "(" + x + "," + y +")"; 
	}
	
	
}
