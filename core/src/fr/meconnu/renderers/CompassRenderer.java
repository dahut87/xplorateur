package fr.meconnu.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.screens.CompassScreen;

public class CompassRenderer {
	private SpriteBatch batcher;
	private Sprite informations,jauge;
	private CompassScreen CompassScreen;
	
	public CompassRenderer(CompassScreen CompassScreen) {
		this.CompassScreen = CompassScreen;
		batcher = new SpriteBatch();
		AssetLoader.viewport.apply();
		informations=new Sprite(AssetLoader.Atlas_images.createSprite("rect"));
		informations.setSize(800.0f,1070f);
		informations.setPosition(255.0f, 12.0f);
		jauge=new Sprite(AssetLoader.Atlas_images.createSprite("jauge"));
		jauge.setSize(35f,35f);
		jauge.setPosition(375.0f, 932.0f);
		//jauge.setPosition(1020.0f, 932.0f);
	}


	public void render(float delta, float runTime) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batcher.begin();
		batcher.setProjectionMatrix(AssetLoader.Camera.combined);
		informations.draw(batcher);
		if (Filler.isLocaliser() && Filler.getLocaliser().isLocalisable())
		{
			float accur=1020f-Filler.getLocaliser().getAccuracy()/100f*645f;
			jauge.setX(accur);
			jauge.draw(batcher);
		}
		batcher.end();
	}
}
