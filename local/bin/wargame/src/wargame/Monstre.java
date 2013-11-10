package wargame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monstre extends Soldat 
{	
	/* Images des Monstres. */
	static protected BufferedImage gobelin, orc;
	
	Monstre(TypesM type_monstre) throws IOException
	{
		super();
		
		vie = type_monstre.getPoints();
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
}
