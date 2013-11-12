package wargame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe permettant de jouer des son simples correspondant à différentes actions
 */
public final class Son {
	
	/* Constructeur privé, on ne veut pas qu'on instancie la classe */
	private Son() {}
	
	
	/* Méthode statique permettant de jouer un son */
	public static void joueSon() {
		AudioClip ac = null;
		try {
			ac = Applet.newAudioClip(new URL(""));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		ac.play();
	}
}
