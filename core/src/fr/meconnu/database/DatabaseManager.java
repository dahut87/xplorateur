package fr.meconnu.database;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.utils.Array;

public class DatabaseManager {
	private static Base[] bases;
	private static String[] urls;
	private static Array<Class<?>> backends;

	public Base getType(Base.datatype model) {
		return bases[model.ordinal()];
	}

	public Base cache() {
		return bases[Base.datatype.cache.ordinal()];
	}

	public Base waypoint() {
		return bases[Base.datatype.waypoint.ordinal()];
	}

	public Base patrimoine() {
		return bases[Base.datatype.patrimoine.ordinal()];
	}

	public boolean verifyall() {
		return (bases[0] != null && bases[1] != null && bases[2] != null);
	}

	public DatabaseManager() {
		bases = new Base[3];
		urls = new String[3];
		backends = new Array<Class<?>>();
	}

	public void CloseAll() {
		for (int i = 0; i < 3; i++)
			if (bases[i] != null) {
				bases[i].Close();
				bases[i] = null;
			}
	}

	public String geturlsOld(Base.datatype model) {
		return urls[model.ordinal()];
	}

	public boolean Attach(Base.datatype model, String Url) {
		if (bases[model.ordinal()] != null || model == null || Url == null)
			return false;
		Base backend = getBackend(model, Url);
		if (backend != null) {
			bases[model.ordinal()] = backend;
			urls[model.ordinal()] = Url;
			return true;
		} else {
			bases[model.ordinal()] = null;
			urls[model.ordinal()] = null;
			return false;
		}
	}

	public Base getBackend(Base.datatype model, String Url) {
		String Type = Url.split(":")[0];
		Class[] cArg = new Class[2];
		cArg[0] = Base.datatype.class;
		cArg[1] = String.class;
		for (Class<?> classe : backends) {
			Base back;
			try {
				back = (Base) classe.newInstance();
				if (back.getPrefix().equals(Type)) {
					back = (Base) classe.getDeclaredConstructor(cArg)
							.newInstance(model, Url);
					return back;
				}
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean isBackend(Base.datatype model, String Url) {
		String Type = Url.split(":")[0];
		for (Class<?> classe : backends) {
			Base back;
			try {
				back = (Base) classe.newInstance();
				if (back.getPrefix().equals(Type))
					return true;
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	public void RegisterBackend(Class<?> classe) {
		backends.add(classe);
	}

}