package wargame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;


/**
 * Classe permettant de jouer une musique des Seigneurs des anneaux
 * Implémente aussi des méthodes statiques permettant de jouer des bruitages
 */
public final class Son {
	/** Pour charger un fichier midi */
	private Sequence sequence;
	/** Pour charger un lecteur */
	private Sequencer sequencer; 
	/** Pour savoir si le son en arrière est stoppé ou non */
	private boolean sonArriereActive = true;
	/** Pour jouer un wma quand il se passe un grand événement (partie perdue ou gagnée) */
	private AudioClip musiqueEvenement = null;
	/** Pour savoir si le son des bruitages est activé */
	public static boolean sonBruitageActive = true;

	/* Différents sons de bruitage */
	private static AudioClip bruitCourir = null;
	private static AudioClip bruitArc = null;
	private static AudioClip bruitEpee = null;
	private static AudioClip bruitMarcher = null;
	private static AudioClip bruitMortHeros = null;
	private static AudioClip bruitMortMonstre = null;

	/**
	 * Constructeur permettant d'instancier l'objet qui manipulera le son d'arrière plan
	 * @throws InvalidMidiDataException Si problème avec le fichier midi
	 * @throws IOException Erreur d'entrée pour le fichier midi
	 * @throws MidiUnavailableException Si le fichier midi est indisponible
	*/
	public Son() throws MidiUnavailableException, InvalidMidiDataException, IOException {
		sequencer = MidiSystem.getSequencer();
		sequencer.open();
		
	    chargeSonArriere();
	    
	    /* Ajout d'un évènement pour être prévenu lorsque la musique est terminée */
	    sequencer.addMetaEventListener(new MetaEventListener() {
			public void meta(MetaMessage message) {
				/* Si la musique est terminée */
				if(message.getType() == 47){
					/* On stoppe le sequencer puis on le ferme, et on charge puis joue une autre musique */
					stopSonArriere();
					
					try {
						chargeSonArriere();
					} catch (InvalidMidiDataException e1) {
						e1.printStackTrace();
					}
					
					try {
						joueSonArriere();
					} catch (MidiUnavailableException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	}
	
	/**
	 * Méthode permettant de jouer le son en arrière plan
	 * @throws MidiUnavailableException Exception permettnat de vérifier qu'il n'y a pas d'erreur à la lecture du son midi
	 */
	public void joueSonArriere() throws MidiUnavailableException{   
	    sequencer.start();
	    sonArriereActive = true;
	}
	
	/** 
	 * Méthode permettant de stopper le son d'arrière plan
	 */
	public void stopSonArriere(){
		sequencer.stop();
		sonArriereActive = false;
	}
	
	
	/**
	 * Méthode permettant de savoir si le son est actuellement joué ou non
	 * @return Boolean à vrai si le son est effectivement en train d'être joué, false sinon
	 */
	public boolean getSonArriereActive(){
		return sonArriereActive;
	}
	
	/**
	 * Méthode permettant de charger un son arrière aléatoire parmi les musiques du dossier
	 * @throws InvalidMidiDataException 
	 */
	public void chargeSonArriere() throws InvalidMidiDataException{
		if(musiqueEvenement != null){
			musiqueEvenement.stop();
			musiqueEvenement = null;
		}
		
		int num = 1 + (int)(Math.random() * (IConfig.NOMBRE_MUSIQUE_ARRIERE_PLAN));
		try {
			/* On charge une autre séquence */
			sequence = MidiSystem.getSequence(new File(IConfig.CHEMIN_MUSIQUE + "arriere_plan" + num + ".mid"));
	
			try {
				sequencer.setSequence(sequence);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** Méthode permettant de jouer un son adapté lorsque le joueur perd le jeu */
	public void jouePerdu(){
		stopSonArriere();
		if(musiqueEvenement != null)
			musiqueEvenement.stop();
		
		try {
			musiqueEvenement = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "perdre.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		musiqueEvenement.play();
	}
	
	/** Méthode permettant de jouer un son adapté lorsque le joueur gagne le jeu */
	public void joueGagne(){
		stopSonArriere();
		if(musiqueEvenement != null)
			musiqueEvenement.stop();
		
		try {
			musiqueEvenement = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "gagner.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		musiqueEvenement.play();
	}
	
	
	
	
	/* ############################# MÉTHODES STATIQUES ############################# */
	

	/**
	 * Méthode statique permettant de jouer le son d'un arc
	 */
	public static void joueArc(){
		if(!Son.sonBruitageActive)
			return;
		
		/* Si le bruit n'a pas été déjà chargé, alors on le charge */
		if(bruitArc == null)
			try {
				bruitArc = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "arc.wav"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		bruitArc.play();
	}
	
	/** 
	 * Méthode statique permettant de jouer le son d'une épée
	 */
	public static void joueEpee(){
		if(!Son.sonBruitageActive)
			return;
		
		if(bruitEpee == null)
			try {
				bruitEpee = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "epee.wav"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		bruitEpee.play();
	}	
	
	/**
	 * Méthode statique permettant de jouer le son d'un personnage courant sur l'herbe
	 */
	public static void joueCourir(){
		if(!Son.sonBruitageActive)
			return;
		
		if(bruitCourir == null)
			try {
				bruitCourir = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "courir.wav"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		bruitCourir.play();
	}
	
	/**
	 * Méthode statique permettant de jouer le son d'un personnage marchant sur l'herbe
	 */
	public static void joueMarcher(){
		if(!Son.sonBruitageActive)
			return;
		
		if(bruitMarcher == null)
			try {
				bruitMarcher = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "marcher.wav"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		bruitMarcher.play();
	}
	
	/**
	 * Méthode statique permettant de jouer le son d'un héros qui meurt
	 */
	private static void joueMourirHeros(){
		if(!Son.sonBruitageActive)
			return;
		
		if(bruitMortHeros == null)
			try {
				bruitMortHeros = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "mort_heros.wav"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		bruitMortHeros.play();
	}
	
	/**
	 * Méthode statique permettant de jouer le son d'un monstre qui meurt
	 */
	private static void joueMourirMonstre(){
		if(!Son.sonBruitageActive)
			return;
		
		if(bruitMortMonstre == null)
			try {
				bruitMortMonstre = Applet.newAudioClip(new URL("file:" + IConfig.CHEMIN_MUSIQUE + "mort_monstre.wav"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		bruitMortMonstre.play();
	}
	
	/**
	 * Méthode permettant de jouer le son de la mort selon le soldat
	 * @param s Soldat (monstre ou héros)
	 */
	public static void joueMourir(Soldat s){
		if(s instanceof Heros)
			Son.joueMourirHeros();
		else
			Son.joueMourirMonstre();
	}
	
}
