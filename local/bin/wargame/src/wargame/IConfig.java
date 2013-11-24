package wargame;

import java.awt.Color;

/**
 * Interface comprenant la configuration du projet
 */
public interface IConfig 
{
	
	/** Nombre de FPS pour la carte. */
	double FPS = 60.0;
	
	/* Chemins : attention de bien préciser le / de fin */
	/** Chemin du dossier des musiques */
	String CHEMIN_MUSIQUE = "musique/"; 
	/** Chemin du dossier des images */
	String CHEMIN_IMAGE = "image/";
	/** Chemin du dossier de sauvegardes */
	String CHEMIN_SAUVEGARDE = "sauvegarde/";
	
	/** Nombre de sauvegardes possibles. */
	int NB_SAUVEGARDES = 10;
	
	/** Nom d'un fichier de sauvegarder. */
	String NOM_SAUVEGARDE = "wargame";
	
	/** Nom du tileset. */
	String NOM_TILESET = "tileset.png";

	/** Nom de la fenêtre. */
	String NOM_FENETRE = "The JAVA War";
	
	/** Liste des images des soldats. */
	String IMAGE_HUMAIN  = "archer.png";
	String IMAGE_ELFE    = "elfe.png";
	String IMAGE_GOBELIN = "gobelin.png";
	String IMAGE_ORC     = "orc.png";

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
	int NB_HEROS = 10; 
	
	/** Nombre de Monstres. */
	int NB_MONSTRES = 15; 

	/** Nombre d'obstacles. */
	int NB_ROCHERS = 10;
	int NB_ARBRES = 3;	
	int NB_PAILLES = 4;
	
	/** Repos maximum dont peut bénéficier un soldat */
	int REPOS_MAX = 10;
	
	/** Couleur du texte. */
    Color COULEUR_TEXTE = Color.black;
    
    /** Valeur de Alpha par Défaut */
    int DEFAULT_ALPHA = 100;
    
    /** Couleur d'une case d'un soldat utilisé */
    Color SOLDAT_UTILISE = new Color(128, 128, 128, DEFAULT_ALPHA);
    
    /** Couleur de la case du soldat sélectionné */
    Color SOLDAT_SELECTIONNEE = new Color(0, 0, 255, DEFAULT_ALPHA);
    
    /** Couleur des cases de déplacement possible */
    Color SOLDAT_DEPLACEMENT_POSSIBLE = new Color(255, 0, 0, DEFAULT_ALPHA);
    
	/** Couleurs du message de l'infobulle */
	Color MESSAGE_POSITIF = new Color(60, 150, 10, 255);
	Color MESSAGE_NEGATIF = Color.RED;
	Color MESSAGE_NEUTRE = Color.BLACK;
	
	/** Couleur de l'arrière plan de l'infobulle */
	Color ARRIERE_PLAN = new Color(240, 240, 0, 220);
    
    /** Marge interne de l'infobulle */
    int MARGE_INFOBULLE = 5;
    
    /** Nombre de musiques d'arrière plan */
    int NOMBRE_MUSIQUE_ARRIERE_PLAN = 3;
      
    /** Mouvement de l'infobulle (vers le haut, vers le bas ou aucun mouvement...) */
	char MOUV_INFOBULLE_HAUT    = 0;
	char MOUV_INFOBULLE_BAS     = 1;
	char MOUV_INFOBULLE_AUCUN = 2;
	
	/** Nombre de millisecondes à attendre après qu'un monstre ait fait une action */
	int ATTENDRE_MONSTRE_DEPLACEMENT = 100;
	int ATTENDRE_MONSTRE_REPOS = 1000;
	int ATTENDRE_MONSTRE_COMBAT = 1000;
	
	/** Couleur du brouillard */
	Color COULEUR_BROUILLARD = new Color(50, 50, 50, 245);
 
    
}
