package fr.meconnu.UI;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.cache.Photo;
import fr.meconnu.cache.Photos;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Photo.PhotoStatusType;
import fr.meconnu.calc.Geo;

public class PhotoView extends Actor{
	private Photos photos;
	private int index;
	Drawable image;
	TransformDrawable over,notover;
	boolean overleft,overright;
	
	public PhotoView(Patrimoine patrimoine) {
		super();
		over=(TransformDrawable) AssetLoader.Skin_images.getDrawable("next");
		notover=(TransformDrawable) AssetLoader.Skin_images.getDrawable("next2");
		this.addListener(new InputListener() {
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y)
			{
				if (x<120 && y>50) 
					overleft=true;
				else
					overleft=false;
				if (x>getWidth()-120 && y>50) 
					overright=true;
				else
					overright=false;
				return true;				
			}
			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor)
			{
				overleft=false;
				overright=false;
			}
		});
		this.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event,float x,float y,int pointer,int button)
			{
				if (overleft) previous();
				if (overright) next();
				if (y<50 && x>0 && x<getWidth())
				{
					index=Math.round((x-20)/28);
					if (index<0) index=0;
					if (index>photos.getSize()-1) index=photos.getSize()-1;
					refresh();
				}
				return true;
			}
		});
		setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		photos=Photos.getPhotos(patrimoine);
		first();
		overleft=false;
		overright=false;
	}
	
	public void first()
	{
		index=0;
		refresh();
	}
	
	public void previous()
	{
		if (index>0) 
			index--;
		refresh();
	}
	
	public void next()
	{
		if (index<photos.getSize()-1)		
			index++;
		refresh();
	}
	
	public void refresh()
	{
		if (photos!=null && photos.getValue(index)!=null && photos.getValue(index).getStatus()==PhotoStatusType.NOTHING)
		photos.getValue(index).netupdate();
	}
	
	@Override
	public void act(float delta) {
		if (photos!=null && photos.getValue(index)!=null && photos.getValue(index).getPhoto()!=null)
		image=photos.getValue(index).getPhoto();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		image.draw(batch, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		for(int i=0;i<photos.getSize();i++)
		{
			int size=24;
			int dec=0;
			if (i==index)
			{
				size=32;
				dec=-4;
			}
			AssetLoader.Skin_images.getDrawable("tick-"+photos.getValue(i).getStatus().toString()).draw(batch, this.getX()+20+i*28+dec, this.getY()+20+dec, size, size);
		}
		if (index>0)
		if (overleft)
			over.draw(batch, this.getX()+120, this.getY()+this.getHeight(), 0, 0, 120, this.getHeight()-50, 1, 1, 180);
		else
			notover.draw(batch, this.getX()+120, this.getY()+this.getHeight(), 0, 0, 120, this.getHeight()-50, 1, 1, 180);
		if (index<photos.getSize()-1)
		if (overright)
			over.draw(batch, this.getX()+this.getWidth()-120, this.getY()+50, 0, 0, 120, this.getHeight()-50, 1, 1, 0);
		else
			notover.draw(batch, this.getX()+this.getWidth()-120, this.getY()+50, 0, 0, 120, this.getHeight()-50, 1, 1, 0);
	}
	
	
}
