package wargame;

import java.awt.Color;

public interface IConfig 
{
	/** Nombre de sauvegardes possibles. */
	int NB_SAUVEGARDES = 10;
	
	/** Nom du tileset. */
	String NOM_TILESET = "tileset.png";

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
	
	/** Couleur du texte. */
    Color COULEUR_TEXTE = Color.black;
    
    /** Couleur d'une case utilisée. */
    Color COULEUR_NEUTRE = Color.magenta;
    
    /** Couleur de déplacement. */
    Color COULEUR_DEPLACEMENT = Color.red;
}