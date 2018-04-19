package fr.meconnu.cache;

import java.util.Date;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoine.Patrimoinetype;

public class Patrimoines implements Json.Serializable,Cloneable {
		private Array<Patrimoine> array;
		private static Array<Criteria> filtre1,filtre2,filtreno,filtreselected;
		private final static int maxpatrimoines=200;
		
		@Override
		public void write(Json json) {
			// TODO Auto-generated method stub
			
		}
	
		public Patrimoines() {
			array=new Array<Patrimoine>();
			filtre1=new Array<Criteria> ();
			filtre2=new Array<Criteria> ();		
			filtreno=new Array<Criteria> ();	
		}
		
		public Array<Patrimoine> getValues() {
			return array;
		}
		
		public void add(Patrimoine patrimoine) {
			array.add(patrimoine);
		}
		
		public Patrimoine getValue(int index) {
			if (index+1>array.size) return null;
			return array.get(index);
		}
		
		public void removePatrimoine(Patrimoine patrimoine) {
			this.array.removeValue(patrimoine, false);
		}
		
		public void setUser(Patrimoine patrimoine)
		{
			if (patrimoine!=null)
			for(Patrimoine patrimoinedst: array)
				patrimoinedst.setUser(patrimoine.getPosition());
		}
		
		public Patrimoines clone() {
			Patrimoines patrimoines;
			patrimoines=new Patrimoines();
			for(Patrimoine patrimoine:this.array)
				 patrimoines.add(patrimoine.clone());
			return patrimoines;
		}
		
		public static void setFilter1(Array<Criteria> Criterias)
		{
			filtre1=Criterias;
		}
		
		public static void setFilter2(Array<Criteria> Criterias)
		{
			filtre2=Criterias;
		}
		
		public static Array<Criteria> getFilter1()
		{
			return filtre1;
		}
		
		public static Array<Criteria> getFilter2()
		{
			return filtre2;
		}
		
		public static void setFilter(int value)
		{
			if (value==0)
				filtreselected=filtreno;
			else if (value==1)
				filtreselected=filtre1;
			else
				filtreselected=filtre2;
		}
		
		public static Array<Criteria> getselectedFilter()
		{
			return filtreselected;
		}
		
		public static int getFilter()
		{
			int value=0;
			if (filtreselected==filtreno)
				value=0;
			else if (filtreselected==filtre1)
				value=1;
			else
				value=2;
			return value;
		}
		
