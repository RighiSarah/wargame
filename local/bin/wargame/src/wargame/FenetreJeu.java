package wargame;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class FenetreJeu
{
	public static void main(String[] args) 
	{
		JFrame frame = new JFrame("C'est la gueguerre !");	
		JPanel carte = new Carte();
		
		((Carte)carte).generer();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        carte.setPreferredSize(new Dimension(800, 480));

		frame.add(carte);
	    frame.setVisible(true);
	    frame.setResizable(false);
	    frame.pack();
	}
}
