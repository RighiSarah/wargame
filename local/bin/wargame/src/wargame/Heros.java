package wargame;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Heros extends Soldat
{	
	/* Images des Héros. */
	static protected BufferedImage archer, elfe;

	Heros(TypesH type_heros) throws IOException
	{
		super();
		
		vie = type_heros.getPoints();
		portee = type_heros.getPortee();
		puissance = type_heros.getPuissance();
		tir = type_heros.getTir();
		
		switch(type_heros.getNom())
		{
			case "archer":				
				if(archer == null) archer = ImageIO.read(new File("archer.png"));
				image = archer;
				break;
			case "elfe":				
				if(elfe == null) elfe = ImageIO.read(new File("elfe.png"));
				image = elfe;
				break;
				
			default: break;
		}
	}
}
