package fr.meconnu.app;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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

	public void speak(String text) {
		// TODO Auto-generated method stub
		
	}
}
