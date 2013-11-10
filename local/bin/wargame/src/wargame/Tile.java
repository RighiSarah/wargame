package wargame;

import java.io.Serializable;

/** Case d'un tileset.
 * @author ABHAMON Ronan 
*/
public class Tile
{
	/** Praticabilité. Une personne peut-elle marcher dessus ? */
	private boolean praticable;
	
	/** Traversabilité. Une flèche peut-elle traverser ? */
	private boolean traversable;
	
	/** Crée une nouvelle tuile de la map.
	 * 
	 * @param praticable  Définit si la tuile est praticable
	 * @param traversable Définit si une tuile peut être traversée par une flèche
	 */
	Tile(boolean praticable, boolean traversable)
	{
		this.praticable = praticable;
		this.traversable = traversable;
	}
	
	/** Teste si le tileset est praticable.
	 * 
	 * @return true si praticable, false sinon.
	 */
	public boolean estPraticable()
	{
		return praticable;
	}
	
	/** Teste si le tileset est traversable.
	 * 
	 * @return true si traversable, false sinon.
	 */
	public boolean estTraversable()
	{
		return traversable;
	}
	
	/** Définit si un tile est praticable.
	 * 
	 * @param praticable true ou false
	 */
	public void setPraticable(boolean praticable)
	{
		this.praticable = praticable;
	}
	
	/** Définit si un tile est traversable.
	 * 
	 * @param traversable true ou false
	 */
	public void setTraversable(boolean traversable)
	{
		this.traversable = traversable;
	}
}
