package fr.meconnu.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;

import de.longri.gdx.sqlite.GdxSqlite;
import de.longri.gdx.sqlite.GdxSqliteCursor;
import de.longri.gdx.sqlite.GdxSqlitePreparedStatement;

import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Photo;
import fr.meconnu.cache.Photos;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoine.Patrimoinetype;

public class LocalBase extends Base {
	private static GdxSqlite dbHandler;
	private String databasename = "base.db";
	private String param;

	// Contructeur de la base de donnée

	@Override
	public String getParam() {
		return this.param;
	}

	public LocalBase() {
	}

	public LocalBase(datatype model, String param) {
		super(model, param);
		String[] params = param.split(":");
		this.param = param;
		if (params.length > 1)
			databasename = params[1];
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
			Gdx.app.log("xplorateur-LocalBase", "Reprise de la base '" + databasename
					+ "', table:" + model.toString());
		else {
			Gdx.app.log("xplorateur-LocalBase", "Utilisation de la base '" + databasename
					+ "', table:" + model.toString());
			FileHandle dbFileHandle = Gdx.files.local(databasename);
			dbHandler = new GdxSqlite(dbFileHandle);

			try {
				dbHandler.openOrCreateDatabase();
			} catch (Exception e) {
				
				Gdx.app.error("xplorateur-LocalBase", "Erreur à l'ouverture de la base");
			}
		}
		init(model);
	}
	
	public void init(datatype model) {
		try {
			if (model == datatype.cache) {
				dbHandler.execSQL("CREATE TABLE if not exists caches(id_article INTEGER, ville_nom_reel TEXT, insee INTEGER, titre TEXT, texte TEXT, types TEXT, maj DATETIME DEFAULT CURRENT_TIMESTAMP, localmaj DATETIME DEFAULT CURRENT_TIMESTAMP, coordx REAL, coordy REAL, interet INTEGER, marche INTEGER, time INTEGER, acces INTEGER, difficile INTEGER, risque INTEGER, coeur INTEGER, argent INTEGER, interdit INTEGER, chien STRING, labels TEXT, nom TEXT, id TEXT, mots TEXT, PRIMARY KEY(id));");
				dbHandler.execSQL("CREATE TABLE if not exists photos(id TEXT, aindex INTEGER, photo TEXT, PRIMARY KEY(id, aindex));");
			}
			else if (model == datatype.waypoint) {
				dbHandler.execSQL("CREATE TABLE if not exists waypoints(date DATETIME DEFAULT CURRENT_TIMESTAMP, level INTEGER NOT NULL, user INTEGER NOT NULL, PRIMARY KEY(level,user));");
				} else
				dbHandler.execSQL("CREATE TABLE if not exists patrimoines(date DATETIME DEFAULT CURRENT_TIMESTAMP, desc TEXT NOT NULL, object TEXT, PRIMARY KEY(desc));");
		} catch (Exception e) {
			
		}
	}
	
	// Gestion model type cache
	
	public void writeToCache(Patrimoines patrimoines) {
		for(Patrimoine patrimoine :patrimoines.getValues()) {
			try {
				String request="INSERT OR REPLACE INTO caches (id_article,ville_nom_reel,insee,titre,texte,types,maj,coordx,coordy,interet,marche,time,acces,difficile,risque,coeur,argent,interdit,chien,labels,nom,id,mots) VALUES ("+patrimoine.getId_article_str()+",'"+patrimoine.getVille_nom_reel().replace("'", "''")+"',"+patrimoine.getInsee_str()+",'"+patrimoine.getTitre().replace("'", "''")+"','"+patrimoine.getTexte().replace("'", "''")+"','"+patrimoine.getTypes_str()+"',date('"+patrimoine.getMaj_str()+"'),"+patrimoine.getCoordx_str()+","+patrimoine.getCoordy_str()+","+patrimoine.getInteret_str()+","+patrimoine.getMarche_str()+","+patrimoine.getTime_str()+","+patrimoine.getAcces_str()+","+patrimoine.getDifficile_str()+","+patrimoine.getRisque_str()+","+patrimoine.getCoeur_str()+","+patrimoine.getArgent_str()+","+patrimoine.getInterdit_str()+",'"+patrimoine.getChien().replace("'", "''")+"','"+patrimoine.getLabels().replace("'", "''")+"','"+patrimoine.getNom().replace("'", "''")+"','"+patrimoine.getId()+"','"+patrimoine.getMots().replace("'", "''")+"');";
				Gdx.app.debug("xplorateur-Localbase", "Requête de stockage: "+patrimoine.getId());
				dbHandler.execSQL(request);
			} catch (Exception e) {
				Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage: "+patrimoine.getId());
			}
		}
	}
	
