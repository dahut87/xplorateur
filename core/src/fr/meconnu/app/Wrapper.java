package fr.meconnu.app;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Photos;

public abstract class Wrapper {
	public enum Localisationtype {
		NETWORK("Interpolation"), GPS("GPS"), NONE("Aucun");
		private final String text;
		
		private Localisationtype(final String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public Wrapper() {
	}

	public void Destroy ()  {}
	
	public boolean hasGPS() {
		return false;
	}
	
	public boolean isLocalisable() {
		return false;
	}
	
	public float getSpeed() {
		return -1;
	}
	
	public float getAccuracy() {
		return -1;
	}
	
	public Localisationtype getLocalisationtype() {
		return null;
	}
	
	public Vector3 getLocation() {
		return null;
	}
	
	public Vector2 get2DLocation() {
		return null;
	}

	public void speak(String text) {	}

	public void Initbase() {}

	public void Closebase() {}

	public void writeToCache(Patrimoines patrimoines) {	}

	public int getNumCache() {
		return 0;
	}

	public String getInformations()
	{
		return "";
	}

	public Array<Criteria> readType(String text) {
		return null;
	}

	public Array<Criteria> readText(String text) {
		return null;
	}

	public Array<Criteria> readTitre(String text) {
		return null;
	}

	public Array<Criteria> readMotcle(String text) {
		return null;
	}

	public Array<Criteria> readCommune(String text) {
		return null;
	}

	public Array<Criteria> readInsee(String text)
	{
		return null;
	}

	public Patrimoines readPatrimoines(Vector2 position, float angle, Patrimoine.FieldType field, int limit, boolean desc) {
		return null;
	}

	public String readPatrimoinesUptoDate(Vector2 position, float angle, String date) {
		return null;
	}

	public Photos PhotosFromCache(Patrimoine patrimoine) {
		return null;
	}

	public void PhotosToCache(String id, int index, byte[] photo) {	}

	public boolean Eraseall() {
		return false;
	}

}
