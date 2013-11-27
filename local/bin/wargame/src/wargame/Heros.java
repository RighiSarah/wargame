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
	static protected BufferedImage imageHumain, imageElfe, imageNain;
	
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
				
			case NAIN:				
				if(imageNain == null) 
					imageNain = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_NAIN));
				
				image = imageNain;
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
		
		vie = type_heros.getPoints();
		typeHeros = type_heros;
		
		setImage();
	}
	
	public int getPortee(){
		return typeHeros.getPortee();
	}
	
	public int getPuissance(){
		return typeHeros.getPuissance();
	}
	
	public int getTir(){
		return typeHeros.getTir();
	}
	
	public String getNom(){
		return typeHeros.getNom();
	}
	
	public int getVieMax(){
		return typeHeros.getPoints();
	}
}
