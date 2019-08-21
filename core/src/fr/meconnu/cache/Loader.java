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
import com.badlogic.gdx.utils.Json;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;

public class Loader {
	private HashMap parameters;
	private HttpRequest httpGet;
	private Json json;
	private String text;
	private boolean accessible;
	
	public Loader(String url) {
		parameters = new HashMap();
		httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl(url);
		json = new Json();
		accessible = false;
	}
	
	public boolean isAccessible()
	{
		return accessible;
	}
	
	public void Request(Vector2 position, Movetype movetype, String except) {
		parameters.clear();
		parameters.put("lat", String.valueOf(position.x));
		parameters.put("long", String.valueOf(position.y));
		parameters.put("type", movetype.toString());
		parameters.put("except", except);
		Gdx.app.debug("xplorateur-Loader", "Lancer la requête http - lat: "+String.valueOf(position.x)+" long: "+String.valueOf(position.y)+" type:"+movetype.toString()+" except:"+except);
		httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
		Gdx.net.sendHttpRequest (httpGet, new HttpResponseListener() {
				public void handleHttpResponse(HttpResponse httpResponse) {
				HttpStatus status = httpResponse.getStatus();
	            if (status.getStatusCode() >= 200 && status.getStatusCode() < 300) {
            		accessible = true;
            		text = httpResponse.getResultAsString();
	            	if (!text.equals("pong")) {
		            	try 
		            	{
	            			json.setElementType(Patrimoines.class, "array", Patrimoine.class);
	            			Patrimoines patrimoines  = json.fromJson(Patrimoines.class, text);
	            			Gdx.app.debug("xplorateur-Loader", "Conversion des objets vers Base de données, nombre: "+String.valueOf(patrimoines.getValues().size));
	            			AssetLoader.Datahandler.cache().writeToCache(patrimoines);
	            		}
	            		catch (Exception E)
	            		{
	            			Gdx.app.debug("xplorateur-Loader", "Erreur lors de la conversion json / stockage dans le cache");
	            		}
	            	}
	             } 
	             else 
	            {
	                	accessible = false;
	                	Gdx.app.debug("xplorateur-Loader", "Erreur avec une réponse du serveur");
	             }
		       }
				
		        public void failed(Throwable t) {
		        	accessible = false;
                	Gdx.app.debug("xplorateur-Loader", "Erreur sans réponse du serveur");
		        }

				@Override
				public void cancelled() {
					accessible = false;
                	Gdx.app.debug("xplorateur-Loader", "Annulation de la requête");
				}
		 });
	}
	
	
	
	

	 
	


}
