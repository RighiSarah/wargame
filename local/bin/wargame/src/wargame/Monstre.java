package wargame;

import java.awt.image.BufferedImage;
import java.io.File;
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
	static protected BufferedImage imageGobelin, imageOrc, imageTroll;

	/** Change l'image du monstre selon son type */
	protected void setImage() throws IOException
	{
		switch(typeMonstre)
		{
			case GOBELIN:				
				if(imageGobelin == null) 
					imageGobelin = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_GOBELIN));
				image = imageGobelin;
				break;
			case ORC:				
				if(imageOrc == null) 
					imageOrc = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_ORC));
				image = imageOrc;
				break;
				
			case TROLL:				
				if(imageTroll == null) 
					imageTroll = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.IMAGE_TROLL));
				image = imageTroll;
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
		
		this.vieMax = this.vie = type_monstre.getPoints();
		this.portee = type_monstre.getPortee();
		this.puissance = type_monstre.getPuissance();
		this.tir = type_monstre.getTir();
		this.nom = type_monstre.getNom();
		this.typeMonstre = type_monstre;
		
		setImage();
	}
}
