package fr.meconnu.cache;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;

public class Photo implements Comparable{
		private HashMap parameters;
		private HttpRequest httpGet;
		private Drawable photo;
		private PhotoStatusType status;
		private int index;
		private String id;
		private Actor actor;
		static private final String url="http://meconnu.fr/getdocument.php";
		
		public enum PhotoStatusType {
			LOADING("go"), OK("ok"), NOTHING("no");
			private final String text;
			
			private PhotoStatusType(final String text) {
				this.text = text;
			}
			
			@Override
			public String toString() {
				return text;
			}
		}
		
	public void setActor(Actor actor) {
		this.actor=actor;
	}
		
	public void changed() {
		if (actor==null) return;
		ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
		changeEvent.setBubbles(false);
		changeEvent.setListenerActor(actor);
		changeEvent.setStage(actor.getStage());
		changeEvent.setTarget(actor);
		changeEvent.setCapture(false);
		actor.fire(changeEvent);
		Pools.free(changeEvent);
	}
	
	public Photo() {
		createphoto("",0);
	}
	
	public Photo(Patrimoine patrimoine) {
		createphoto(patrimoine.getId(),0);
	}
	
	public Photo(Patrimoine patrimoine, int index) {
		createphoto(patrimoine.getId(),index);
	}
	
	public Photo(String id, int index) {
		createphoto(id,index);
	}
		
	public void createphoto(String id,int index) {
		this.id=id;
		this.index=index;
		status=PhotoStatusType.NOTHING;
		httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setTimeOut(5000);
		httpGet.setUrl(url);
		parameters = new HashMap();
		if (Filler.isAccessible())
			photo=AssetLoader.Skin_images.getDrawable("gophoto");
		else
			photo=AssetLoader.Skin_images.getDrawable("nophoto");
	}
	
	public void netupdate() {
		if (!Filler.isAccessible() || id=="" ) return;
		status=PhotoStatusType.LOADING;
		changed();
		parameters.clear();
		parameters.put("id",id);
		parameters.put("index", String.valueOf(index));
		Gdx.app.debug("xplorateur-Photo", "Lancer la requête http - Photo :"+id+","+String.valueOf(index));
		httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
				public void handleHttpResponse(HttpResponse httpResponse) {
				HttpStatus netstatus = httpResponse.getStatus();
	            if (netstatus.getStatusCode() >= 200 && netstatus.getStatusCode() < 300) {
	            	final byte[] rawImageBytes = httpResponse.getResult();
	            		Gdx.app.postRunnable(new Runnable(){
	            	        public void run(){
	            	        	try
	        	            	{
	            	        		Pixmap pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
	            	        		Image image = new Image(new Texture(pixmap));
	            	        		photo = image.getDrawable();
	        	            	}
	            	        	catch (Exception E)
	        	            	{
	        	            		Gdx.app.debug("xplorateur-Photo", "Ceci n'est pas une photo");
	        	            	}
            	        		changed();
	            	        }
	            	});
	            }
	            else 
	            {
	                Gdx.app.debug("xplorateur-Photo", "Erreur avec une réponse du serveur");
	        		status=PhotoStatusType.NOTHING;
	        		changed();
	            }
		       }
				
		        public void failed(Throwable t) {
                	Gdx.app.debug("xplorateur-Photo", "Erreur sans réponse du serveur");
                	status=PhotoStatusType.NOTHING;
	        		changed();
		        }

				@Override
				public void cancelled() {
                	Gdx.app.debug("xplorateur-Photo", "Annulation de la requête");
                	status=PhotoStatusType.NOTHING;
	        		changed();
				}
		 });
	}
	
	public void setFromPatrimoine(Patrimoine patrimoine)
	{
		this.id=patrimoine.getId();
	}
	
	public void setFromPatrimoine(Patrimoine patrimoine, int index)
	{
		this.id=patrimoine.getId();
		this.index=index;
	}


	public void setId(String id) {
		this.id=id;
	}
	
	public String getId() {
		return String.valueOf(id);
	}
	
	public void setIndex(int index) {
		this.index=index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public Drawable getPhoto() {
		return this.photo;
	}
	
	public PhotoStatusType getStatus() {
		return status;
	}

	public void setPhoto(byte[] blob) {
		Pixmap pixmap = new Pixmap(blob, 0, blob.length);
		Image image = new Image(new Texture(pixmap));
		this.photo = image.getDrawable();
	}
	
	@Override
	public int compareTo(Object arg0) {
		return this.index-((Photo) arg0).getIndex();
	}
	
	@Override
	public boolean equals(Object other){
		if (other!=null)
			return ((((Photo)other).getId().equals(this.getId())) && ((Photo)other).getIndex() == this.getIndex());
		else
			return false;
	}
}
