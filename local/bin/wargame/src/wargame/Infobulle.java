package wargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;

final class Infobulle extends Rectangle implements ActionListener
{
	private static String message;
	private static Timer timer;

	private Infobulle(){}
	
	/* Ajout d'un boolean pour afficher ou non le rectangle */
	public static void dessiner(Graphics g, int x, int y, String message, Color string_color, Color background_color)
	{
		Color ancienne_couleur = g.getColor();  
		
		String[] lignes = message.split("\n");
		
		FontMetrics fm = g.getFontMetrics(); 
		 
		int start_x = x * IConfig.NB_PIX_CASE;
		int start_y = y * IConfig.NB_PIX_CASE;
		
		int hauteur_rectangle = lignes.length * g.getFontMetrics().getHeight() + IConfig.MARGE_INFOBULLE;
		int largeur_rectangle = fm.stringWidth(Infobulle.stringTailleMax(lignes)) + 2*IConfig.MARGE_INFOBULLE;
		
		int pixel_largeur_carte = IConfig.LARGEUR_CARTE * IConfig.NB_PIX_CASE;
		int pixel_hauteur_carte = IConfig.HAUTEUR_CARTE * IConfig.NB_PIX_CASE;
		
		if(hauteur_rectangle + start_y > pixel_hauteur_carte){
			start_y = pixel_hauteur_carte - hauteur_rectangle;
		}
		
		if(largeur_rectangle + start_x > pixel_largeur_carte){
			start_x = pixel_largeur_carte - largeur_rectangle;
		}
		
		g.setColor(background_color);
		g.fillRect(start_x - IConfig.MARGE_INFOBULLE, start_y, largeur_rectangle, hauteur_rectangle); 
		
		g.setColor(Color.DARK_GRAY);
		g.drawRect(start_x - IConfig.MARGE_INFOBULLE, start_y, largeur_rectangle, hauteur_rectangle);
		
		g.setColor(string_color);
		
		for (String ligne : lignes){
			g.drawString(ligne, start_x, start_y += g.getFontMetrics().getHeight() ); 
		}
		
		g.setColor(ancienne_couleur);  
	}
	
	private static String stringTailleMax(String[] message){
		String string_max = message[0];
		int length_max = string_max.length();
		
		for(int i = 1; i < message.length; i++){
			if(message[i].length() > length_max){
				string_max = message[i];
				length_max = string_max.length();
			}
		}
		
		return string_max;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
