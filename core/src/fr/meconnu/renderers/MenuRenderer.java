package fr.meconnu.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.screens.MenuScreen;

public class MenuRenderer {
	private SpriteBatch batcher;
	int scrollx;
	int scrolly;
	int dirx;
	int diry;
	private MenuScreen MenuScreen;
	
	public MenuRenderer(MenuScreen MenuScreen) {
		this.MenuScreen = MenuScreen;
		this.scrollx = 0;
		this.scrolly = 0;
		this.dirx = 1;
		this.diry = 1;
		batcher = new SpriteBatch();
		AssetLoader.viewport.apply();
	}

	public void evolve() {
		this.scrollx += dirx;
		this.scrolly += diry;
		if (this.scrollx > 1500)
			this.scrolly += diry;
		if (this.scrollx > 1024)
			this.dirx = -1;
		if (this.scrolly > 768)
			this.diry = -1;
		if (this.scrollx < 0)
			this.dirx = 1;
		if (this.scrolly < 0)
			this.diry = 1;
	}

	public void render(float delta, float runTime) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		batcher.begin();
		batcher.setProjectionMatrix(AssetLoader.Camera.combined);
		batcher.setColor(0.95f, 0.95f, 0.95f, 0.7f);
		batcher.draw(AssetLoader.Texture_fond2, 0, 0, this.scrollx / 2,
				this.scrolly / 2, AssetLoader.width, AssetLoader.height);
		batcher.setColor(0.7f, 0.7f, 0.7f, 1);
		batcher.draw(AssetLoader.Texture_fond, 0, 0, this.scrollx,
				this.scrolly, AssetLoader.width, AssetLoader.height);
		batcher.end();
	}
}

