package wargame;

public class Heros extends Soldat {
	TypesH s;
	protected TypesH[] EHeros;
	
	Heros(){
		for (int i = 0; i < IConfig.NB_HEROS ; i++)
			EHeros[i] = ISoldat.TypesH.getTypeHAlea();	
	}

}