		public static Patrimoines FilterPatrimoines(Patrimoines sendpatrimoines, FieldType field, Patrimoine patrimoine) {
			if (patrimoine!=null && field!=null && sendpatrimoines!=null)
			switch(field) {
			case TITRE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getTitre()), false);
			case COMMUNE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getInsee()), false);
			case TYPE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getTypes()), false);
			case MOTCLE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getMots()), false);
			case DATECACHE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getLocalmaj()), false);
			case DATEMAJ:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getMaj()), false);
			case INTERET:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getInteret()), false);
			case APPROCHE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getMarche()), false);
			case DUREE:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getTime()), false);
			case ACCES:
				return FilterPatrimoines(sendpatrimoines, new Criteria(field, patrimoine.getAcces()), false);
			default:
				return null;
			}
			return sendpatrimoines;
		}
		
		public static Patrimoines FilterPatrimoines(Patrimoines sendpatrimoines, Criteria criteria, boolean invert ) {
			Patrimoines newpatrimoines=new Patrimoines();
			boolean	prop=false;
			if (criteria!=null)
			{
				Object arg=criteria.getValues();
				for(Patrimoine patrimoine: sendpatrimoines.getValues())
				{
					switch(criteria.getTypes()) {
					case TITRE:
						prop=patrimoine.getTitre().contains((String)arg);
						break;
					case COMMUNE:
						prop=patrimoine.getInsee()==(int)arg;
						break;
					case TYPE:
						prop=patrimoine.getTypes()==(Patrimoinetype)arg;
						break;
					case MOTCLE:
						prop=patrimoine.getMots().contains((String)arg);
						break;
					case DATECACHE:
						prop=patrimoine.getLocalmaj().after((Date)arg);
						break;
					case DATEMAJ:
						prop=patrimoine.getMaj().after((Date)arg);
						break;
					case INTERET:
						prop=patrimoine.getInteret()==(int)arg;
						break;
					case APPROCHE:
						prop=patrimoine.getMarche()==(int)arg;
						break;
					case DUREE:
						prop=patrimoine.getTime()==(int)arg;
						break;
					case ACCES:
						prop=patrimoine.getAcces()==(int)arg;
						break;
					case CHIEN:
						prop=patrimoine.getChien().equals(arg);
						break;			
					case INTERDIT:
						prop=patrimoine.isInterdit()==(boolean)arg;
						break;
					case ARGENT:
						prop=patrimoine.isArgent()==(boolean)arg;
						break;
					case INSCRIT:
						prop=!(patrimoine.getLabels()!="");
						break;
						case DIFFICILE:
						prop=patrimoine.isDifficile()==(boolean)arg;
						break;
					case RISQUE:
						prop=patrimoine.isRisque()==(boolean)arg;
						break;
					case COEUR:
						prop=patrimoine.isCoeur()==(boolean)arg;
						break;
					case PHOTO:
						break;
					default:
						return null;
					}
					if (prop && !invert) newpatrimoines.add(patrimoine);
					if (!prop && invert) newpatrimoines.add(patrimoine);
				}
			}
			return newpatrimoines;
		}
		
		public static Patrimoines FilterPatrimoines(Patrimoines sendpatrimoines, Array<Criteria> Criterias ) {
			Patrimoines newpatrimoines=sendpatrimoines.clone();
			if (Criterias!=null)
			for (Criteria criteria:Criterias)
				newpatrimoines=FilterPatrimoines(newpatrimoines, criteria, false);
			return sendpatrimoines;
		}
		
		public static Patrimoines getNear(Patrimoine patrimoine) {
			if (patrimoine!=null) {
				Patrimoines patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(patrimoine.getPosition(),0.2f, FieldType.PROXIMITE, maxpatrimoines, false);
				//patrimoines.removePatrimoine(patrimoine);
				return FilterPatrimoines(patrimoines,filtreselected);
			}
			else
				return null;
		}
		
		public static Patrimoines getNear(Patrimoine patrimoine, int numbers) {
			if (patrimoine!=null) {
				Patrimoines patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(patrimoine.getPosition(),0.2f, FieldType.PROXIMITE, numbers, false);
				//patrimoines.removePatrimoine(patrimoine);
				return FilterPatrimoines(patrimoines,filtreselected);
			}
			else
				return null;
		}
		
		public static Patrimoines getNear(Vector2 position) {
			Patrimoines patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(position, 0.4f, FieldType.PROXIMITE, maxpatrimoines, false);
			return FilterPatrimoines(patrimoines,filtreselected);
		}
		
		public static Patrimoines getNear(Vector2 position, int numbers) {
			Patrimoines patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(position, 0.4f, FieldType.PROXIMITE, numbers, false);
			return FilterPatrimoines(patrimoines,filtreselected);
		}
		
		public static Patrimoines getNear() {
			Patrimoines patrimoines=null;
			if ( Filler.isLocaliser()) {
				patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(Filler.getLocaliser().get2DLocation(), 0.2f, FieldType.PROXIMITE, maxpatrimoines, false);
			}
			else
				patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(new Vector2(45f , 1.2f), 0.2f, FieldType.PROXIMITE, maxpatrimoines, false);
			return FilterPatrimoines(patrimoines,filtreselected);
		}
		
		public static Patrimoines getNear(int numbers) {
			Patrimoines patrimoines=null;
			if ( Filler.isLocaliser()) {
				patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(Filler.getLocaliser().get2DLocation(), 0.2f, FieldType.PROXIMITE, numbers, false);
			}
			else
				patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(new Vector2(45f , 1.2f), 0.2f, FieldType.PROXIMITE, numbers, false);
			return FilterPatrimoines(patrimoines,filtreselected);
		}

		@Override
		public void read(Json json, JsonValue jsonData) {
			Patrimoine patrimoine;
			// TODO Auto-generated method stub
			for (JsonValue child = jsonData.child; child != null; child = child.next) {
				patrimoine=json.readValue(Patrimoine.class, child);
				array.add(patrimoine);
	        }
		}
}
