package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monstre extends Soldat 
{	
	/* Images des Monstres. */
	static protected BufferedImage gobelin, orc;
	
	/* Vie max du monstre. */
	private final int VIE_MAX;

	Monstre(TypesM type_monstre) throws IOException
	{
		super();
		
		VIE_MAX = vie = type_monstre.getPoints();
		portee = type_monstre.getPortee();
		puissance = type_monstre.getPuissance();
		tir = type_monstre.getTir();
		
		switch(type_monstre.getNom())
		{
			case "gobelin":				
				if(gobelin == null) gobelin = ImageIO.read(new File("gobelin.png"));
				image = gobelin;
				break;
			case "orc":				
				if(orc == null) orc = ImageIO.read(new File("orc.png"));
				image = orc;
				break;
				
			default: break;
		}
	}
	
	/** Dessine la barre de vie du Monstre. */
	protected void dessineVie(Graphics g, int x, int y)
	{
		Color color;

		/* Couleur de la barre de vie. */
		int res = ((int)(100.0 * vie / (double)VIE_MAX));
		
		if(res >= 70)
			color = Color.gray;
		else if(res >= 40)
			color = Color.lightGray;
		else
			color = Color.red;
		
		int dx = x * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE;
		int dy = y * IConfig.NB_PIX_CASE + 2;
		
		/* Contenant. */
		g.setColor(Color.black);
		g.drawRect(dx, dy, 4, IConfig.NB_PIX_CASE - 2);
		
		/* Contenu. */
		int offset = (int)(IConfig.NB_PIX_CASE * vie / (double)VIE_MAX);
		g.setColor(color);
		g.fillRect(dx + 1 , dy + 1 + IConfig.NB_PIX_CASE - offset, 3, offset - 3);
	}
}
