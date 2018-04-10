package fr.meconnu.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.assets.AssetLoader;

public class NotationGroup extends Actor{
	Array<Notation> notations;
	ShapeRenderer shaperenderer;
	
	public NotationGroup() {
		CreateNotationGroup(Notationtype.INTERET);
	}
	
	public NotationGroup(Notationtype type) {
		CreateNotationGroup(type);
	}
	
	public void CreateNotationGroup(Notationtype type) {
		notations=new Array<Notation>();
		shaperenderer=new ShapeRenderer();
		shaperenderer.setProjectionMatrix(AssetLoader.Camera.combined);
		this.setSize(250f,250f);
		//this.debug();
		for(byte i=1;i<5;i++) {
			Notation notation=new Notation(type);
			notation.setNote(i);
			notation.setName("unselected");
			notation.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Notation notation=(Notation)event.getListenerActor();
					if (notation.getName().equals("selected"))
						notation.setName("unselected");
					else
						notation.setName("selected");
					ChangeEvent changed=new ChangeEvent();
					fire(changed);
				}
			});
			notations.add(notation);
		}
		sizeChanged();
	}
	
	public String getRequest() {
		String result="";
		for(Notation notation:notations)
		{
			if (notation.getName().equals("selected"))
			{
				if (result.length()>0)
					result=result+" and ";
				result=result+notation.getNotationType().toString()+"="+notation.getNote();
			}
		}
		return result;
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		for(Notation notation:notations)
		{
			Vector2 coords=this.localToStageCoordinates(new Vector2(x,y));
			if (coords.x>=notation.getX() && coords.x<=notation.getRight() && coords.y>=notation.getY() && coords.y<=notation.getTop())
				return notation;
		}
		return null;
	}
	
	
	@Override
	protected void positionChanged () {
		sizeChanged();
	}
	
	@Override
	protected void sizeChanged() {
		float width,height;
		width=this.getWidth();
		height=0.95f*this.getHeight()/4f;
		int i=0;
		for(Notation notation:notations)
		{
			notation.setBounds(this.getX(), this.getY()+i/4f*this.getHeight(), width, height);
			i++;
		}
	}
	
	@Override
	public void act(float delta) {
		for(Notation notation:notations)
			notation.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		for(Notation notation:notations)
		{
			if (notation.getName().equals("selected"))
			{
				batch.end();
				Gdx.gl.glEnable(GL20.GL_BLEND);
				shaperenderer.begin(ShapeType.Filled);
				shaperenderer.setColor(0.75f, 0f, 0f, 0.5f);
				shaperenderer.rect(notation.getX(), notation.getY(), notation.getWidth(), notation.getHeight());
				shaperenderer.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				batch.begin();
				batch.setColor(1f, 1f, 1f, 1f);
			}
			else
				batch.setColor(0.8f, 0.8f, 0.8f, 0.75f);
			notation.draw(batch, parentAlpha);
		}
	}
}
