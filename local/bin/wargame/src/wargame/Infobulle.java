package wargame;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;

final class Infobulle extends Rectangle implements ActionListener
{
	private static String message;
	private static Timer timer;

	public static void dessiner(Graphics g, int x, int y, String message, Color c)
	{
		int i = 0;
		String[] lignes = message.split("\n");
		
		FontMetrics fm = g.getFontMetrics(); 
		 
		int startX = x * IConfig.NB_PIX_CASE;
		int startY = y * IConfig.NB_PIX_CASE;
		int hauteur_ligne = g.getFontMetrics().getHeight();
		int largeur_rectangle = fm.stringWidth(Infobulle.stringTailleMax(lignes));
		
		Color old = g.getColor();  
		g.setColor(c);   
		g.fillRect( startX, startY, largeur_rectangle, lignes.length * hauteur_ligne);   

		g.setColor(Color.WHITE);
		for (String ligne : lignes){
			g.drawString( ligne, startX, startY += g.getFontMetrics().getHeight() ); 
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
