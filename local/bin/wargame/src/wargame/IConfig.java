package wargame;

import java.awt.Color;

public interface IConfig 
{
	/** Nombre de sauvegardes possibles. */
	int NB_SAUVEGARDES = 10;
	
	/** Nom d'un fichier de sauvegarder. */
	String NOM_SAUVEGARDE = "wargame";
	
	/** Nom du tileset. */
	String NOM_TILESET = "tileset.png";

	/** Nom de la fenêtre. */
	String NOM_FENETRE = "The JAVA War";
	
	/** Liste des images des soldats. */
	String ARCHER  = "archer.png";
	String ELFE    = "elfe.png";
	String GOBELIN = "gobelin.png";
	String ORC     = "orc.png";

	/** Taille de la carte. */
	int LARGEUR_CARTE = 25; 
	int HAUTEUR_CARTE = 15;
	
	/** Nombre de pixels par case. */
	int NB_PIX_CASE = 32;
	
	/** Nombre de pixels par deplacement */
	int VITESSE_DEPLACEMENT = 8;
	
	/** Position de la fenêtre. */
	int POSITION_X = 100; 
	int POSITION_Y = 50;

	/** Nombre de Héros. */
	int NB_HEROS = 20; 
	
	/** Nombre de Monstres. */
	int NB_MONSTRES = 35; 

	/** Nombre d'obstacles. */
	int NB_ROCHERS = 10;
	int NB_ARBRES = 3;	
	int NB_PAILLES = 4;
	
	/** Regen max d'un pégus */
	int REGEN_MAX = 10;
	
	/** Couleur du texte. */
    Color COULEUR_TEXTE = Color.black;
    
    /** Couleur d'une case d'un soldat utilisé */
    Color SOLDAT_UTILISE = Color.GRAY;
    
    /** Couleur de la case du soldat [ Heros ]  séléctionné */
    Color SOLDAT_SELECTIONNEE = Color.BLUE;
    
    /** Couleur des cases de déplacement possible */
    Color SOLDAT_DEPLACEMENT_POSSIBLE = Color.RED;
    
    /** Valeur de Alpha par Défaut */
	int DEFAULT_ALPHA = 100;
	
	/** Couleurs de l'arrière plan de l'infobulle */
	Color MESSAGE_POSITIF = Color.RED;
	Color MESSAGE_NEGATIF = Color.GREEN;
	Color MESSAGE_NEUTRE = new Color(240, 240, 0, 220);
    
    /** Marge interne de l'infobulle */
    int MARGE_INFOBULLE = 5;
 
    
}