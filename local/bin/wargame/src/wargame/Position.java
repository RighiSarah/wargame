package wargame;

import java.awt.Point;

import wargame.Charset.Direction;

/**
 * Classe permettant de gérer les positions sur la carte
 */
public class Position extends Point implements IConfig{
	private static final long serialVersionUID = 4537495200581219261L;

	/**
	 * Constructeur de base, initialise une position aléatoire comprise dans la carte
	 */
	Position(){
		super((int)(Math.random() * IConfig.LARGEUR_CARTE), (int)(Math.random() * IConfig.HAUTEUR_CARTE));
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
	 * @return Un point avec pour coordonnées le x et y du pixel en haut à gauche de la case
	 */
	public Point getCoordPixel(){
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
		return (x >= 0 && y >= 0 && x < IConfig.LARGEUR_CARTE && y < IConfig.HAUTEUR_CARTE);
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
	 * Méthode retournant la direction vers une autre position. Si c'est en diagonale, ce sera une des deux directions.
	 * Ex : diagonale haut droite, la méthode retournera soit haut soit droite
	 * @param pos La position dont on se demande la direction
	 * @return Une direction 
	 */
	public Direction direction(Position pos){
		int sx = x;
		int sy = y;
		int dx = pos.x;
		int dy = pos.y;
		
		Direction direction = Direction.HAUT;

		if(dx > sx) {
			direction = Direction.DROITE;
		}
		else if(dx < sx) {
			direction = Direction.GAUCHE;
		}
		if(dy > sy) {
			direction = Direction.BAS;
		}
		else if(dy < sy) {
			direction = Direction.HAUT;
		}
		
		return direction;
	}
	
	/**
	 * Méthode vérifiant si deux positions sont égales
	 * @param pos Position à vérifier
	 * @return Vrai si les deux positions sont égales, faux sinon
	 */
	public boolean equals(Position pos){
		return ((pos.x == this.x) && (pos.y == this.y));  
	}
	
	
	/**
	 * Méthode toString affichant les coordonnées de la position
	 */
	public String toString() { 
		return "(" + x + "," + y +")"; 
	}
	
}
