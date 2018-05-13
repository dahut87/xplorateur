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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Photos;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Photo.PhotoStatusType;

public class PhotoView extends Actor{
	private Photos photos;
	private int index;
	private int max;
	
	public PhotoView(Patrimoine patrimoine) {
		super();
		ticks=new Array<ImageButton>();
		photos=Photos.getPhotos(patrimoine);
		photos.setActor(this);
		first();
		this.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	if (event.getBubbles()==false)
		    		refresh_pic();
		    	else
		    		refresh_ticks();
		    }
		});
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.photos.setPatrimoine(patrimoine);
		first();
	}
	
	public void first()
	{
		index=0;
	}
	
	public void previous()
	{
		if (index>0) 
		{
			index--;
			refresh_pic();
		}
	}
	
	public void next()
	{
		if (index<max-1)		
		{
			index++;
			refresh_pic();
		}
	}
	
	public void load()
	{
		if (photos.getValue(index).getStatus()==PhotoStatusType.NOTHING)
			photos.getValue(index).netupdate();
	}
	
	public void refresh_pic()
	{
		load();
		Drawable image=photos.getValue(index).getPhoto();
		this.setDrawable(image);
	}
	
	
	
}
