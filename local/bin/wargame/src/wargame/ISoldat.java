package wargame;

import java.io.Serializable;

/** 
 * Interface comprenant les attributs et méthodes d'un soldat.
 * Comprend aussi les différentes valeurs des caractéristiques des monstres et héros.
 */
public interface ISoldat extends Serializable
{
	public static final char MONSTRE = 1;
	public static final char HEROS = 2;

	/**
	 * Énumération permettant d'avoir les caractéristiques des différents héros
	 */
	static enum TypesH 
	{
		HUMAIN(40, 3, 10, 2, "Archer"), 
		SOLDAT(80, 1, 20, 0, "Soldat"), 
		ELFE(70, 5, 10, 6, "Elfe"), 
		HOBBIT(20, 3, 5, 2, "Hobbit");
		
		private final int POINTS_DE_VIE, PORTEE_VISUELLE, PUISSANCE, TIR;
		private final String NOM;
		
		TypesH(int points, int portee, int puissance, int tir, String nom) 
		{
			POINTS_DE_VIE = points; 
			PORTEE_VISUELLE = portee;
			PUISSANCE = puissance; 
			TIR = tir;
			NOM = nom;
		}

		public int getPoints() 
		{ 
			return POINTS_DE_VIE; 
		}

		public int getPortee() 
		{ 
			return PORTEE_VISUELLE; 
		}

		public int getPuissance() 
		{ 
			return PUISSANCE; 
		}

		public int getTir() 
		{ 
			return TIR; 
		}

		public String getNom() 
		{ 
			return NOM; 
		}
		
		public static TypesH getTypeHAlea() 
		{
			return values()[(int)(Math.random() * values().length)];
		}
	}

	/**
	 * Énumération permettant d'avoir les principales caractéristiques des différents monstres
	 */
	public static enum TypesM 
	{
		SQUELETTE(100, 1, 30, 0, "Squelette"), 
		ORC(40, 2, 10, 3, "Orc"), 
		GOBELIN(20, 2, 5, 2, "Gobelin");

		private final int POINTS_DE_VIE, PORTEE_VISUELLE, PUISSANCE, TIR;
		private final String NOM;
		
		TypesM(int points, int portee, int puissance, int tir, String nom) 
		{
			POINTS_DE_VIE = points; 
			PORTEE_VISUELLE = portee;
			PUISSANCE = puissance; 
			TIR = tir;
			NOM = nom;
		}

		public int getPoints() 
		{ 
			return POINTS_DE_VIE; 
		}

		public int getPortee() 
		{ 
			return PORTEE_VISUELLE; 
		}

		public int getPuissance() 
		{
			return PUISSANCE; 
		}

		public int getTir() 
		{ 
			return TIR; 
		}

		public String getNom() 
		{ 
			return NOM; 
		}
		
		public static TypesM getTypeMAlea() 
		{
			return values()[(int)(Math.random() * values().length)];
		}
	}

	int getPortee();
	int getPuissance();
	int getTir();
	int getVieMax();
	int getVie();
	double getPourcentageVie();
	boolean getSeDeplace();
	String getNom();

	void setSeDeplace(boolean value);	
	void setMort(boolean mort);
	void setPosition(Position position);
	void setAJoue(boolean value);
	void setVie(int vie);
	
	
	int repos(boolean afficher_message);
	int combat(Soldat soldat, int distance);
	
}