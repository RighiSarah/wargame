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
	
	/* Vie max du Héros. */
	private final int VIE_MAX;
	
	public int getVieMax() {
		return VIE_MAX;
	}
	
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
		
		VIE_MAX = vie = type_heros.getPoints();
		portee = type_heros.getPortee();
		puissance = type_heros.getPuissance();
		tir = type_heros.getTir();
		nom = type_heros.getNom();
		
		setImage();
	}
	//protected void dessineDeplacement(Graphics g, int x, int y);
	
	/** Dessine la barre de vie du Héros.
	 * @param g : Zone de dessin. 
	 * @param x : Coordonnée X du personnage.
	 * @param y : Coordonnée Y du personnage.
	 */
	protected void dessineVie(Graphics g, int x, int y)
	{
		Color color;

		/* Couleur de la barre de vie. */
		int res = ((int)(100.0 * vie / (double)VIE_MAX));
		
		if(res >= 70)
			color = Color.green;
		else if(res >= 40)
			color = Color.orange;
		else
			color = Color.red;
		
		int dx = x * IConfig.NB_PIX_CASE + IConfig.NB_PIX_CASE;
		int dy = y * IConfig.NB_PIX_CASE + 2;
		
		/* Contenant. */
		g.setColor(Color.black);
		g.drawRect(dx + offsetX, dy + offsetY, 4, IConfig.NB_PIX_CASE - 2);
		
		/* Contenu. */
		int offset = (int)(IConfig.NB_PIX_CASE * vie / (double)VIE_MAX);
		g.setColor(color);
		g.fillRect(dx + 1 + offsetX , dy + 1 + offsetY + IConfig.NB_PIX_CASE - offset, 3, offset - 3);
	}
}
