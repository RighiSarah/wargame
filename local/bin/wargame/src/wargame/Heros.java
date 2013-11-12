package wargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Heros extends Soldat
{	
	/* Images des Héros. */
	static protected BufferedImage archer, elfe;
	
	protected void setImage() throws IOException
	{
		switch(nom)
		{
			case "archer":				
				if(archer == null) archer = ImageIO.read(new File(IConfig.ARCHER));
				image = archer;
				break;
			case "elfe":				
				if(elfe == null) elfe = ImageIO.read(new File(IConfig.ELFE));
				image = elfe;
				break;
				
			default: break;
		}
	}
	
	Heros(TypesH type_heros) throws IOException
	{
		super();
		
		vieMax = vie = type_heros.getPoints();
		portee = type_heros.getPortee();
		puissance = type_heros.getPuissance();
		tir = type_heros.getTir();
		nom = type_heros.getNom();
		
		setImage();
	}
}
