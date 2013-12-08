package wargame;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Classe représenant un monstre
 */
public class Monstre extends Soldat 
{	
	private static final long serialVersionUID = -2866246699288761404L;
	
	/** Permet de connaitre le type du héros */
	private TypesM typeMonstre;
	
	/** Images des Monstres. */
	static protected BufferedImage imageGobelin, imageOrc, imageSquelette;

	/** Change l'image du monstre selon son type */
	protected void setImage() throws IOException
	{
		switch(typeMonstre)
		{
			case GOBELIN:				
				if(imageGobelin == null) 
					imageGobelin = ImageIO.read(this.getClass().getResource(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_GOBELIN));
				image = imageGobelin;
				break;
			case ORC:				
				if(imageOrc == null) 
					imageOrc = ImageIO.read(this.getClass().getResource(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_ORC));
				image = imageOrc;
				break;
				
			case SQUELETTE:				
				if(imageSquelette == null) 
					imageSquelette = ImageIO.read(this.getClass().getResource(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_SQUELETTE));
				image = imageSquelette;
				break;
			default: break;
		}
	}
	
	/** 
	 * Constructeur du monstre 
	 * @param type_monstre Type du monstre
	 * @throws IOException Si l'image de peut pas être chargée
	 */
	Monstre(TypesM type_monstre) throws IOException
	{
		super();
		
		this.vie = type_monstre.getPoints();
		this.typeMonstre = type_monstre;
		
		setImage();
	}
	

	public int getPortee(){
		return typeMonstre.getPortee();
	}
	
	public int getPuissance(){
		return typeMonstre.getPuissance();
	}
	
	public int getTir(){
		return typeMonstre.getTir();
	}
	
	public String getNom(){
		return typeMonstre.getNom();
	}
	
	public int getVieMax(){
		return typeMonstre.getPoints();
	}
	
}
