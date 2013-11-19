package wargame;

import java.awt.Point;

public class Position extends Point implements IConfig{
	private static final long serialVersionUID = 4537495200581219261L;

	Position(){
		super();
	}

	Position(Point p){
		super(p);
	}

	Position(int x, int y) {
		super(x, y);
	}

	Position(int num_case){
		super();
		this.setNumCase(num_case);
	}
	
	public Point getCoordPixel(Position p){
		return (new Point(x * IConfig.NB_PIX_CASE, y * IConfig.NB_PIX_CASE));
	}
	
	public int getNumCase(){
		return x + IConfig.LARGEUR_CARTE * y;
	}
	
	public void setNumCase(int num){
		x = num % IConfig.LARGEUR_CARTE;
		y = num / IConfig.LARGEUR_CARTE;
	}
	
	public boolean estValide() {
		if (x < 0 || x >= IConfig.LARGEUR_CARTE || y < 0 || y >= IConfig.HAUTEUR_CARTE) 
			return false; 
		
		return true;
	}
	
	public boolean estVoisine(Position pos) {
		return ((Math.abs(x-pos.x) <= 1) && (Math.abs(y-pos.y) <= 1));
	}
	
	public String toString() { 
		return "(" + x + "," + y +")"; 
	}
}
