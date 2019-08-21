package fr.meconnu.wrapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.speech.tts.TextToSpeech;

import de.longri.gdx.sqlite.GdxSqlite;
import de.longri.gdx.sqlite.GdxSqliteCursor;
import de.longri.gdx.sqlite.GdxSqlitePreparedStatement;
import fr.meconnu.app.AndroidLauncher;
import fr.meconnu.app.Wrapper;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Photo;
import fr.meconnu.cache.Photos;
import fr.meconnu.calc.Maths;

public class Android extends Wrapper {
	final private String databasename="cache.db";
	private GdxSqlite dbHandler;
	private Context context;
	private GPS gps;
	private TextToSpeech tts;
	private int speech;
	
	public Android() {
		this.context = AndroidLauncher.gettheApplication().getContext();
		this.gps = new GPS(this.context);
		tts=new TextToSpeech(this.context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
            	speech=tts.setLanguage(Locale.FRANCE);
            }
        });
	}

	public Activity getActivity(Context context)
	{
		if (context == null)
		{
			return null;
		}
		else if (context instanceof ContextWrapper)
		{
			if (context instanceof Activity)
			{
				return (Activity) context;
			}
			else
			{
				return getActivity(((ContextWrapper) context).getBaseContext());
			}
		}

		return null;
	}

	@Override
	public void Destroy() {
		tts.shutdown();
	}
	
	@Override
	public boolean isLocalisable() {
		return this.gps.canGetLocation;
	}
	
	@Override
	public boolean hasGPS() {
		return this.gps.isGPSEnabled;
	}

	@Override
    public void speak(String text)
    {
    	 if (tts.isSpeaking())
    	 	tts.stop();
		 if(speech!=TextToSpeech.LANG_MISSING_DATA && speech!=TextToSpeech.LANG_NOT_SUPPORTED){
			 		tts.speak(text, TextToSpeech.QUEUE_ADD, null);
         }
    }
	
	@Override
	public Localisationtype getLocalisationtype() {
	    if (this.gps.isGPSEnabled == true) 
	    	return Localisationtype.GPS;
	    else if (this.gps.isNetworkEnabled == true) 
	    	return Localisationtype.NETWORK;
	    else
	    	return Localisationtype.NONE;
	}
	
	@Override
	public float getAccuracy() {
	   return this.gps.accuracy;
	}
	
	@Override
	public float getSpeed() {
		   return this.gps.speed;
		}
	
	@Override
	public Vector3 getLocation() {
		//android.location.Location location=this.gps.getLocation();
		return new Vector3((float)this.gps.latitude,(float)this.gps.longitude,(float)this.gps.altitude);
	}
	
	@Override
	public Vector2 get2DLocation() {
		//android.location.Location location=this.gps.getLocation();
		return new Vector2((float)this.gps.latitude,(float)this.gps.longitude);
	}

	@Override
	public void Initbase() {
		try {
		/*switch (Gdx.app.getType()) {
		case Android:
			try {
				FileHandle newbase = Gdx.files.absolute("/data/data/fr.meconnu.app.android/databases/"+ databasename);
				if (!newbase.exists()) {
					Gdx.app.log("xplorateur-LocalBase", "***** Copie de la base de donnee android");
					Gdx.files.internal("bases/" + databasename).copyTo(newbase);
				}
			} catch (Exception e1) {
				Gdx.app.error("xplorateur-LocalBase", "Erreur de copie");
			}
			break;
		case Desktop:
			Gdx.app.log("xplorateur-LocalBase", "***** Copie de la base de donnee desktop");
			FileHandle newbase = Gdx.files.local(databasename);
			try {
				if (!newbase.exists())
					Gdx.files.internal("bases/" + databasename).copyTo(newbase);
			} catch (Exception e1) {
				Gdx.app.error("xplorateur-LocalBase", "Erreur de copie");
			}
			break;
		}*/
			if (dbHandler != null)
				Gdx.app.log("xplorateur-LocalBase", "Déjà connecté'");
			else {
				Gdx.app.log("xplorateur-LocalBase", "Connexion à la base");
				FileHandle dbFileHandle = Gdx.files.local(databasename);
				dbHandler = new GdxSqlite(dbFileHandle);

				try {
					dbHandler.openOrCreateDatabase();
				} catch (Exception e) {

					Gdx.app.error("xplorateur-LocalBase", "Erreur à l'ouverture de la base");
				}
			}
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
				dbHandler.execSQL("CREATE TABLE if not exists caches(id_article INTEGER, ville_nom_reel TEXT, insee INTEGER, titre TEXT, texte TEXT, types TEXT, maj DATETIME DEFAULT CURRENT_TIMESTAMP, localmaj DATETIME DEFAULT CURRENT_TIMESTAMP, coordx REAL, coordy REAL, interet INTEGER, marche INTEGER, time INTEGER, acces INTEGER, difficile INTEGER, risque INTEGER, coeur INTEGER, argent INTEGER, interdit INTEGER, chien STRING, labels TEXT, nom TEXT, id TEXT, mots TEXT, PRIMARY KEY(id));");
				dbHandler.execSQL("CREATE TABLE if not exists photos(id TEXT, aindex INTEGER, photo BLOB, localmaj DATETIME DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(id, aindex));");
				dbHandler.execSQL("CREATE TABLE if not exists waypoints(date DATETIME DEFAULT CURRENT_TIMESTAMP, level INTEGER NOT NULL, user INTEGER NOT NULL, PRIMARY KEY(level,user));");
				dbHandler.execSQL("CREATE TABLE if not exists patrimoines(date DATETIME DEFAULT CURRENT_TIMESTAMP, desc TEXT NOT NULL, object TEXT, PRIMARY KEY(desc));");
		} catch (Exception e) {
			Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage: "+e.toString());
		}
		finally
		{
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
	}

	@Override
	public void writeToCache(Patrimoines patrimoines) {
		for(Patrimoine patrimoine :patrimoines.getValues()) {
			try {
				String request="INSERT OR REPLACE INTO caches (id_article,ville_nom_reel,insee,titre,texte,types,maj,coordx,coordy,interet,marche,time,acces,difficile,risque,coeur,argent,interdit,chien,labels,nom,id,mots) VALUES ("+patrimoine.getId_article_str()+",'"+patrimoine.getVille_nom_reel().replace("'", "''")+"',"+patrimoine.getInsee_str()+",'"+patrimoine.getTitre().replace("'", "''")+"','"+patrimoine.getTexte().replace("'", "''")+"','"+patrimoine.getTypes_str()+"',date('"+patrimoine.getMaj_str()+"'),"+patrimoine.getCoordx_str()+","+patrimoine.getCoordy_str()+","+patrimoine.getInteret_str()+","+patrimoine.getMarche_str()+","+patrimoine.getTime_str()+","+patrimoine.getAcces_str()+","+patrimoine.getDifficile_str()+","+patrimoine.getRisque_str()+","+patrimoine.getCoeur_str()+","+patrimoine.getArgent_str()+","+patrimoine.getInterdit_str()+",'"+patrimoine.getChien().replace("'", "''")+"','"+patrimoine.getLabels().replace("'", "''")+"','"+patrimoine.getNom().replace("'", "''")+"','"+patrimoine.getId()+"','"+patrimoine.getMots().replace("'", "''")+"');";
				Gdx.app.debug("xplorateur-Localbase", "Requête de stockage: "+patrimoine.getId());
				dbHandler.execSQL(request);
			}
			catch (Exception e) {
				Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage: "+e.toString());
			}
			finally
			{
				try {
					dbHandler.endTransaction();
				}
				catch (Exception e)
				{
					Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
				}
			}
		}
	}

	@Override
	public int getNumCache() {
		GdxSqliteCursor cursor=null;
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select count(id_article) from caches;");
			if (cursor!=null)
				cursor.moveToFirst();
			while (cursor.getCount()>0)
				return cursor.getInt(0);
		}
		catch (Exception e)
		{
			Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			return 0;
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
				return 0;
			}
		}
		return 0;
	}

	@Override
	public Patrimoines readPatrimoines(Vector2 position, float angle, Patrimoine.FieldType field, int limit, boolean desc)
	{
		String ordering=field.toLabel();
		if (position==null)
			position=new Vector2(0,0);
		if (field== Patrimoine.FieldType.PROXIMITE)
			ordering=ordering.replace("%lat%", String.valueOf(position.x)).replace("%lon%", String.valueOf(position.y));
		String ordered="asc";
		if (desc)
			ordered="desc";
		return requestToPatrimoines("coordx<"+String.valueOf(position.x)+"+"+String.valueOf(angle/2)+" and coordx>"+String.valueOf(position.x)+"-"+String.valueOf(angle/2)+" and coordy<"+String.valueOf(position.y)+"+"+String.valueOf(angle/2)+" and coordy>"+String.valueOf(position.y)+"-"+String.valueOf(angle/2)+" order by "+ordering+" "+ordered+" limit "+String.valueOf(limit)+";");
	}

	@Override
	public Array<Criteria> readTitre(String text)
	{
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		result.add(new Criteria(Patrimoine.FieldType.TITRE,text));
		return result;
	}

	@Override
	public Array<Criteria> readText(String text)
	{
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		result.add(new Criteria(Patrimoine.FieldType.TEXTE,text));
		return result;
	}

	@Override
	public Array<Criteria> readMotcle(String text)
	{
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select distinct mots from caches where LOWER(mots) like '%"+text+"%' order by mots asc;");
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.MOTCLE,cursor.getString(0)));
				cursor.moveToNext();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		return result;
	}

	@Override
	public Array<Criteria> readCommune(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select distinct ville_code_commune from caches where LOWER(ville_nom_reel) like '%"+text+"%' order by ville_nom_reel asc;");
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.COMMUNE,cursor.getInt(0)));
				cursor.moveToNext();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		return result;
	}

	@Override
	public Array<Criteria> readInsee(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select distinct ville_code_commune from caches where insee like '%"+text+"%' order by ville_nom_reel asc;");
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.COMMUNE,cursor.getInt(0)));
				cursor.moveToNext();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		return result;
	}

	@Override
	public Array<Criteria> readType(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select distinct types from caches where LOWER(types) like '%"+text+"%' order by types asc;");
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.TYPE, Patrimoine.Patrimoinetype.getPatrimoinetype(cursor.getString(0))));
				cursor.moveToNext();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		return result;
	}

	@Override
	public String readPatrimoinesUptoDate(Vector2 position, float angle, String date) {
		return requestToString("id","localmaj>"+date+" and coordx<"+String.valueOf(position.x)+"+"+String.valueOf(angle/2)+" and coordx>"+String.valueOf(position.x)+"-"+String.valueOf(angle/2)+" and coordy<"+String.valueOf(position.y)+"+"+String.valueOf(angle/2)+" and coordy>"+String.valueOf(position.y)+"-"+String.valueOf(angle/2)+";");
	}

	public String requestToString(String field,String request) {
		GdxSqliteCursor cursor = null;
		String result="";
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select "+field+" from caches where "+request);
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result=result+","+cursor.getString(0);
				cursor.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		if (result.length()>1)
			return result.substring(1);
		else
			return "";
	}

	public Patrimoines requestToPatrimoines(String request) {
		Patrimoines patrimoines;
		patrimoines=new Patrimoines();
		GdxSqliteCursor cursor = null;
		try {
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select * from caches where "+request);
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Patrimoine patrimoine;
				patrimoine=new Patrimoine();
				patrimoine.setId_article(cursor.getInt(0));
				patrimoine.setVille_nom_reel(cursor.getString(1));
				patrimoine.setInsee(cursor.getInt(2));
				patrimoine.setTitre(cursor.getString(3));
				patrimoine.setTexte(cursor.getString(4));
				patrimoine.setTypes(Patrimoine.Patrimoinetype.getPatrimoinetype(cursor.getString(5)));
				String date;
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					date=cursor.getString(6);
					patrimoine.setMaj(dateFormat.parse(date));
					date=cursor.getString(7);
					patrimoine.setLocalmaj(dateFormat.parse(date));
				}
				catch (Exception e)
				{
				}
				patrimoine.setCoordx(cursor.getFloat(8));
				patrimoine.setCoordy(cursor.getFloat(9));
				patrimoine.setInteret((byte) cursor.getInt(10));
				patrimoine.setMarche((byte) cursor.getInt(11));
				patrimoine.setTime((byte) cursor.getInt(12));
				patrimoine.setAcces((byte) cursor.getInt(13));
				patrimoine.setDifficile(cursor.getInt(14)>0);
				patrimoine.setRisque(cursor.getInt(15)>0);
				patrimoine.setCoeur(cursor.getInt(16)>0);
				patrimoine.setArgent(cursor.getInt(17)>0);
				patrimoine.setInterdit(cursor.getInt(18)>0);
				patrimoine.setChien(cursor.getString(19));
				patrimoine.setLabels(cursor.getString(20));
				patrimoine.setNom(cursor.getString(21));
				patrimoine.setId(cursor.getString(22));
				patrimoine.setMots(cursor.getString(23));
				patrimoines.add(patrimoine);
				cursor.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.getStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		return patrimoines;
	}

	@Override
	public String getInformations()
	{
		GdxSqliteCursor cursor = null;
		String result="";
		try
		{
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select  count(id_article),min(localmaj),max(localmaj),count(distinct substr(insee,0,2)),count(distinct insee),count(distinct types) from caches;");
			if (cursor!=null)
				cursor.moveToFirst();
			result="Base de données PATRIMOINE\n -nombre de patrimoines: "+String.valueOf(cursor.getInt(0))+"\n -Plus ancien cache: "+String.valueOf(cursor.getString(1))+"\n -Plus récent cache:"+String.valueOf(cursor.getString(2))+"\n -Nombre de départements différents: "+String.valueOf(cursor.getInt(3))+"\n -Nombre de communes différentes:"+String.valueOf(cursor.getInt(4))+"\n -Nombre de types différents: "+String.valueOf(cursor.getInt(5));

			cursor = dbHandler.rawQuery("select sum(length(photo)),count(photo),count(distinct id),min(localmaj) from  photos");
			if (cursor!=null)
				cursor.moveToFirst();
			result=result+"\n\nBase de données PHOTOGRAPHIE\n -espace occupée: "+ Maths.humanReadableByteCount(cursor.getInt(0),true)+"\n -nombre de photos: "+String.valueOf(cursor.getInt(1))+"\n -nombre de patrimoines avec photo: "+String.valueOf(cursor.getInt(2))+"\n -Date des plus anciennes photos: "+String.valueOf(cursor.getString(3));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		return result;
	}

	@Override
	public void PhotosToCache(String id, int index, byte[] photo)
	{
		try {
			Gdx.app.debug("xplorateur-Localbase", "Requête de stockage photo: "+id+","+String.valueOf(index));
			String request="INSERT OR REPLACE INTO photos (id,aindex,photo,localmaj) VALUES ('"+id+"',"+index+",?,date('now'));";
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			GdxSqlitePreparedStatement preparedStatement = dbHandler.prepare(request);
			preparedStatement.bind(photo);
			preparedStatement.commit();
		}
		catch (Exception e)
		{
			Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage photo: "+id+","+String.valueOf(index)+"..."+e.getMessage());
		}
		finally
		{
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
	}

	@Override
	public Photos PhotosFromCache(Patrimoine patrimoine) {
		Photos photos;
		photos=new Photos(patrimoine);
		photos.clear();
		GdxSqliteCursor cursor = null;
		try {
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			cursor = dbHandler.rawQuery("select id,aindex,photo from photos where id='"+patrimoine.getId()+"'");
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String id=cursor.getString(0);
				int index=cursor.getInt(1);
				byte[] bytes = cursor.getBlob(2);
				Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
				Image image;
				if (pixmap.getFormat()==Pixmap.Format.Alpha)
				{
					Pixmap newpixmap=new Pixmap(pixmap.getWidth(),pixmap.getHeight(),Pixmap.Format.RGB888);
					newpixmap.setColor(Color.BLACK);
					newpixmap.fill();
					pixmap.setBlending(Pixmap.Blending.SourceOver);
					newpixmap.drawPixmap(pixmap, 0, 0);
					image = new Image(new Texture(newpixmap));
				}
				else
					image = new Image(new Texture(pixmap));
				Photo photo=new Photo(id,index,image.getDrawable());
				photos.add(photo);
				photos.SetCached();
				cursor.moveToNext();
			}
			photos.validate();
		}
		catch (Exception e)
		{
			Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
		}
		finally
		{
			if (cursor!=null)
				cursor.close();
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
		if (photos.getSize()>0)
			return photos;
		else
		{
			photos=new Photos(patrimoine);
			photos.getValue(0).netupdate();
			return photos;
		}
	}

	@Override
	public boolean Eraseall() {
		try {
			while(dbHandler.isInTransaction()) {}
			dbHandler.beginTransaction();
			dbHandler.execSQL("drop table if exists caches;");
			dbHandler.execSQL("drop table if exists waypoints;");
			dbHandler.execSQL("drop table if exists patrimoines;");
			return true;
		} catch (Exception e) {
			Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			return false;
		}
		finally
		{
			try {
				dbHandler.endTransaction();
			}
			catch (Exception e)
			{
				Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			}
		}
	}

	@Override
	public void Closebase() {
		try {
			if (dbHandler != null) {
				dbHandler.closeDatabase();
				dbHandler = null;
			}
		} catch (Exception e) {
			Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
		}
	}

}
