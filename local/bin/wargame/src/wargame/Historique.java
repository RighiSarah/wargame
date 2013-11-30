package wargame;

import java.util.LinkedList;

import javax.swing.JLabel;

/**
 * Classe permettant de gérer un historique
 */
public class Historique extends JLabel implements IConfig
{
	private static final long serialVersionUID = 7045910608318694418L;
	private LinkedList<HistoriqueMessage> fileHistorique;
	private int numMessage;
	
	/**
	 * Constructeur par défaut
	 */
	public Historique(){
		super();
		fileHistorique = new LinkedList<HistoriqueMessage>();
		numMessage = 0;
	}
	
	/**
	 * Constructeur permettant d'ajouter un texte de présentation à l'historique et de préciser son alignement horizontal
	 * @param texte Le texte
	 * @param alignement_horizontal Constante d'alignement
	 */
	public Historique(String texte, int alignement_horizontal){
		super(texte, alignement_horizontal);
		fileHistorique = new LinkedList<HistoriqueMessage>();
		numMessage = 0;
	}


	/**
	 * Classe privée permettant d'instancier un message
	 */
	private class HistoriqueMessage{
		private String message;
		private int numero;

		/** Constructeur par défaut, avec comme paramètre le texte du message */
		HistoriqueMessage(String msg) {
			this.message = msg;
			this.numero = numMessage;
		}	
	}

	/**
	 * Méthode permettant d'ajouter un message à l'historique
	 * @param message Le message à ajouter
	 */
	public void addMessage(String message) {

		if(this.getTailleHistorique() > IConfig.TAILLE_MAX_HISTORIQUE)
			fileHistorique.removeFirst();
		
		numMessage++;
		fileHistorique.add(new HistoriqueMessage(message));
		this.setText("[" + numMessage + "] " + message);
	}

	/**
	 * Méthode permettant de récupérer le message numéro x
	 * @param x Le numéro du message à récupérer
	 * @return Le texte du message
	 */
	public String getMessage(int x) {
		HistoriqueMessage elem = fileHistorique.get(x);
		return "[" + elem.numero + "] " + elem.message;
	}
	
	/**
	 * Méthode permettant d'afficher le message numéro x
	 * @param x Le numéro du message
	 */
	public void afficherMessage(int x) {
		this.setText(this.getMessage(x));
	}
	
	/**
	 * Méthode permettant de récupérer le premier message de l'historique
	 * @return Le texte du premier message de l'historique
	 */
	public String getPremier() {
		HistoriqueMessage elem = fileHistorique.getFirst();
		return "[" + elem.numero + "] " + elem.message;
	}
	
	/**
	 * Méthode permettant d'afficher le premier message de l'historique
	 */
	public void afficherPremier(){
		this.setText(this.getPremier());
	}
	
	/**
	 * Méthode permettant de récupérer le dernier message de l'historique
	 * @return Le dernier message de l'historique
	 */
	public String getDernier() {
		HistoriqueMessage elem = fileHistorique.getLast();
		return "[" + elem.numero + "] " + elem.message;
	}
	
	/**
	 * Méthode permettant d'afficher le dernier message de l'historique
	 */
	public void afficherDernier() {
		this.setText(this.getDernier());
	}
	
	/**
	 * Méthode permettant de récupérer la taille de l'historique
	 * @return La taille de l'historique
	 */
	public int getTailleHistorique() {
		return fileHistorique.size();
	}	
}
