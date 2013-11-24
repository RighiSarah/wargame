package wargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
    private JPanel barreEtat;
    private JSeparator separator;
    static JLabel historique;
    static JLabel information;
	/** Menus. */
	private JMenuBar menu;
	private JMenu jeu;
	private JMenu sauvegarder;
	private JMenu charger;
	private JMenu config;
	
	private static JButton finTour;

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
	
	/** activer / désactiver le brouillard */
	private JMenuItem brouillard;
	
	/** Carte du jeu. */
    Carte carte;

    /** Compteur servant à l'initialisation des évènements de sauvegardes. */
	private static int k = 0;
	
	/** Objet Son servant à gérer le son d'arrière plan */
	private Son sonArriere;

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
		this.setTitle("Wargame");
        this.setIconImage(new ImageIcon(IConfig.CHEMIN_IMAGE + "icone.png").getImage());
        
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
		brouillard = new JMenuItem("Désactiver brouillard");
		
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
					carte.sauvegarde(NUM);
		    	}       
		    });
	    	
	    	slot[k].addActionListener(new ActionListener() {
	    		private final int NUM = k;
		    	public void actionPerformed(ActionEvent arg0) 
		    	{
					carte.charge(NUM);
				    menu.add(Box.createHorizontalGlue()); 
				    menu.add(finTour);
				    setJMenuBar(menu);
		    	}       
		    });
	    }
	    
	    config.add(son);
	    config.add(brouillard);
	    
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
	    
	    quitter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
	    
	    
	    /* Nouvelle partie. */
	    nouveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{ 
				carte.generer();							
				sauvegarder.setEnabled(true);
				
				System.out.println(finTour.getSize());
			    menu.add(Box.createHorizontalGlue()); 
			    finTour.setPreferredSize(new Dimension(150,10));
			    menu.add(finTour);
			    setJMenuBar(menu);
			}
		});
	    nouveau.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	    
	    /* Activation désactivation son */
	    son.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{ 
				if(sonArriere.getSonArriereActive()){
					sonArriere.stopSonArriere();
					son.setText("Activer le son");
				}
				else{
					try {
						sonArriere.joueSonArriere();
					} catch (MidiUnavailableException e1) {
						e1.printStackTrace();
					}
					son.setText("Désactiver le son");
				}
			}
		});
	    
	    son.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
	   
	    brouillard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{ 
					if(carte.getBrouillardActive()) {
						carte.setBrouillardActive(false);
						brouillard.setText("Activer le brouillard");
					}
					else {
						carte.setBrouillardActive(true);
						brouillard.setText("Désactiver le brouillard");
					}
						
			}
		});

	    brouillard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
	    
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
				finTour.setPreferredSize(new Dimension(150,10));
				/*int[] key =
					{KeyEvent.VK_0, KeyEvent.VK_1,
					 KeyEvent.VK_2, KeyEvent.VK_3,
					 KeyEvent.VK_4, KeyEvent.VK_5,
					 KeyEvent.VK_6, KeyEvent.VK_7,
					 KeyEvent.VK_8, KeyEvent.VK_9};*/
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
				if(carte.isGeneree()){
					carte.joueMonstres();
					carte.reinitAJoue();
				}
			}
		});
	    
        carte.setPreferredSize(new Dimension(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE, 
        									 IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE));
        this.add(carte, BorderLayout.NORTH);
        
        /* Do Not Touch plz */
        
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBackground(Color.DARK_GRAY);
        separator.setSize(new Dimension(carte.getWidth(), 5));
        
        this.add(separator,BorderLayout.CENTER);

        barreEtat = new JPanel();
        historique = new JLabel("Pour commencer, crée une nouvelle partie ou charger en une", JLabel.RIGHT);
        information = new JLabel("Ici s'affichera l'historique des actions", JLabel.LEFT);
        barreEtat.setSize(new Dimension(carte.getWidth(), 16));
        barreEtat.setLayout(new BoxLayout(barreEtat, BoxLayout.X_AXIS));
        
        barreEtat.add(historique);
        barreEtat.add(Box.createHorizontalGlue());
        barreEtat.add(information);

        this.add(barreEtat,BorderLayout.SOUTH);
	    
        
        /* On joue le son d'arrière plan */
		sonArriere = new Son();
		sonArriere.joueSonArriere();

        
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    this.pack();
	    
	    this.setVisible(true);
	    
	    /* Événements de carte */
	    carte.onStateRealized(new CarteListener() {
			
			@Override
			public void joueurPerd() {
				sonArriere.jouePerdu();
			}
			
			@Override
			public void joueurGagne() {
				sonArriere.joueGagne();
			}
			
			public void deplaceMonstre(){
				finTour.setEnabled(!finTour.isEnabled());
			}
		});
	    
	    
	}
	public static void activableFinTour(boolean b) {
	   finTour.setEnabled(b);
	}
}

