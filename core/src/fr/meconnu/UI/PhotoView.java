package fr.meconnu.UI;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
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
	Drawable image,background;
	TransformDrawable over,notover,photobd;
	boolean overleft,overright;
	
	public PhotoView(Patrimoine patrimoine) {
		super();
		over=(TransformDrawable) AssetLoader.Skin_images.getDrawable("next");
		notover=(TransformDrawable) AssetLoader.Skin_images.getDrawable("next2");
		background=(TransformDrawable) AssetLoader.Skin_images.getDrawable("black");
		photobd=(TransformDrawable) AssetLoader.Skin_images.getDrawable("bdphoto");		
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
		setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		final Patrimoine Argument=patrimoine;
		Gdx.app.postRunnable(new Runnable(){
	        public void run(){
	        	photos=Photos.getPhotos(Argument);
	        }
		});
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
		if (photos==null) return;
		if (index>0) 
			index--;
		refresh();
	}
	
	public void next()
	{
		if (photos==null) return;
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
		if (photos==null)
		{
			photobd.draw(batch, 0, 0, this.getWidth(), this.getHeight());
			return;
		}
		TextureRegionDrawable textureregion=((TextureRegionDrawable)image);
		if (textureregion.getRegion().getTexture().getTextureData().getFormat()==Pixmap.Format.Alpha)
			textureregion.tint(Color.RED);
		else
			background.draw(batch, 0, 0, this.getWidth(), this.getHeight());
		float ratio=textureregion.getMinWidth()/textureregion.getMinHeight();
		if (ratio>1)
		{
			float width=this.getWidth();
			float height=width/ratio;
			if (height>this.getHeight()) height=this.getHeight();
			float x=0;
			float y=(this.getHeight()-height)/2;
			if (y<0) y=0;
			image.draw(batch, x, y, width, height);
		}
		else
		{
			float height=this.getHeight();
			float width=height*ratio;
			if (width>this.getWidth()) height=this.getWidth();
			float x=(this.getWidth()-width)/2;
			if (x<0) x=0;
			float y=0;
			image.draw(batch, x, y, width, height);
		}
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

	public void setOverright() {
		this.overright=true;
	}

	public void setOverleft() {
		this.overleft=true;
	}

	public boolean getOverright() {
		return overright;
	}

	public boolean getOverleft() {
		return overleft;
	}

	public int getSize() {
		if (photos!=null)
			return photos.getSize();
		else
			return 0;
	}

	public void goTo(int aindex)
	{
		if (photos==null) return;
		if (aindex < 0) aindex = 0;
		if (aindex > photos.getSize() - 1) aindex = photos.getSize() - 1;
		index=aindex;
		refresh();
	}

}
