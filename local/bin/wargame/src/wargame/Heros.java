package wargame;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Classe représenant un héros
 */
public class Heros extends Soldat
{	
	private static final long serialVersionUID = -3793338387029787601L;
	
	/* Images des Héros. */
	static protected BufferedImage archer, elfe;
	
	/** Change l'image du héros selon son type */
	protected void setImage() throws IOException
	{
		switch(nom)
		{
			case "archer":				
				if(archer == null) archer = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.ARCHER));
				image = archer;
				break;
			case "elfe":				
				if(elfe == null) elfe = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.ELFE));
				image = elfe;
				break;
				
			default: break;
		}
	}
	
	/** 
	 * Constructeur du héros 
	 * @param type_heros Type du monstre
	 * @throws IOException Si l'image de peut pas être chargée
	 */
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
