package wargame;

import java.awt.Color;

public interface IConfig {
	int LARGEUR_CARTE = 50; int HAUTEUR_CARTE = 30; // en nombre de cases
	int NB_PIX_CASE = 20;
	int POSITION_X = 100; int POSITION_Y = 50; // Position de la fenêtre
	int NB_HEROS = 30; int NB_MONSTRES = 50; int NB_OBSTACLES = 100;

	Color COULEUR_NEUTRE = Color.magenta, COULEUR_TEXTE = Color.black;
	Color COULEUR_HEROS = Color.red, COULEUR_HEROS_OFF = Color.pink;
	Color COULEUR_MONSTRES = Color.black;
	Color COULEUR_EAU = Color.blue, COULEUR_FORET = Color.green, COULEUR_ROCHER = Color.gray;
}