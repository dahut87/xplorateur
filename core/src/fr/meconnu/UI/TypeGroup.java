package fr.meconnu.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoine.Patrimoinetype;

public class TypeGroup extends Actor{
	Array<ImageTextButton> buttons;
	ShapeRenderer shaperenderer;
	
	public TypeGroup() {
		CreateTypeGroup();
	}
	
	public void CreateTypeGroup() {
		buttons=new Array<ImageTextButton>();
		shaperenderer=new ShapeRenderer();
		shaperenderer.setProjectionMatrix(AssetLoader.Camera.combined);
		this.setSize(610f,250f);
		//this.debug();
		for(Patrimoinetype item:Patrimoinetype.values()) {
			ImageTextButtonStyle style=new ImageTextButton.ImageTextButtonStyle();
			SpriteDrawable sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite(item.toString().replace(" ", "_").replace(",","")));
			sprite.setName(item.toString());
			style.up=sprite;
			style.font=AssetLoader.Skin_images.getFont("DejaVuSans-14");
			ImageTextButton button=new ImageTextButton("", style);
			button.setName("unselected");
			button.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					ImageTextButton button=(ImageTextButton)event.getListenerActor();
					if (button.getName().equals("selected"))
						button.setName("unselected");
					else
						button.setName("selected");
					ChangeEvent changed=new ChangeEvent();
					fire(changed);
				}
			});
			
			buttons.add(button);
		}
		sizeChanged();
	}
	
	public Array<Criteria> getCriterias() {
		Array<Criteria> criterias = new Array<Criteria>();
		for(ImageTextButton button:buttons)
		{
			if (button.getName().equals("selected"))
				criterias.add(new Criteria(FieldType.TYPE,Patrimoinetype.getPatrimoinetype(String.valueOf(button.getStyle().up.toString()))));
		}
		return criterias;
	}
	
	public void setCriterias(Array<Criteria> criterias) {
		for(ImageTextButton button:buttons)
			button.setName("unselected");
		if (criterias!=null)
		for(Criteria criteria:criterias)
		{
			if (criteria.getTypes()==FieldType.TYPE)
			{
				for(ImageTextButton button:buttons)
					if (String.valueOf(button.getStyle().up.toString()).equals(criteria.getValues().toString()))
						button.setName("selected");
			}			
		}
	}
	
	
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		for(ImageTextButton button:buttons)
		{
			Vector2 coords=this.localToStageCoordinates(new Vector2(x,y));
			if (coords.x>=button.getX() && coords.x<=button.getRight() && coords.y>=button.getY() && coords.y<=button.getTop())
				return button;
		}
		return null;
	}
	
	
	@Override
	protected void positionChanged () {
		sizeChanged();
	}
	
	@Override
	protected void sizeChanged() {
		int deltax,deltay;
		float size;
		Array<Vector3> resultat = new Array<Vector3>();
		for(int i=Patrimoinetype.values().length;i>0;i--)
		{
			deltay=i;
			deltax=Patrimoinetype.values().length/deltay;
			if (Patrimoinetype.values().length%deltay>0) deltax++;
			if (this.getWidth()>this.getHeight())
			{
				size=this.getHeight()/deltay;
				if (size*deltax>this.getWidth())
					size=0;
			}
			else
			{
				size=this.getWidth()/deltax;
				if (size*deltay>this.getHeight())
					size=0;
			}

			resultat.add(new Vector3(size,deltax,deltay));
		}
		Vector3 max=new Vector3(0f,0f,0f);
		for(Vector3 item:resultat)
		{
			if (item.x>max.x)
				max=item;
		}
		deltax=(int)max.y;
		deltay=(int)max.z;
		size=max.x;
		int x=0,y=0;
		for(ImageTextButton button:buttons) 
		{
			button.setBounds(this.getX()+x*this.getWidth()/deltax, this.getY()+y*this.getHeight()/deltay,0.95f*size,0.95f*size);
			x++;
			if (x>=deltax)
			{
				x=0;
				y++;
			}
		}
	}
	
	@Override
	public void act(float delta) {
		for(ImageTextButton button:buttons)
			button.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		for(ImageTextButton button:buttons)
		{
			if (button.getName().equals("selected"))
			{
				batch.end();
				Gdx.gl.glEnable(GL20.GL_BLEND);
				shaperenderer.begin(ShapeType.Filled);
				shaperenderer.setColor(0.75f, 0f, 0f, 0.5f);
				shaperenderer.rect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
				shaperenderer.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				batch.begin();
				batch.setColor(1f, 1f, 1f, 1f);
			}
			else
				batch.setColor(0.8f, 0.8f, 0.8f, 0.75f);
			button.draw(batch, parentAlpha);
		}
	}
}
