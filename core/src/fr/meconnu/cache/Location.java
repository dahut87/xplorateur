package fr.meconnu.cache;

import com.badlogic.gdx.math.Vector2;

public abstract class Location {
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
	
	public Location() {
	}
	
	public boolean isLocalisable() {
		return false;
	}
	
	public Localisationtype getLocalisationtype() {
		return null;
	}
	
	public Vector2 getLocation() {
		return null;
	}
}
