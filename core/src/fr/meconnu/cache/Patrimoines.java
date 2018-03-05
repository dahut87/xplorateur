package fr.meconnu.cache;

import java.util.Date;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoine.Patrimoinetype;

public class Patrimoines implements Json.Serializable {
		   public Array<Patrimoine> array;
		@Override
		public void write(Json json) {
			// TODO Auto-generated method stub
			
		}
	
		public Patrimoines() {
			array=new Array<Patrimoine>();
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
		
		public static Patrimoines FilterPatrimoines(Patrimoines sendpatrimoines, FieldType field, Patrimoine patrimoine) {
			if (patrimoine!=null && field!=null && sendpatrimoines!=null)
			switch(field) {
			case TITRE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getTitre(), false);
			case COMMUNE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getInsee(), false);
			case TYPE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getTypes(), false);
			case MOTCLE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getMots(), false);
			case DATECACHE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getLocalmaj(), false);
			case DATEMAJ:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getMaj(), false);
			case INTERET:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getInteret(), false);
			case APPROCHE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getMarche(), false);
			case DUREE:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getTime(), false);
			case ACCES:
				return FilterPatrimoinesByarg(sendpatrimoines, field, patrimoine.getAcces(), false);
			}
			return sendpatrimoines;
		}
		
		public static Patrimoines FilterPatrimoinesByarg(Patrimoines sendpatrimoines, FieldType field, Object arg, boolean invert) {
			Patrimoines newpatrimoines=new Patrimoines();
			for(Patrimoine patrimoine: sendpatrimoines.getValues())
			{
			boolean	prop=false;
			switch(field) {
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
			}
			if (prop && !invert) newpatrimoines.add(patrimoine);
			if (!prop && invert) newpatrimoines.add(patrimoine);
			}
			return newpatrimoines;
		}
		
		public static Patrimoines getNear(Patrimoine patrimoine) {
			if (patrimoine!=null) {
				Patrimoines patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(patrimoine.getPosition(),0.2f, FieldType.PROXIMITE, 200, false);
				//patrimoines.removePatrimoine(patrimoine);
				return patrimoines;
			}
			else
				return null;
		}
		
		public static Patrimoines getNear(Patrimoine patrimoine, int numbers) {
			if (patrimoine!=null) {
				Patrimoines patrimoines=AssetLoader.Datahandler.cache().readPatrimoines(patrimoine.getPosition(),0.2f, FieldType.PROXIMITE, numbers, false);
				//patrimoines.removePatrimoine(patrimoine);
				return patrimoines;
			}
			else
				return null;
		}
		
		public static Patrimoines getNear(Vector2 position) {
			return AssetLoader.Datahandler.cache().readPatrimoines(position, 0.2f, FieldType.PROXIMITE, 200, false);
		}
		
		public static Patrimoines getNear(Vector2 position, int numbers) {
			return AssetLoader.Datahandler.cache().readPatrimoines(position, 0.2f, FieldType.PROXIMITE, numbers, false);
		}
		
		public static Patrimoines getNear() {
			if ( Filler.getLocaliser()!=null) {
				return AssetLoader.Datahandler.cache().readPatrimoines(Filler.getLocaliser().getLocation(), 0.2f, FieldType.PROXIMITE, 200, false);
			}
			else
				return AssetLoader.Datahandler.cache().readPatrimoines(new Vector2(45f , 1.2f), 0.2f, FieldType.PROXIMITE, 200, false);
		}
		
		public static Patrimoines getNear(int numbers) {
			if ( Filler.getLocaliser()!=null) {
				return AssetLoader.Datahandler.cache().readPatrimoines(Filler.getLocaliser().getLocation(), 0.2f, FieldType.PROXIMITE, numbers, false);
			}
			else
				return AssetLoader.Datahandler.cache().readPatrimoines(new Vector2(45f , 1.2f), 0.2f, FieldType.PROXIMITE, numbers, false);
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
