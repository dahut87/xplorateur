package fr.meconnu.cache;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.calc.Geo;

public class Patrimoine implements Json.Serializable,Cloneable,Comparable {
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
	public enum Particularitetype {
		Chien_Autorise("chiens_oui"),Coup_de_coeur("coupdecoeur"),Gratuit("gratuit"),
		Inscrit("inscrit"),Avec_photo("image"),Facile_acces("facile");

		private final String text;
		
		private Particularitetype(final String text) {
			this.text = text;
		}
		
		public static Particularitetype getParticularitetype(String text) {
			for(Particularitetype item :Particularitetype.values())
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
		INTERET("Par intérêt","interet"),DUREE("Par durée","time"),ACCES("Par accès","acces"),APPROCHE("Par approche","marche"),TITRE("Par titre","titre"),COMMUNE("Par commune","insee"),TYPE("Par type","types"),
		MOTCLE("Par mot clé","mots"),DATEMAJ("Par date","maj"),DATECACHE("Par date cache","localmaj"),TEXTE("Par texte","texte"),CHIEN("","chien"),INTERDIT("","interdit"),ARGENT("","argent"),INSCRIT("","labels"),DIFFICILE("","difficile"),RISQUE("","risque"),COEUR("","coeur"),
		PROXIMITE("Par proximité","((coordx-%lat%)*(coordx-%lat%)) + ((coordy - %lon%)*(coordy - %lon%))"),PHOTO("","photo"),RESULTAT("","..."),ORDRE("","");
		
		private final String text;
		private final String label;
		
		private FieldType(final String label,final String text) {
			this.label = label;
			this.text = text;
		}
		
		@Override
		public String toString() {
			return label;
		}
		
		public String toLabel() {
			return text;
		}
	}
	public enum FieldSizeType {
		MAXIMALE(250,"Maximal"),GRANDE(150,"Important"),MOYENNE(100,"Moyen"),PETITE(50,"Faible");
		private final int size;
		private final String text;
		private FieldSizeType(final int size, final String text) {
			this.size = size;
			this.text = text;
		}
		
		public FieldSizeType getNext() {
		     return this.ordinal() < FieldSizeType.values().length - 1
		         ? FieldSizeType.values()[this.ordinal() + 1]
		         : FieldSizeType.values()[0];
		   }
		
		public FieldSizeType getPrevious() {
		     return this.ordinal() > 0
		         ? FieldSizeType.values()[this.ordinal() - 1]
		         : FieldSizeType.values()[FieldSizeType.values().length - 1];
		   }
		
		@Override
		public String toString() {
			return text;
		}
		
		public int toSize() {
			return size;
		}
	}
	
	@Override
	public Patrimoine clone() {
		Patrimoine patrimoine=new Patrimoine();
		patrimoine.setId_article(this.id_article);
		patrimoine.setVille_nom_reel(new String(this.ville_nom_reel));
		patrimoine.setInsee(this.insee);
		patrimoine.setTitre(new String(this.titre));
		patrimoine.setTexte(new String(this.texte));
		patrimoine.setTypes(this.types);
		patrimoine.setMaj(this.maj);
		patrimoine.setCoordx(this.coordx);
		patrimoine.setCoordy(this.coordy);
		patrimoine.setInteret(this.interet);
		patrimoine.setMarche(this.marche);
		patrimoine.setTime(this.time);
		patrimoine.setAcces(this.acces);
		patrimoine.setDifficile(this.difficile);
		patrimoine.setRisque(this.risque);
		patrimoine.setCoeur(this.coeur);
		patrimoine.setArgent(this.argent);
		patrimoine.setInterdit(this.interdit);
		patrimoine.setChien(this.chien);
		patrimoine.setLabels(new String(this.labels));
		patrimoine.setNom(new String(this.nom));
		patrimoine.setId(new String(this.id));
		patrimoine.setMots(new String(this.mots));
		return patrimoine;
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
	
	public Vector2 getPosition()
	{
		return new Vector2(coordx,coordy);
	}
	
	public Float GetDistance()
	{
		if (AssetLoader.wrapper.isLocalisable())
			return Geo.Distance2D(AssetLoader.wrapper.get2DLocation(),getPosition());
		else
			return -1f;
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

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return (int) Math.round(((Patrimoine)arg0).GetDistance()-this.GetDistance());
	}
	
	public static Comparator<Object> Proximite = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object1).GetDistance()-((Patrimoine)object2).GetDistance());
		}
	};
	
	public static Comparator<Object> Titre = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object1).getTitre().compareTo(((Patrimoine)object2).getTitre()));
		}
	};
	
	public static Comparator<Object> Interet = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object2).getInteret()-((Patrimoine)object1).getInteret());
		}
	};
	
	public static Comparator<Object> Duree = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object2).getTime()-((Patrimoine)object1).getTime());
		}
	};
	
	public static Comparator<Object> Type = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object1).getTypes().ordinal()-((Patrimoine)object2).getTypes().ordinal());
		}
	};
	
	public static Comparator<Object> Approche = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object2).getMarche()-((Patrimoine)object1).getMarche());
		}
	};
	
	public static Comparator<Object> Acces = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object2).getAcces()-((Patrimoine)object1).getAcces());
		}
	};
	
	public static Comparator<Object> Commune = new Comparator<Object>() {
		public int compare(Object object1,Object object2) {
			return (int) Math.round(((Patrimoine)object1).getInsee()-((Patrimoine)object2).getInsee());
		}
	};
	
}

