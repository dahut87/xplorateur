package fr.meconnu.cache;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.calc.Geo;

public class Patrimoine implements Json.Serializable {
	private Float user_coordx,user_coordy;
	private int id_article;
	private String ville_nom_reel;
	private int insee;
	private String titre;
	private String texte;
	private Patrimoinetype types;
	private Date maj;
	private Date localmaj;
	private float coordx;
	private float coordy;
	private byte interet;
	private byte marche;
	private byte time;
	private byte acces;
	private boolean difficile;
	private boolean risque;
	private boolean coeur;
	private boolean argent;
	private boolean interdit;
	private String chien;
	private String labels;
	private String nom;
	private String id;
	private String mots;
	public enum Patrimoinetype {
		Architecture_civile("Architecture civile"),Architecture_religieuse("Architecture religieuse"),Batiment_ou_Friche_industriel("Bâtiment ou Friche industriel"),
		Chateaux_ouvrages_militaire("Châteaux, ouvrages militaires"),Culturel_et_artistique("Culturel et artistique"),Insolite_ou_inclassable("Insolite ou inclassable"),
		Mines_cavites_et_grottes("Mines, cavités et grottes"),Parc_jardin_ou_foret("Parc, jardin ou forêt"),Petit_patrimoine("Petit patrimoine"),Pierres_et_carrieres("Pierres et carrières"),
		Prehistoire_antiquite("Préhistoire, antiquité"),Riviere_lac("Rivière lac");

		private final String text;
		
		private Patrimoinetype(final String text) {
			this.text = text;
		}
		
