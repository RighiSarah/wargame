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
import javax.swing.Timer;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
	
	private JPanel sousMenu;
	
	private JButton boutonCharger;
	private JButton boutonSauvegarder;
	private JButton navigerHistoriqueUp;
	private JButton navigerHistoriqueDown;
	
	/** Trigger*/
	private boolean scroll = false;
	
	/** Carte du jeu. */
    Carte carte;

	private JSeparator separator;
    private JPanel barreEtat;
    static JLabel historique;
    static JLabel information;

    /** Compteur servant à l'initialisation des évènements de sauvegardes. */
	private static int k = 0;
	
	/** Numéro de la case du héros actionnée */
	int numHeros = 0;
	
	/** 
	 * Tableau contenant les touches actionnées 
	 * Dans cette ordre :
	 * 			HAUT, BAS, GAUCHE, DROITE
	 */
	boolean[] tabKey = {false,false,false,false};
	
	/** Timer. */
	Timer timer = new Timer(IConfig.DELAI_TOUCHE, 
													new ActionListener() {
														public void actionPerformed (ActionEvent event) {
															if(timerOn) {
																//System.out.println("Up : "+tabKey[0] + " Down : "+tabKey[1] +" Left : "+ tabKey[2] +" Right : "+ tabKey[3]);
																carte.changePos(tabKey);
																timerOn = false;
															}
														}
													});
	/** 
	 * Boolean qui indique si le timer est activé ou non
	 * 				True si le timer est en route
	 * 				False sinon
	 */
	boolean timerOn = false;
	
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
	
	public void timer() {
		if(timerOn)
			return;
		
		timer.setDelay(IConfig.DELAI_TOUCHE);	
		timer.start();
		timerOn = true;
	}
	
	public FenetreJeu() throws InvalidMidiDataException, IOException, MidiUnavailableException
	{

		/* No Tab key-pressed or key-released events are received by the key event listener.
		 * This is because the focus subsystem consumes focus traversal keys, such as Tab and Shift Tab.
		 */
		setFocusTraversalKeysEnabled(false);
		addKeyListener(new KeyListener() {
			
		    public void keyPressed(KeyEvent e) { 
		    	int key = e.getKeyCode();

		    	if (key == KeyEvent.VK_TAB) { 
		    		numHeros = carte.trouverProchainHeros(numHeros);
		    	}
		    	else if(key == KeyEvent.VK_UP) {
		    		timer();
		    		tabKey[0] = true;
		    		//System.out.println("UP");
		    	}
		    	else if(key == KeyEvent.VK_DOWN) {
		    		timer();
		    		tabKey[1] = true;
		    		//System.out.println("DOWN");
		    	}
		    	else if(key == KeyEvent.VK_LEFT) {
		    		timer();
		    		tabKey[2] = true;
		    		//System.out.println("LEFT");
		    	}
		    	else if(key == KeyEvent.VK_RIGHT) {
		    		timer();
		    		tabKey[3] = true;
		    		//System.out.println("RIGHT");
		    	} 	
		    }

			public void keyReleased(KeyEvent e) { 
				int key = e.getKeyCode();
		    	if(key == KeyEvent.VK_UP)   		tabKey[0] = false;
		    	else if(key == KeyEvent.VK_DOWN) 	tabKey[1] = false;
		    	else if(key == KeyEvent.VK_LEFT)	tabKey[2] = false;
		    	else if(key == KeyEvent.VK_RIGHT)	tabKey[3] = false;
			}

			public void keyTyped(KeyEvent e) { /* Pas utilisée */	}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				scroll = !scroll;
				if(!scroll)
					return;
				
	            if (e.getPreciseWheelRotation() < 0) // Haut
	            	System.out.println("Up " + e.getPreciseWheelRotation());
	            else
	            	System.out.println("Down " + e.getPreciseWheelRotation() + e.getClickCount());
			}
		});
		
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
					carte.reinitAJoue();
				}
			}
		});
	    
	    
        carte.setPreferredSize(new Dimension(IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE, 
        									 IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE));
	    
	    sousMenu = new JPanel();
	    sousMenu.setPreferredSize(new Dimension(carte.getWidth(), 35));
	    
	    boutonCharger 			= new JButton("Charger");
	    boutonCharger.setPreferredSize(new Dimension(30,30));
	    
		boutonSauvegarder 		= new JButton("Sauvegarder");
		boutonSauvegarder.setPreferredSize(new Dimension(30,30));
		
		navigerHistoriqueUp 	= new JButton("Naviger Up");
		navigerHistoriqueUp.setPreferredSize(new Dimension(30,30));
		
		navigerHistoriqueDown 	= new JButton("Naviger Down");
		navigerHistoriqueDown.setPreferredSize(new Dimension(30,30));
		
	    sousMenu.add(boutonCharger);
	    sousMenu.add(boutonSauvegarder);
	    sousMenu.add(navigerHistoriqueUp);
	    sousMenu.add(navigerHistoriqueDown);

	    
	    this.add(sousMenu,BorderLayout.PAGE_START);
	    
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBackground(Color.DARK_GRAY);
        separator.setSize(new Dimension(carte.getWidth(), 5));
        
        JSeparator sep = separator;
        this.add(sep,BorderLayout.NORTH);

        this.add(carte, BorderLayout.CENTER);
        
        this.add(separator, BorderLayout.SOUTH);

        barreEtat = new JPanel();
        historique = new JLabel("Pour commencer, crée une nouvelle partie ou charger en une", JLabel.RIGHT);
        information = new JLabel("Ici s'affichera l'historique des actions", JLabel.LEFT);
        barreEtat.setSize(new Dimension(carte.getWidth(), 16));
        barreEtat.setLayout(new BoxLayout(barreEtat, BoxLayout.X_AXIS));
        
        barreEtat.add(historique);
        barreEtat.add(Box.createHorizontalGlue());
        barreEtat.add(information);

        this.add(barreEtat,BorderLayout.PAGE_END);
	    
        
        /* On joue le son d'arrière plan */
		//sonArriere = new Son();
		//sonArriere.joueSonArriere();

        
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

