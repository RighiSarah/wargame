package wargame;

import java.util.LinkedList;

import javax.swing.JLabel;

/**
 * Classe implémentant une méthode statique permettant de crée la file de message de l'historique 
 */
public class Historique extends JLabel implements IConfig
{
	private static final long serialVersionUID = 7045910608318694418L;
	private LinkedList<HistoriqueMessage> fileHistorique = new LinkedList<HistoriqueMessage>();
	private int numMessage = 0;
	

	public Historique(){
		super();
	}
	
	public Historique(String texte, int alignement_horizontal){
		super(texte, alignement_horizontal);
	}


	private class HistoriqueMessage{
		private String message;
		private int numero;

		HistoriqueMessage(String msg) {
			this.message = msg;
			this.numero = numMessage;
		}	
	}

	public void addMessage(String message) {

		if(this.getTailleHistorique() > IConfig.TAILLE_MAX_HISTORIQUE)
			fileHistorique.removeFirst();
		
		numMessage++;
		fileHistorique.add(new HistoriqueMessage(message));
		this.setText("[" + numMessage + "] " + message);
	}

	public String getMessage(int x) {
		HistoriqueMessage elem = fileHistorique.get(x);
		return "[" + elem.numero + "] " + elem.message;
	}
	
	public void setMessage(int x) {
		this.setText(this.getMessage(x));
	}
	
	public String getPremier() {
		HistoriqueMessage elem = fileHistorique.getFirst();
		return "[" + elem.numero + "] " + elem.message;
	}
	
	public void setPremier(){
		this.setText(this.getPremier());
	}
	
	public String getDernier() {
		HistoriqueMessage elem = fileHistorique.getLast();
		return "[" + elem.numero + "] " + elem.message;
	}
	
	public void setDernier() {
		this.setText(this.getDernier());
	}
	
	public int getTailleHistorique() {
		return fileHistorique.size();
	}	
}
