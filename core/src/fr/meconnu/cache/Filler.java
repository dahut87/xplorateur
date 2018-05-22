package fr.meconnu.cache;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Application.ApplicationType;
import fr.meconnu.app.Xplorateur;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Location.Localisationtype;
import fr.meconnu.calc.Geo;

public class Filler {
	private static Location location;
	private static Timer FillTimer;
	private static TimerTask FillTask;
	private static int FillCounter;
	private static Vector3 oldposition,position;
	private static Movetype movetype;
	private static Loader loader;
	final static int cachespeed=60;
	final static int monthlife=60;
	
	public enum Movetype {
		MOTORCYCLE("Moto",30f), CAR("Voiture",16f), CYCLE("Velo",3f), FOOT("Pied",0.5f), STOP("Arret",0f), NOSTATUS("rien",0f);
		private final String text;
		private final Float speed;
		
		private Movetype(final String text, final Float speed) {
			this.text = text;
			this.speed = speed;
		}
		
		public static Movetype getMovetype(float speed2) {
			for(Movetype move : Movetype.values())
				if(speed2 >= move.speed) return move;
			return movetype.NOSTATUS;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum Cachetype {
		GOOD("cache4",200), MEDIUM("cache3",50), LOW("cache2",20), VERYLOW("cache1",1), EMPTY("cache0",0);
		private final String text ;
		private final int size;
		
		private Cachetype(final String text, final int size) {
			this.text = text;
			this.size = size;
		}
		
		public static Cachetype getCachetype(int number) {
			for(Cachetype capacity : Cachetype.values())
				if(number >= capacity.size) return capacity;
			return Cachetype.EMPTY;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public static Cachetype getCachelevel() {
		if (AssetLoader.Datahandler.cache()!=null)
			return Cachetype.getCachetype(AssetLoader.Datahandler.cache().getNumCache());
		else
			return Cachetype.EMPTY;
	}
	
	public static Movetype getMovetype() {
		if (location!=null)
			return movetype;
		else
			return Movetype.STOP;
	}
	
	public static Location getLocaliser() {
		return location;
	}
	
	public static Vector2 getLocalisation() {
		if (isLocaliser())
			return getLocaliser().get2DLocation();
		else
			return new Vector2(45.038835f , 1.237758f);		
	}
	
	public static boolean isLocaliser() {
		return location!=null;
	}
	
	public static boolean isRunning() {
		return (location!=null);
	}
	
	public static float getSpeed() {
		if (Filler.isLocaliser() && Filler.getLocaliser().isLocalisable())
		{
			float speed=Filler.getLocaliser().getSpeed();
			if (speed<=0)
				return Geo.Distance2D(position,oldposition);
			else
				return speed;
		}
		else
			return -1f;
	}
	
	public static void Fill() {
		FillCounter++;
		if (location!=null) {
			Gdx.app.debug("xplorateur-filler","Récupération du positionnement");
			oldposition=position;
			position=location.getLocation();
			Gdx.app.debug("xplorateur-filler","Ancienne:"+position.toString()+" Nouvelle:"+oldposition.toString());
			float speed=getSpeed();
			Gdx.app.debug("xplorateur-filler","distance : "+String.valueOf(speed));
			movetype=Movetype.getMovetype(speed);
		}
		if (FillCounter>=cachespeed) {
			FillCounter=0;
			if (location!=null) {
				Gdx.app.debug("xplorateur-filler","Requête locale d'exception ");
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date date = new Date();
				GregorianCalendar gc = new GregorianCalendar();
		        gc.setTime(date);
		        gc.add(GregorianCalendar.MONTH, monthlife);
		        Vector2 coords=Filler.getLocaliser().get2DLocation();
				String except=AssetLoader.Datahandler.cache().readPatrimoinesUptoDate(coords, 0.2f, dateFormat.format(gc.getTime()));
				Gdx.app.debug("xplorateur-filler","Requête avec déplacement : "+movetype.toString());
				loader.Request(coords, movetype, except);
			}
			else
			{
				Gdx.app.debug("xplorateur-filler","Requête ping");
				ping();
			}
		}	
	}
	
	public static void ping() {
		loader.Request(new Vector2(0f, 0f), Movetype.NOSTATUS, "");
	}
	
	public static void test() {
		loader.Request(new Vector2(45.038835f , 1.237758f) , Movetype.STOP, "");
	}
	
	public static boolean isAccessible()
	{
		return loader.isAccessible();
	}
	
	public Filler(Location location) {
		this.location=location;
		this.loader=new Loader("https://meconnu.fr/patrimoines.php");
	}
	
	public void init() {
		Gdx.app.debug("xplorateur-filler","Initialisation des valeurs");
		FillCounter=0;
		position = new Vector3(0,0,0);
		oldposition = new Vector3(0,0,0);	
		movetype = Movetype.NOSTATUS;
		if (Filler.isRunning())
			Gdx.app.debug("xplorateur-filler","Test du système de positionnement:"+this.location.getLocalisationtype().toString());
		Gdx.app.debug("xplorateur-filler", "Mise en place du timer.");
		FillTimer = new Timer();
		FillTask = new TimerTask() {
				@Override
				public void run() {
					Filler.Fill();
			}
		};
		FillTimer.scheduleAtFixedRate(FillTask, 2000, 1000);
	}

}
