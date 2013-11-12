package wargame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;

public class Monstre extends Soldat 
{	
	/* Images des Monstres. */
	static protected BufferedImage gobelin, orc;

	protected void setImage() throws IOException
	{
		switch(nom)
		{
			case "gobelin":				
				if(gobelin == null) gobelin = ImageIO.read(new File(IConfig.GOBELIN));
				image = gobelin;
				break;
			case "orc":				
				if(orc == null) orc = ImageIO.read(new File(IConfig.ORC));
				image = orc;
			//	this.setOffset(20);
				break;
				
			default: break;
		}
	}
	
	Monstre(TypesM type_monstre) throws IOException
	{
		super();
		
		vieMax = vie = type_monstre.getPoints();
		portee = type_monstre.getPortee();
		puissance = type_monstre.getPuissance();
		tir = type_monstre.getTir();
		nom = type_monstre.getNom();
		
		setImage();
	}
}
