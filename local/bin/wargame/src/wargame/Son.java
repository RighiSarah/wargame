package wargame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;


/**
 * Classe permettant de jouer des son simples correspondant à différentes actions
 */
public final class Son {
	
	/* Constructeur privé, on ne veut pas qu'on instancie la classe */
	private Son() {}
	
	
	/* Méthode statique permettant de jouer un son */
	public static void joueSonArriere() throws InvalidMidiDataException, IOException, MidiUnavailableException {
		Sequence sequence = MidiSystem.getSequence(new File(IConfig.CHEMIN_MUSIQUE + "arriere_plan1.mid"));
	    Sequencer sequencer = MidiSystem.getSequencer();
	    sequencer.open();
	    sequencer.setSequence(sequence);

	    sequencer.start();
	}
	
	public static void joueArc(){
		AudioClip ac = null;
		try {
			ac = Applet.newAudioClip(new URL("file://" + IConfig.CHEMIN_MUSIQUE + "coucou"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		ac.play();
	}
	
	public static void joueEpee(){
		AudioClip ac = null;
		try {
			ac = Applet.newAudioClip(new URL("file://" + IConfig.CHEMIN_MUSIQUE + "coucou"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		ac.play();
	}
		

		
}
