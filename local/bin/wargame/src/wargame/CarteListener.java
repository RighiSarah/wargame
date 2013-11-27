package wargame;

public interface CarteListener {
	void joueurPerd();
	void joueurGagne();
	void deplaceMonstre();
	void historique(String s);
	void information(String s);
}
