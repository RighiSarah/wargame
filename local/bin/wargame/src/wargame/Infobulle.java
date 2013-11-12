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

	/* Ajout d'un boolean pour afficher ou non le rectangle */
	public static void dessiner(Graphics g, int x, int y, String message, Color c)
	{
		int i = 0;
		String[] lignes = message.split("\n");
		
		FontMetrics fm = g.getFontMetrics(); 
		 
		int startX = x * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE;
		int startY = y * IConfig.NB_PIX_CASE - IConfig.NB_PIX_CASE;
		int hauteur_ligne = g.getFontMetrics().getHeight();
		int largeur_rectangle = fm.stringWidth(Infobulle.stringTailleMax(lignes));
		
		Color old = g.getColor();  
		g.setColor(Color.YELLOW);  
		g.fillRect( startX + IConfig.MARGE_X_MESSAGE, startY, largeur_rectangle , lignes.length * hauteur_ligne);   

		g.setColor(c);
		
		for (String ligne : lignes){
			g.drawString( ligne, startX + IConfig.MARGE_X_MESSAGE, startY += g.getFontMetrics().getHeight() ); 
			i++;
		}
		
		g.setColor( old );  
	}
	
	public static String stringTailleMax(String[] message){
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
