package fr.meconnu.database;

import com.badlogic.gdx.math.Vector2;

import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.database.Base.datatype;

public abstract class Base {
	public enum datatype {
		cache, waypoint, patrimoine
	}

	public Base(datatype model, String param) {
	}

	public Base() {
	}

	public String getParam() {
		return null;
	}
	
	public void init(datatype model) {
	}

	// Gestion type cache
	
	public void writeToCache(Patrimoines patrimoines) {
		
	}
	
	public int getNumCache() {
		return 0;
	}
	
	public Patrimoines readPatrimoines(Vector2 position, float angle, FieldType field, int limit, boolean desc) {
		return null;
	}
	
	public String readPatrimoinesUptoDate(Vector2 position, float angle, String date) {
		return null;
	}
	
	// Gestion type waypoint
	
	// Gestion type patrimoine

	// Commun

	public boolean Eraseall() {
		return false;
	}

	public static boolean isHandling(datatype base) {
		return false;
	}

	public void Close() {
	}

	public String getPrefix() {
		return "";
	}

}
