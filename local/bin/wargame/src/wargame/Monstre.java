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
	
	/** Images des Monstres. */
	static protected BufferedImage gobelin, orc;

	/** Change l'image du monstre selon son type */
	protected void setImage() throws IOException
	{
		switch(nom)
		{
			case "gobelin":				
				if(gobelin == null) 
					gobelin = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.GOBELIN));
				image = gobelin;
				break;
			case "orc":				
				if(orc == null) 
					orc = ImageIO.read(new File(IConfig.CHEMIN_IMAGE + IConfig.ORC));
				image = orc;
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
		
		setImage();
	}
}
