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
	
	/** Permet de connaitre le type du héros */
	private TypesH typeHeros;
	
	/** Images des Héros. */
	static protected BufferedImage imageHumain, imageElfe, imageSoldat;
	
	/** Change l'image du héros selon son type */
	protected void setImage() throws IOException
	{
		switch(typeHeros)
		{
			case HUMAIN:				
				if(imageHumain == null) 
					imageHumain = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_HUMAIN));
				
				image = imageHumain;
				break;
			case ELFE:				
				if(imageElfe == null) 
					imageElfe = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_ELFE));
				
				image = imageElfe;
				break;
				
			case SOLDAT:				
				if(imageSoldat == null) 
					imageSoldat = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_SOLDAT));
				
				image = imageSoldat;
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
		typeHeros = type_heros;
		
		setImage();
	}
}