		public static Patrimoinetype getPatrimoinetype(String text) {
			for(Patrimoinetype item :Patrimoinetype.values())
				if (item.toString().equals(text))
					return item;
			return null;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	public enum FieldType {
		TITRE("titre"),COMMUNE("insee"),TYPE("types"),
		MOTCLE("mots"),DATEMAJ("maj"),DATECACHE("localmaj"),
		INTERET("interet"),APPROCHE("marche"),DUREE("time"),ACCES("acces"),PROXIMITE("((coordx-%lat%)*(coordx-%lat%)) + ((coordy - %lon%)*(coordy - %lon%))");

		private final String text;
		
		private FieldType(final String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
		
	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		JsonValue index=jsonData;
		this.setId_article(json.readValue("id_article",Integer.class, index));
		this.setVille_nom_reel(json.readValue("ville_nom_reel",String.class, index));
		this.setInsee(json.readValue("insee",Integer.class, index));
		this.setTitre(json.readValue("titre",String.class, index));
		this.setTexte(json.readValue("texte",String.class, index));
		this.setTypes(Patrimoinetype.getPatrimoinetype(json.readValue("types",String.class, index)));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.setMaj(dateFormat.parse(json.readValue("maj",String.class, index)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			this.setMaj(new Date());
		}
		this.setCoordx(json.readValue("coordx",Float.class, index));
		this.setCoordy(json.readValue("coordy",Float.class, index));
		this.setInteret(json.readValue("interet",Byte.class, index));
		this.setMarche(json.readValue("marche",Byte.class, index));
		this.setTime(json.readValue("time",Byte.class, index));
		this.setAcces(json.readValue("acces",Byte.class, index));
		this.setDifficile(json.readValue("difficile",String.class, index).equals("on"));
		this.setRisque(json.readValue("risque",String.class, index).equals("on"));
		this.setCoeur(json.readValue("coeur",String.class, index).equals("on"));
		this.setArgent(json.readValue("argent",String.class, index).equals("on"));
		this.setInterdit(json.readValue("interdit",String.class, index).equals("on"));
		this.setChien(json.readValue("chien",String.class, index));
		this.setLabels(json.readValue("labels",String.class, index));
		this.setNom(json.readValue("nom",String.class, index));
		this.setId(json.readValue("id",String.class, index));
		this.setMots(json.readValue("mots",String.class, index));
	}
	
	public static String boolToStr(boolean val)
	{
		if (val)
			return "1";
			else
			return "0";
	}
	
	public void setUser(Vector2 position)
	{
		this.user_coordx=position.x;
		this.user_coordy=position.y;
	}
	
	public Vector2 getUser()
	{
		return new Vector2(user_coordx,user_coordy);
	}
	
	public Vector2 getPosition()
	{
		return new Vector2(coordx,coordy);
	}
	
	public Float GetDistance()
	{
		if (this.user_coordx!=null && this.user_coordy!=null)
			return Geo.Distance2D(getUser(),getPosition());
		else
			return 0f;
	}
	
	@Override
	public boolean equals(Object other){
		if (other!=null)
			return ((Patrimoine)other).getId().equals(this.getId());
		else
			return false;
	}
	
	public String getId() {
		if (id!=null)
			return id;
		else
			return "";
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNom() {
		if (nom!=null)
			return nom;
		else
			return "";
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLabels() {
		if (labels!=null)
			return labels;
		else
			return "";
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getChien() {
		if (chien!=null)
			return chien;
		else
			return "";
	}

	public void setChien(String chien) {
		this.chien = chien;
	}

	public boolean isInterdit() {
		return interdit;
	}
	
	public String getInterdit_str() {
		return boolToStr(interdit);
	}

	public void setInterdit(boolean interdit) {
		this.interdit = interdit;
	}

	public boolean isCoeur() {
		return coeur;
	}
	
	public String getCoeur_str() {
		return boolToStr(coeur);
	}

	public void setCoeur(boolean coeur) {
		this.coeur = coeur;
	}

	public boolean isRisque() {
		return risque;
	}
	
	public String getRisque_str() {
		return boolToStr(risque);
	}

	public void setRisque(boolean risque) {
		this.risque = risque;
	}

	public boolean isDifficile() {
		return difficile;
	}
	
	public String getDifficile_str() {
		return boolToStr(difficile);
	}

	public void setDifficile(boolean difficile) {
		this.difficile = difficile;
	}

	public byte getAcces() {
		return acces;
	}
	
	public String getAcces_str() {
		return String.valueOf(acces);
	}

	public void setAcces(byte acces) {
		this.acces = acces;
	}

	public byte getTime() {
		return time;
	}
	
	public String getTime_str() {
		return String.valueOf(time);
	}

	public void setTime(byte time) {
		this.time = time;
	}

	public byte getMarche() {
		return marche;
	}
	
	public String getMarche_str() {
		return String.valueOf(marche);
	}

	public void setMarche(byte marche) {
		this.marche = marche;
	}

	public byte getInteret() {
		return interet;
	}
	
	public String getInteret_str() {
		return String.valueOf(interet);
	}

	public void setInteret(byte interet) {
		this.interet = interet;
	}

	public float getCoordy() {
		return coordy;
	}
	
	public String getCoordy_str() {
		return String.valueOf(coordy);
	}

	public void setCoordy(float coordy) {
		this.coordy = coordy;
	}

	public float getCoordx() {
		return coordx;
	}
	
	public String getCoordx_str() {
		return String.valueOf(coordx);
	}

	public void setCoordx(float coordx) {
		this.coordx = coordx;
	}

	public Date getMaj() {
		if (maj!=null)
			return maj;
		else
			return (new Date());
	}
	
	public String getMaj_str() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		if (maj!=null) {
			return dateFormat.format(maj);
		}
		else
			return dateFormat.format(new Date());
	}
	
	public String getMaj_formated() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if (maj!=null) {
			return dateFormat.format(maj);
		}
		else
			return dateFormat.format(new Date());
	}

	public void setMaj(Date maj) {
		this.maj = maj;
	}

	public Patrimoinetype getTypes() {
		return types;
	}
	
	public String getTypes_str() {
		return types.toString();
	}

	public void setTypes(Patrimoinetype types) {
		this.types = types;
	}

	public String getTexte() {
		if (texte!=null)
			return texte;
		else
			return "";
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public String getTitre() {
		if (titre!=null)
			return titre;
		else
			return "";
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public int getInsee() {
		return insee;
	}
	
	public String getInsee_str() {
		return String.valueOf(insee);
	}

	public void setInsee(int insee) {
		this.insee = insee;
	}

	public String getVille_nom_reel() {
		if (ville_nom_reel!=null)
			return ville_nom_reel;
		else
			return "";
	}

	public void setVille_nom_reel(String ville_nom_reel) {
		this.ville_nom_reel = ville_nom_reel;
	}

	public int getId_article() {
		return id_article;
	}
	
	public String getId_article_str() {
		return String.valueOf(id_article);
	}

	public void setId_article(int id_article) {
		this.id_article = id_article;
	}

	public boolean isArgent() {
		return argent;
	}
	
	public String getArgent_str() {
		return boolToStr(argent);
	}

	public void setArgent(boolean argent) {
		this.argent = argent;
	}

	public Date getLocalmaj() {
		if (localmaj!=null)
			return localmaj;
		else
			return new Date();
	}
	
	public String getLocalMaj_str() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(localmaj);
	}
	
	public String getLocalmaj_formated() {
		if (localmaj!=null)
		{
		    Calendar startCalendar = new GregorianCalendar();
		    startCalendar.setTime(localmaj);
		    Calendar endCalendar = new GregorianCalendar();
		    endCalendar.setTime(new Date());
		    int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		    int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		    int diffDay = endCalendar.get(Calendar.DAY_OF_YEAR) - startCalendar.get(Calendar.DAY_OF_YEAR);
		    if (diffMonth<1)
		    	if (diffDay==1)
		    		return "1 jour";
		    	else if (diffDay==0)
		    		return "aujourd'hui";
		    	else
		    		return String.valueOf(diffMonth)+" jours";	
			else
				return String.valueOf(diffMonth)+" mois";
		}
		else
			return "Actuelle";
	}

	public void setLocalmaj(Date localmaj) {
		this.localmaj = localmaj;
	}

	public String getMots() {
		if (mots!=null)
			return mots;
		else
			return "";
	}

	public void setMots(String mots) {
		this.mots = mots;
	}
}

