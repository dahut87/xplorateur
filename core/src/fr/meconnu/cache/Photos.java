package fr.meconnu.cache;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine.FieldType;

public class Photos {
	private HashMap parameters;
	private HttpRequest httpGet;
	private Array<Photo> photos;
	private Patrimoine patrimoine;
	private PhotosStatusType status;
	static private final String url="http://meconnu.fr/getdocument.php";
	
	public enum PhotosStatusType {
		LOADING("Chargement"), OK("Disponible"), NOTHING("Indisponible");
		private final String text;
		
		private PhotosStatusType(final String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public Photos(Patrimoine patrimoine) {
		parameters = new HashMap();
		httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl(url);
		photos=new Array<Photo>();
		setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) 
	{
		photos.clear();
		photos.add(new Photo(patrimoine,0));
		this.patrimoine=patrimoine;
		this.status=PhotosStatusType.NOTHING;
		update();
	}
	
	public void update() {
		if (!Filler.isAccessible() || patrimoine==null) return;
		this.status=PhotosStatusType.LOADING;
		parameters.clear();
		parameters.put("id",patrimoine.getId());
		parameters.put("index", String.valueOf(-1));
		Gdx.app.debug("xplorateur-Photos", "Lancer la requête http - Num de photo :"+patrimoine.getId());
		httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
				public void handleHttpResponse(HttpResponse httpResponse) {
				HttpStatus netstatus = httpResponse.getStatus();
	            if (netstatus.getStatusCode() >= 200 && netstatus.getStatusCode() < 300) {
            		final int number = Integer.parseInt(httpResponse.getResultAsString());
            		Gdx.app.postRunnable(new Runnable(){
            	        public void run(){
            	        	try
        	            	{
            	        		int todo=number-photos.size;
			            		for(int i=0;i<todo;i++)
			            			photos.add(new Photo(patrimoine,photos.size));
        	            	}
            	        	catch (Exception E)
            	        	{
            	        		
            	        	}
            	        }
            		});
	            }
	            else 
	            {
	                Gdx.app.debug("xplorateur-Photo", "Erreur avec une réponse du serveur");
	        		status=PhotosStatusType.NOTHING;
	            }
		       }
				
		        public void failed(Throwable t) {
                	Gdx.app.debug("xplorateur-Photo", "Erreur sans réponse du serveur");
            		status=PhotosStatusType.NOTHING;
		        }

				@Override
				public void cancelled() {
                	Gdx.app.debug("xplorateur-Photo", "Annulation de la requête");
            		status=PhotosStatusType.NOTHING;
				}
		 });
	}

	public void add(Photo photo) {
		photos.add(photo);
		photos.sort();
	}
	
	public void validate()
	{
		if (photos.size==0) return;
		photos.sort();
		int max=0;
		for(Photo photo:photos)
			if (photo.getIndex()>max) max=photo.getIndex();
		for(int i=0;i<max;i++)
		{
			Photo photo=new Photo(this.patrimoine,i);
			if (!photos.contains(photo, false))
				photos.add(photo);
		}
		photos.sort();
	}
	
	public Photo getValue(int index) {
		if (index+1>photos.size) return null;
		return photos.get(index);
	}
	
	public int getSize() {
		return photos.size;
	}
	
	static public Photos getPhotos(Patrimoine patrimoine) {
		return AssetLoader.Datahandler.cache().PhotosFromCache(patrimoine);
	}
	
	static public void setPhotos(String id, int index, byte[] photo) {
		AssetLoader.Datahandler.cache().PhotosToCache(id,index,photo);
	}

	public void clear() {
		this.photos.clear();		
	}

}
