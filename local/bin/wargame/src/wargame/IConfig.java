package wargame;

import java.awt.Color;

public interface IConfig 
{
	/** Largeur de la carte. */
	int LARGEUR_CARTE = 25; 
	
	/** Hauteur de la carte. */
	int HAUTEUR_CARTE = 15;
	
	/** Nombre de pixels par case. */
	int NB_PIX_CASE = 32;
	
	/** Position en X de la fenêtre. */
	int POSITION_X = 100; 
	
	/** Position en Y de la fenêtre. */
	int POSITION_Y = 50;
	
	/** Nombre de Héros. */
	int NB_HEROS = 20; 
	
	/** Nombre de monstres. */
	int NB_MONSTRES = 35; 
	
	/** Nombre de cailloux. */
	int NB_ROCHERS = 10;

	/** Nombre d'arbres. */
	int NB_ARBRES = 3;
	
	/** Nombre de pailles. */
	int NB_PAILLES = 4;
	
	/** Couleur du texte. */
    Color COULEUR_TEXTE = Color.black;
    
    /** Couleur d'une case utilisée. */
    Color COULEUR_NEUTRE = Color.magenta;
    
    /** Couleur de déplacement. */
    Color COULEUR_DEPLACEMENT = Color.red;
}