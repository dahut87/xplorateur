package fr.meconnu.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.screens.CompassScreen;

public class CompassRenderer {
	private SpriteBatch batcher;
	private Sprite informations;
	private CompassScreen CompassScreen;
	
	public CompassRenderer(CompassScreen CompassScreen) {
		this.CompassScreen = CompassScreen;
		batcher = new SpriteBatch();
		AssetLoader.viewport.apply();
		informations=new Sprite(AssetLoader.Atlas_images.createSprite("rect"));
		informations.setSize(800.0f,1070f);
		informations.setPosition(255.0f, 12.0f);
	}


	public void render(float delta, float runTime) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batcher.begin();
		batcher.setProjectionMatrix(AssetLoader.Camera.combined);
		informations.draw(batcher);
		batcher.end();
	}
}
