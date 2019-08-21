package fr.meconnu.wrapper;

import java.sql.SQLException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import fr.meconnu.app.Wrapper.Localisationtype;
import fr.meconnu.app.Wrapper;

public class Desktop extends Wrapper {
	
	public Desktop() {
	}
	
	@Override
	public boolean hasGPS() {
		return false;
	}

	@Override
	public void speak(String text) {}
	
	@Override
	public boolean isLocalisable() {
		return false;
	}
	
	@Override
	public float getSpeed() {
		return -1;
	}
	
	@Override
	public float getAccuracy() {
		return -1;
	}
	
	@Override
	public Localisationtype getLocalisationtype() {
		return Localisationtype.NONE;
	}
	
	@Override
	public Vector3 getLocation() {
		return new Vector3(45.041971f, 1.23997f, 200f);
	}
	
	@Override
	public Vector2 get2DLocation() {
		return new Vector2(45.041971f, 1.23997f);
	}
}
