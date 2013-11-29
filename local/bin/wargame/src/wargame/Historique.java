package wargame;

import java.util.LinkedList;

/**
 * Classe implémentant une méthode statique permettant de dessiner une infobulle sur la carte
 */
final class Historique
{
	private static LinkedList<HistoriqueMessage> FileHistorique = new LinkedList<HistoriqueMessage>();
	private static int NUM_MESSAGE = 0;
	
	
	/**
	 * Constructeur privé, ce n'est pas une classe faite pour être instanciée
	 */
	private Historique(){}

	/**
	 *  Classe message qui instancie les messages 
	 */
	static class HistoriqueMessage{
		
		private String message;
		private int numero;

		HistoriqueMessage(String msg) {
			this.message = msg;
			this.numero = NUM_MESSAGE;
		}	
	}

	public static void addMessage(String message) {
		if(getSize() > IConfig.TAILLE_MAX_HISTORIQUE)
			FileHistorique.removeFirst();
		
		NUM_MESSAGE++;
		FileHistorique.add(new HistoriqueMessage(message));
		FenetreJeu.information.setText("[" + NUM_MESSAGE + "] " + message);
	}

	public static String getMessage(int x) {
		HistoriqueMessage elem = FileHistorique.get(x);
		return "[" + elem.numero + "] " + elem.message;
	}
	
	public static String getFirst() {
		HistoriqueMessage elem = FileHistorique.getFirst();
		return "[" + elem.numero + "] " + elem.message;
	}
	
	public static String getLast() {
		HistoriqueMessage elem = FileHistorique.getLast();
		return "[" + elem.numero + "] " + elem.message;
	}
	
	public static int getSize() {
		return FileHistorique.size();
	}	
}
