package wargame;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class FenetreJeu extends JFrame
{
	private static final long serialVersionUID = 1;

	/** Menus. */
	private JMenuBar menu;
	private JMenu jeu;
	private JMenu sauvegarder;
	private JMenu charger;
	
	/* Options des menus. */
	
	/** Nouvelle partie. */
	private JMenuItem nouveau;
	
	/** Quitter. */
	private JMenuItem quitter;
	
	/** Liste des sauvegardes. */
	private JMenuItem []sauvegarde;
	
	/** Liste des slots de chargement. */
	private JMenuItem []slot;

	public static void main(String[] args)
	{
		FenetreJeu fenetre = new FenetreJeu();
	}
	 
	public FenetreJeu()
	{
		/* Création des menus principaux. */
		menu        = new JMenuBar();
		jeu         = new JMenu("Jeu");
		sauvegarder = new JMenu("Sauvegarder");
		charger     = new JMenu("Charger");
		
		/* Création des options des menus. */
		nouveau = new JMenuItem("Nouvelle partie");
		quitter = new JMenuItem("Quitter");
		
		sauvegarde = new JMenuItem[IConfig.NB_SAUVEGARDES];
		slot = new JMenuItem[IConfig.NB_SAUVEGARDES];

		for(int i = 0; i < IConfig.NB_SAUVEGARDES; i ++)
		{
			sauvegarde[i] = new JMenuItem("Sauvegarde " + i);
			slot[i] = new JMenuItem("Sauvegarde " + i);
		}
	    
		/* Initialisation des menus. */
	    jeu.add(nouveau);
	    jeu.addSeparator();
	    jeu.add(quitter);

	    for(int i = 0; i < IConfig.NB_SAUVEGARDES; i++)
	    {
	    	sauvegarder.add(sauvegarde[i]);
	    	charger.add(slot[i]);
	    }
	    
	    /* Ajout des menus dans la barre de menus. */
	    menu.add(jeu);
	    menu.add(sauvegarder);
	    menu.add(charger);

	    this.setJMenuBar(menu);
	    this.setVisible(true);
	    
	    
	    JPanel carte = new Carte();
		
		((Carte)carte).generer();
		
        carte.setPreferredSize(new Dimension(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE, 
        									 IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE));
        this.add(carte);
	    this.setVisible(true);
	    
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.pack();
	  }
	}