	public int getNumCache() {
		GdxSqliteCursor cursor=null;
			try 
			{
				cursor = dbHandler.rawQuery("select count(id_article) from caches;");
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
			}
			return 0;
	}
	
	public Patrimoines readPatrimoines(Vector2 position, float angle, FieldType field, int limit, boolean desc) {
		String ordering=field.toLabel();
		if (position==null)
			position=new Vector2(0,0);
		if (field==FieldType.PROXIMITE)
			ordering=ordering.replace("%lat%", String.valueOf(position.x)).replace("%lon%", String.valueOf(position.y));
		String ordered="asc";
		if (desc)
			ordered="desc";
		return requestToPatrimoines("coordx<"+String.valueOf(position.x)+"+"+String.valueOf(angle/2)+" and coordx>"+String.valueOf(position.x)+"-"+String.valueOf(angle/2)+" and coordy<"+String.valueOf(position.y)+"+"+String.valueOf(angle/2)+" and coordy>"+String.valueOf(position.y)+"-"+String.valueOf(angle/2)+" order by "+ordering+" "+ordered+" limit "+String.valueOf(limit)+";");
	}
	
	public Array<Criteria> readTitre(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		result.add(new Criteria(FieldType.TITRE,text));
		return result;
	}
	
	public Array<Criteria> readText(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		result.add(new Criteria(FieldType.TEXTE,text));
		return result;
	}
	
