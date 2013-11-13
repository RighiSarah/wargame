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
 * Classe permettant de jouer des sons simples correspondant à différentes actions
 */
public final class Son {

	/* Constructeur privé, on ne veut pas qu'on instancie la classe */
	private Son() {}
	
	/**
	 * Méthode statique permettant de jouer en boucle et aléatoirement les musiques d'arrière plan
	 * @throws InvalidMidiDataException Si problème avec le fichier midi
	 * @throws IOException Erreur d'entrée pour le fichier midi
	 * @throws MidiUnavailableException Si le fichier midi est indisponible
	*/
	public static void joueSonArriere() throws InvalidMidiDataException, IOException, MidiUnavailableException {
		int num = 1 + (int)(Math.random() * (IConfig.NOMBRE_MUSIQUE_ARRIERE_PLAN));
		Sequence sequence = MidiSystem.getSequence(new File(IConfig.CHEMIN_MUSIQUE + "arriere_plan" + num + ".mid"));
				
		/* Final pour pouvoir accéder au sequencer dans la méthode meta de la sous classe MetaEventListener */
		final Sequencer sequencer = MidiSystem.getSequencer();
	    sequencer.open();
	    sequencer.setSequence(sequence);

	    sequencer.start();
	    
	    /* Ajout d'un évènement pour être prévenu lorsque la musique est terminée */
	    sequencer.addMetaEventListener(new MetaEventListener() {
			public void meta(MetaMessage message) {
				/* Si la musique est terminée */
				if(message.getType() == 47){
					int num = 1 + (int)(Math.random() * (IConfig.NOMBRE_MUSIQUE_ARRIERE_PLAN));
					try {
						/* On charge une autre séquence */
						Sequence sequence;
						sequence = MidiSystem.getSequence(new File(IConfig.CHEMIN_MUSIQUE + "arriere_plan" + num + ".mid"));
						
						/* On stoppe le sequencer puis on le ferme, et on charge puis joue une autre musique */
						sequencer.stop();
						
						try {
							sequencer.setSequence(sequence);
						} catch (InvalidMidiDataException e) {
							e.printStackTrace();
						}
						
						sequencer.start();
					} catch (InvalidMidiDataException | IOException e) {
						e.printStackTrace();
					}	
				}
			}
		});
	}
	
	/* Méthode statique privée permettant de jouer un son wav */
	private static void joueWav(String son){
		AudioClip ac = null;
		try {
			ac = Applet.newAudioClip(new URL("file://" + IConfig.CHEMIN_MUSIQUE + son));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		ac.play();
	}
	
	/**
	 * Méthode statique permettant de jouer le son d'un arc
	 */
	public static void joueArc(){
		joueWav("arc.wav");
	}
	
	/** 
	 * Méthode statique permettant de jouer le son d'une épée
	 */
	public static void joueEpee(){
		joueWav("epee.wav");
	}	
	
	/**
	 * Méthode statique permettant de jouer le son d'un personnage courant sur l'herbe
	 */
	public static void joueCourir(){
		joueWav("courir.wav");
	}
	
	/**
	 * Méthode statique permettant de jouer le son d'un personnage marchant sur l'herbe
	 */
	public static void joueMarcher(){
		joueWav("marcher.wav");
	}
}
