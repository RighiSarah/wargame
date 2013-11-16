package wargame;

import java.awt.Dimension;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe permettant de dessiner la fenêtre du jeu (avec ses menus...)
 */
public class FenetreJeu extends JFrame
{
	private static final long serialVersionUID = 7794325642011100784L;
	
	/** Menus. */
	private JMenuBar menu;
	private JMenu jeu;
	private JMenu sauvegarder;
	private JMenu charger;
	private JMenu config;
	private JButton finTour;

	/* Options des menus. */
	
	/** Nouvelle partie. */
	private JMenuItem nouveau;
	
	/** Quitter. */
	private JMenuItem quitter;
	
	/** Liste des sauvegardes. */
	private JMenuItem []sauvegarde;
	
	/** Liste des slots de chargement. */
	private JMenuItem []slot;

	/** activer / désactiver son */
	private JMenuItem son;
	
	/** Carte du jeu. */
    JPanel carte;

    /* Compteur servant à l'initialisation des évènements de sauvegardes. */
	private static int k = 0;

    private String getDate(File f)
    {
		return new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format( new Date(f.lastModified()));
    }
    
	public static void main(String[] args) throws InvalidMidiDataException, IOException, MidiUnavailableException
	{
		FenetreJeu fenetre = new FenetreJeu();
		fenetre.setVisible(true);
	}
	 
	public FenetreJeu() throws InvalidMidiDataException, IOException, MidiUnavailableException
	{
		/* Création d'une carte vide. */
		carte = new Carte();
		
		/* Création des menus principaux. */
		menu        = new JMenuBar();
		jeu         = new JMenu("Jeu");
		sauvegarder = new JMenu("Sauvegarder");
		charger     = new JMenu("Charger");
		config		= new JMenu("Configuration");
		finTour 	= new JButton("Fin de tour");

		/* Création des options des menus. */
		nouveau = new JMenuItem("Nouvelle partie");
		quitter = new JMenuItem("Quitter");
		
		son = new JMenuItem("Désactiver Son");
		
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

	    for(k = 0; k < IConfig.NB_SAUVEGARDES; k++)
	    {
	    	sauvegarder.add(sauvegarde[k]);
	    	sauvegarder.setEnabled(false);
	    	charger.add(slot[k]);
	    	
	    	sauvegarde[k].addActionListener(new ActionListener() {
	    		private final int NUM = k;
		    	public void actionPerformed(ActionEvent arg0) 
		    	{
					((Carte)carte).sauvegarde(NUM);
		    	}       
		    });
	    	
	    	slot[k].addActionListener(new ActionListener() {
	    		private final int NUM = k;
		    	public void actionPerformed(ActionEvent arg0) 
		    	{
					((Carte)carte).charge(NUM);
				    menu.add(Box.createHorizontalGlue()); 
				    menu.add(finTour);
				    setJMenuBar(menu);
		    	}       
		    });
	    }
	    
	    config.add(son);
	    
	    /* Ajout des menus dans la barre de menus. */
	    menu.add(jeu);
	    menu.add(sauvegarder);
	    menu.add(charger);
	    menu.add(config);
	    
	    this.setJMenuBar(menu);
	    this.setVisible(true);
	    
	    /* Actions des menus. */
	    
	    /* Quitter. */
	    quitter.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) 
	    	{
	    		System.exit(0);
	    	}       
	    });
	    
	    /* Nouvelle partie. */
	    nouveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{ 
				((Carte)carte).generer();							
				sauvegarder.setEnabled(true);
				
			    menu.add(Box.createHorizontalGlue()); 
			    menu.add(finTour);
			    setJMenuBar(menu);
			}
		});
	   
	    /* Activation désactivation son */
	    son.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{ 
				if(Son.getSon()) {
					Son.setEnableSon(false);
					son.setText("Activer son");

				}
				else {
					son.setText("Désactiver son");
					Son.setEnableSon(true);
					try {
						Son.joueSonArriere();
					} catch (InvalidMidiDataException | IOException | MidiUnavailableException e1) {
						e1.printStackTrace();
					}
				}

			}
		});
	   
	    /* Sauvegarder */
	    sauvegarder.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent e) 
			{
				for(int i = 0; i < IConfig.NB_SAUVEGARDES; i++)
				{
					File f = new File(IConfig.CHEMIN_SAUVEGARDE + IConfig.NOM_SAUVEGARDE + i + ".ser");
		    	
					if(f.exists())
						sauvegarde[i].setText(getDate(f));
				}
			}

			public void menuDeselected(MenuEvent e) {}
			public void menuCanceled(MenuEvent e) {}
		});
	    	
	    /* Charger */
	    charger.addMenuListener(new MenuListener() {
			public void menuSelected(MenuEvent e) 
			{
				for(int i = 0; i < IConfig.NB_SAUVEGARDES; i++)
				{
					File f = new File(IConfig.CHEMIN_SAUVEGARDE + IConfig.NOM_SAUVEGARDE + i + ".ser");
		    	
					if(f.exists())
					{
						slot[i].setText(getDate(f));
						slot[i].setEnabled(true);
					}
					else
						slot[i].setEnabled(false);
				}
			}
	
			public void menuDeselected(MenuEvent e) {}
			public void menuCanceled(MenuEvent e) {}
			
		});
	    
	    finTour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{ 
				System.out.println("YEAHH");
				if(Carte.generer)
					Carte.reinitAJoue();
			}
		});
	    
        carte.setPreferredSize(new Dimension(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE, 
        									 IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE));
        this.add(carte);

	    
        /* On joue le son d'arrière plan */
		Son.joueSonArriere();

        this.setTitle("Wargame");
        this.setIconImage(new ImageIcon(IConfig.CHEMIN_IMAGE + "icone.png").getImage());
	    
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.pack();
	    
	    this.setVisible(true);
	    
	    
	  }
	}