	public Array<Criteria> readMotcle(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try 
		{
			cursor = dbHandler.rawQuery("select distinct mots from caches where LOWER(mots) like '%"+text+"%' order by mots asc;");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) 
			{
				result.add(new Criteria(FieldType.MOTCLE,cursor.getString(0)));
				cursor.moveToNext();
			}
				
		}
		catch (Exception e) 
		{
			
		}
		finally 
		{
			if (cursor!=null)
			cursor.close();
		}
		return result;
	}
	
	public Array<Criteria> readCommune(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try 
		{
			cursor = dbHandler.rawQuery("select distinct ville_nom_reel from caches where LOWER(ville_nom_reel) like '%"+text+"%' order by ville_nom_reel asc;");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) 
			{
				result.add(new Criteria(FieldType.COMMUNE,cursor.getString(0)));
				cursor.moveToNext();
			}
				
		}
		catch (Exception e) 
		{
		}
		finally 
		{
			if (cursor!=null)
			cursor.close();
		}
		return result;
	}
	
	public Array<Criteria> readInsee(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try 
		{
			cursor = dbHandler.rawQuery("select distinct ville_nom_reel from caches where insee like '%"+text+"%' order by ville_nom_reel asc;");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) 
			{
				result.add(new Criteria(FieldType.COMMUNE,cursor.getInt(0)));
				cursor.moveToNext();
			}
				
		}
		catch (Exception e) 
		{
			
		}
		finally 
		{
			if (cursor!=null)
			cursor.close();
		}
		return result;
	}
	
	public Array<Criteria> readType(String text) {
		Array<Criteria> result=new Array<Criteria>();
		if (text.equals("")) return result;
		GdxSqliteCursor cursor = null;
		try 
		{
			cursor = dbHandler.rawQuery("select distinct types from caches where LOWER(types) like '%"+text+"%' order by types asc;");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) 
			{
				result.add(new Criteria(FieldType.TYPE,cursor.getString(0)));
				cursor.moveToNext();
			}
				
		}
		catch (Exception e) 
		{
			
		}
		finally 
		{
			if (cursor!=null)
			cursor.close();
		}
		return result;
	}
	
	public String readPatrimoinesUptoDate(Vector2 position, float angle, String date) {
		return requestToString("id","localmaj>"+date+" and coordx<"+String.valueOf(position.x)+"+"+String.valueOf(angle/2)+" and coordx>"+String.valueOf(position.x)+"-"+String.valueOf(angle/2)+" and coordy<"+String.valueOf(position.y)+"+"+String.valueOf(angle/2)+" and coordy>"+String.valueOf(position.y)+"-"+String.valueOf(angle/2)+";");	
	}
	
	public String requestToString(String field,String request) {
		GdxSqliteCursor cursor = null;
		String result="";
		try 
		{
			cursor = dbHandler.rawQuery("select "+field+" from caches where "+request);
			if (cursor==null)
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				result=result+","+cursor.getString(0);
				cursor.moveToNext();
			}
		} 
		catch (Exception e) 
		{
			
		}
		finally
		{
		if (cursor!=null)
			cursor.close();
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
			cursor = dbHandler.rawQuery("select * from caches where "+request);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Patrimoine patrimoine;
				patrimoine=new Patrimoine();
				patrimoine.setId_article(cursor.getInt(0));
				patrimoine.setVille_nom_reel(cursor.getString(1));			
				patrimoine.setInsee(cursor.getInt(2));
				patrimoine.setTitre(cursor.getString(3));
				patrimoine.setTexte(cursor.getString(4));
				patrimoine.setTypes(Patrimoinetype.getPatrimoinetype(cursor.getString(5)));
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
		}
		return patrimoines;
	}
	
	public void PhotosToCache(String id, int index, byte[] photo)
	{
		try {
			String encoded = Base64Coder.encodeLines(photo);
			String request="INSERT OR REPLACE INTO photos (id,aindex,photo) VALUES ('"+id+"',"+index+",'"+encoded+"');";
			Gdx.app.debug("xplorateur-Localbase", "Requête de stockage photo: "+id+","+String.valueOf(index));
			dbHandler.execSQL(request);
		} 
		catch (Exception e) 
		{
			Gdx.app.debug("xplorateur-Localbase", "Erreur de stockage photo: "+id+","+String.valueOf(index)+"..."+e.getMessage());
		}
	}
	
	public Photos PhotosFromCache(Patrimoine patrimoine) {
		Photos photos;
		photos=new Photos(patrimoine);
		photos.clear();
		GdxSqliteCursor cursor = null;
		try {
			cursor = dbHandler.rawQuery("select id,aindex,photo from photos where id='"+patrimoine.getId()+"'");
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String id=cursor.getString(0);
				int index=cursor.getInt(1);
				byte[] bytes = Base64Coder.decodeLines(cursor.getString(2));
				Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
        		Image image = new Image(new Texture(pixmap));
				Photo photo=new Photo(id,index,image.getDrawable());	
				photos.add(photo);
				cursor.moveToNext();
			}
			photos.validate();
		} 
		catch (Exception e) 
		{
			
		}
		finally
		{
		if (cursor!=null)
			cursor.close();
		}
		if (photos.getSize()>0)
			return photos;
		else 
			return new Photos(patrimoine);
	}

	// Gestion model type waypoint

	// Gestion model type patrimoine

	// Commun

	@Override
	public boolean Eraseall() {
		try {
			dbHandler.execSQL("drop table if exists caches;");
			dbHandler.execSQL("drop table if exists waypoints;");
			dbHandler.execSQL("drop table if exists patrimoines;");
			return true;
		} catch (Exception e1) {
			return false;
		}
	}

	@Override
	public void Close() {
		try {
			if (dbHandler != null) {
				dbHandler.closeDatabase();
				dbHandler = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
	}

	@Override
	public String getPrefix() {
		return "local";
	}

	public static boolean isHandling(datatype base) {
		return true;
	}

}
