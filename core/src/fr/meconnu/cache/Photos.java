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
	private Actor actor;
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
	
	public void setActor(Actor actor) {
		this.actor=actor;
		for(Photo photo:photos)
			photo.setActor(actor);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) 
	{
		photos.clear();
		Photo photo=new Photo(patrimoine,0);
		photo.setActor(actor);
		photos.add(photo);
		this.patrimoine=patrimoine;
		this.status=PhotosStatusType.NOTHING;
		changed();
		update();
	}
	
	public void changed() {
		if (actor==null) return;
		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
		changeEvent.setBubbles(true);
		changeEvent.setListenerActor(actor);
		changeEvent.setStage(actor.getStage());
		changeEvent.setTarget(actor);
		changeEvent.setCapture(false);
		actor.fire(changeEvent);
		Pools.free(changeEvent);
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
			            		for(int i=1;i<number;i++)
			            		{
			            			if (i<photos.size-1)
			            			{
			            				Photo photo=new Photo(patrimoine,i);
			            				photo.setActor(actor);
			            				photos.add(photo);
			            			}
			            		}
			            		changed();
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
		int max=photos.get(photos.size-1).getIndex();
		for(int i=0;i<max;i++)
		{
			Photo photo=new Photo(this.patrimoine,i);
			if (!photos.contains(photo, false))
				photos.add(photo);
		}
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
	
	static public void setPhotos(Photo photo) {
		AssetLoader.Datahandler.cache().PhotosToCache(photo);
	}

	public void clear() {
		this.photos.clear();		
	}

}
