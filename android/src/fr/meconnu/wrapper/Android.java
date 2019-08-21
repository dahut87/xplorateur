package fr.meconnu.wrapper;

import java.sql.PreparedStatement;
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

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteStatement;
import android.speech.tts.TextToSpeech;

import fr.meconnu.app.AndroidLauncher;
import fr.meconnu.app.Wrapper;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Photo;
import fr.meconnu.cache.Photos;
import fr.meconnu.calc.Maths;
import fr.meconnu.database.MySQLite;

public class Android extends Wrapper {
	private MySQLite mySQLite;
	private SQLiteDatabase db;
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
			if (db != null)
				Gdx.app.log("xplorateur-LocalBase", "Déjà connecté'");
			else {
				Gdx.app.log("xplorateur-LocalBase", "Connexion à la base");
				mySQLite = MySQLite.getInstance(context);
				db = mySQLite.getWritableDatabase();
			}
		}
			catch (Exception e) {
				Gdx.app.error("xplorateur-LocalBase", "Erreur à l'ouverture de la base");
			}
	}

	@Override
	public void writeToCache(Patrimoines patrimoines) {
		for(Patrimoine patrimoine :patrimoines.getValues()) {
			try {
				while(db.inTransaction()) {}
				db.beginTransaction();
				String request="INSERT OR REPLACE INTO caches (id_article,ville_nom_reel,insee,titre,texte,types,maj,coordx,coordy,interet,marche,time,acces,difficile,risque,coeur,argent,interdit,chien,labels,nom,id,mots) VALUES ("+patrimoine.getId_article_str()+",'"+patrimoine.getVille_nom_reel().replace("'", "''")+"',"+patrimoine.getInsee_str()+",'"+patrimoine.getTitre().replace("'", "''")+"','"+patrimoine.getTexte().replace("'", "''")+"','"+patrimoine.getTypes_str()+"',date('"+patrimoine.getMaj_str()+"'),"+patrimoine.getCoordx_str()+","+patrimoine.getCoordy_str()+","+patrimoine.getInteret_str()+","+patrimoine.getMarche_str()+","+patrimoine.getTime_str()+","+patrimoine.getAcces_str()+","+patrimoine.getDifficile_str()+","+patrimoine.getRisque_str()+","+patrimoine.getCoeur_str()+","+patrimoine.getArgent_str()+","+patrimoine.getInterdit_str()+",'"+patrimoine.getChien().replace("'", "''")+"','"+patrimoine.getLabels().replace("'", "''")+"','"+patrimoine.getNom().replace("'", "''")+"','"+patrimoine.getId()+"','"+patrimoine.getMots().replace("'", "''")+"');";
				Gdx.app.debug("xplorateur-Localbase", "Requête de stockage: "+patrimoine.getId());
				db.execSQL(request);
				db.setTransactionSuccessful();
			}
			catch (Exception e) {
				Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage: "+e.toString());
			}
			finally
			{
				try {
					db.endTransaction();
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
		Cursor cursor=null;
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select count(id_article) from caches;",null);
			if (cursor!=null)
				cursor.moveToFirst();
			while (cursor.getCount()>0)
				return cursor.getInt(0);
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select distinct mots from caches where LOWER(mots) like '%?%' order by mots asc;",new String[] {text});
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.MOTCLE,cursor.getString(0)));
				cursor.moveToNext();
			}
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select distinct ville_code_commune from caches where LOWER(ville_nom_reel) like '%?%' order by ville_nom_reel asc;",new String[] {text});
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.COMMUNE,cursor.getInt(0)));
				cursor.moveToNext();
			}
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select distinct ville_code_commune from caches where insee like '%?%' order by ville_nom_reel asc;",new String[] {text});
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.COMMUNE,cursor.getInt(0)));
				cursor.moveToNext();
			}
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select distinct types from caches where LOWER(types) like '%?%' order by types asc;",new String[] {text});
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result.add(new Criteria(Patrimoine.FieldType.TYPE, Patrimoine.Patrimoinetype.getPatrimoinetype(cursor.getString(0))));
				cursor.moveToNext();
			}
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		String result="";
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select "+field+" from caches where "+request,null);
			if (cursor!=null)
				cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result=result+","+cursor.getString(0);
				cursor.moveToNext();
			}
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		try {
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select * from caches where "+request,null);
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
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
		Cursor cursor = null;
		String result="";
		try
		{
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select  count(id_article),min(localmaj),max(localmaj),count(distinct substr(insee,0,2)),count(distinct insee),count(distinct types) from caches;",null);
			if (cursor!=null)
				cursor.moveToFirst();
			result="Base de données PATRIMOINE\n -nombre de patrimoines: "+String.valueOf(cursor.getInt(0))+"\n -Plus ancien cache: "+String.valueOf(cursor.getString(1))+"\n -Plus récent cache:"+String.valueOf(cursor.getString(2))+"\n -Nombre de départements différents: "+String.valueOf(cursor.getInt(3))+"\n -Nombre de communes différentes:"+String.valueOf(cursor.getInt(4))+"\n -Nombre de types différents: "+String.valueOf(cursor.getInt(5));

			cursor = db.rawQuery("select sum(length(photo)),count(photo),count(distinct id),min(localmaj) from  photos",null);
			if (cursor!=null)
				cursor.moveToFirst();
			result=result+"\n\nBase de données PHOTOGRAPHIE\n -espace occupée: "+ Maths.humanReadableByteCount(cursor.getInt(0),true)+"\n -nombre de photos: "+String.valueOf(cursor.getInt(1))+"\n -nombre de patrimoines avec photo: "+String.valueOf(cursor.getInt(2))+"\n -Date des plus anciennes photos: "+String.valueOf(cursor.getString(3));
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
			while(db.inTransaction()) {}
			db.beginTransaction();
			SQLiteStatement preparedStatement = db.compileStatement(request);

			preparedStatement.clearBindings();
			preparedStatement.bindBlob(1, photo);
			preparedStatement.executeInsert();
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage photo: "+id+","+String.valueOf(index)+"..."+e.getMessage());
		}
		finally
		{
			try {
				db.endTransaction();
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
		Cursor cursor = null;
		try {
			while(db.inTransaction()) {}
			db.beginTransaction();
			cursor = db.rawQuery("select id,aindex,photo from photos where id='?'",new String[] {patrimoine.getId()});
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
			db.setTransactionSuccessful();
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
				db.endTransaction();
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
			while(db.inTransaction()) {}
			db.beginTransaction();
			db.execSQL("drop table if exists caches;");
			db.execSQL("drop table if exists waypoints;");
			db.execSQL("drop table if exists patrimoines;");
			db.setTransactionSuccessful();
			return true;
		} catch (Exception e) {
			Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
			return false;
		}
		finally
		{
			try {
				db.endTransaction();
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
			if (mySQLite != null) {
				db.endTransaction();
				mySQLite.close();
				db = null;
			}
		} catch (Exception e) {
			Gdx.app.debug("xplorateur-Localbase", "Erreur: "+e.getMessage());
		}
	}

}
