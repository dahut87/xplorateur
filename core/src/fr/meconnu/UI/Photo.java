package fr.meconnu.UI;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Filler.Movetype;

public class Photo extends Image{
	private Patrimoine patrimoine;
	private final String url="http://meconnu.fr/getdocument.php";
	private boolean flag;
	private Array<Drawable> photos;
	private int index;
	
	public Photo(Patrimoine patrimoine) {
		super();
		photos=new Array<Drawable>();
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		photos.clear();
		if (Filler.isAccessible())
			photos.add(AssetLoader.Skin_images.getDrawable("gophoto"));
		else
			photos.add(AssetLoader.Skin_images.getDrawable("nophoto"));
		index=0;
		flag=false;
		refresh();
		update();
	}
	
	public void update() {
		if (Filler.isAccessible())
		if (this.patrimoine!=null)
			RequestNb(this.patrimoine.getId());
		RequestPhoto(patrimoine.getId(),index);
	}
	
	public void RequestNb(String id) {
		HashMap parameters;
		HttpRequest httpGet;
		parameters = new HashMap();
		httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl(url);
		parameters.clear();
		parameters.put("id",id);
		parameters.put("index", String.valueOf(-1));
		Gdx.app.debug("xplorateur-Photo", "Lancer la requête http - Num de photo :"+id);
		httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
				public void handleHttpResponse(HttpResponse httpResponse) {
				HttpStatus status = httpResponse.getStatus();
	            if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
            		int number = Integer.parseInt(httpResponse.getResultAsString());
            		for(int i=number;i>1;i--)
            			if (Filler.isAccessible())
            				photos.add(AssetLoader.Skin_images.getDrawable("gophoto"));
            			else
            				photos.add(AssetLoader.Skin_images.getDrawable("nophoto"));
            		flag=true;
	            }
	            else 
	                Gdx.app.debug("xplorateur-Photo", "Erreur avec une réponse du serveur");
		       }
				
		        public void failed(Throwable t) {
                	Gdx.app.debug("xplorateur-Photo", "Erreur sans réponse du serveur");
		        }

				@Override
				public void cancelled() {
                	Gdx.app.debug("xplorateur-Photo", "Annulation de la requête");
				}
		 });
	}
	
	public void refresh()
	{
		this.setDrawable(photos.get(index));
		TextureRegionDrawable textureregion=((TextureRegionDrawable)photos.get(index));
		float ratio=textureregion.getMinWidth()/textureregion.getMinHeight();
		if (ratio>1)
		{
			this.setScaleY(ratio*500f/textureregion.getMinWidth());
			this.setScaleX(1f);
		}
		else
		{
			this.setScaleX(ratio*500f/textureregion.getMinHeight());
			this.setScaleY(1f);
		}
	}
	
	public void RequestPhoto(String id,final int newindex) {
		HashMap parameters;
		HttpRequest httpGet;
		parameters = new HashMap();
		httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setTimeOut(5000);
		httpGet.setUrl(url);
		parameters.clear();
		parameters.put("id",id);
		parameters.put("index", String.valueOf(newindex));
		Gdx.app.debug("xplorateur-Photo", "Lancer la requête http - Photo :"+id);
		httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
				public void handleHttpResponse(HttpResponse httpResponse) {
				HttpStatus status = httpResponse.getStatus();
	            if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
	            	final byte[] rawImageBytes = httpResponse.getResult();
	            		Gdx.app.postRunnable(new Runnable(){
	            	        public void run(){
	            	        	try
	        	            	{
	            	        		Pixmap pixmap = new Pixmap(rawImageBytes, 0, rawImageBytes.length);
	            	        		Image image = new Image(new Texture(pixmap));
	            	        		if (newindex<=photos.size)
	            	        		{
	            	        			photos.set(newindex,image.getDrawable());
	            	        			if (newindex==index)
	            	        				refresh();
	            	        		}
	        	            	}
	            	        	catch (Exception E)
	        	            	{
	        	            		Gdx.app.debug("xplorateur-Photo", "Ceci n'est pas une photo");
	        	            	}
	            	        }
	            	});
	            }
	            else 
	                Gdx.app.debug("xplorateur-Photo", "Erreur avec une réponse du serveur");
		       }
				
		        public void failed(Throwable t) {
                	Gdx.app.debug("xplorateur-Photo", "Erreur sans réponse du serveur");
		        }

				@Override
				public void cancelled() {
                	Gdx.app.debug("xplorateur-Photo", "Annulation de la requête");
				}
		 });
	}
}
