package fr.meconnu.UI;

import org.oscim.core.MapPosition;
import org.oscim.event.Event;
import org.oscim.event.Gesture;
import org.oscim.event.MotionEvent;
import org.oscim.gdx.GdxMap;
import org.oscim.gdx.GdxMotionEvent;
import org.oscim.map.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.calc.Maths;

import org.oscim.layers.TileGridLayer;
import org.oscim.layers.tile.bitmap.BitmapTileLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Layers;
import org.oscim.map.Map;
import org.oscim.renderer.MapRenderer;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.TileSource;
import org.oscim.tiling.source.OkHttpEngine;
import org.oscim.tiling.source.UrlTileSource;
import org.oscim.tiling.source.bitmap.BitmapTileSource;
import org.oscim.tiling.source.bitmap.DefaultSources;
import org.oscim.tiling.source.oscimap4.OSciMap4TileSource;
import org.oscim.utils.Parameters;
import org.oscim.scalebar.MapScaleBar;
import org.oscim.scalebar.MapScaleBarLayer;
import org.oscim.scalebar.MetricUnitAdapter;
import org.oscim.scalebar.DefaultMapScaleBar;
import org.oscim.scalebar.ImperialUnitAdapter;

public class MyGdxMap extends Actor {

    protected Map mMap;
    protected MapRenderer mMapRenderer;
    protected DefaultMapScaleBar mapScaleBar;
    protected BitmapTileLayer mBitmapLayer;
    protected BitmapTileSource mTileSource;

    public MyGdxMap() {
        mMap = new aMap();
        mMapRenderer = new MapRenderer(mMap);
        mMap.viewport().setViewSize(200, AssetLoader.height);
        mMapRenderer.onSurfaceCreated();
        mMapRenderer.onSurfaceChanged(200, AssetLoader.height);
        mMapRenderer.setBackgroundColor(0x00000000);
        mTileSource = new BitmapTileSource("https://a.tile.openstreetmap.fr/osmfr", "/{Z}/{X}/{Y}.png", 0, 17);
        mBitmapLayer = new BitmapTileLayer(mMap, mTileSource);
        mMap.layers().add(mBitmapLayer);
    	mMap.layers().add(new TileGridLayer(mMap, Color.rgba8888(Color.BLACK),1.8f,10));
    	//mMap.setMapPosition(45.08f, 1.2f, 1 << 15);
        Vector2 position= Filler.getLocalisation();
        mMap.setMapPosition(position.x, position.y, 1 << 15);
        mapScaleBar = new DefaultMapScaleBar(mMap);
        mapScaleBar.setScaleBarMode(DefaultMapScaleBar.ScaleBarMode.BOTH);
        mapScaleBar.setDistanceUnitAdapter(MetricUnitAdapter.INSTANCE);
        mapScaleBar.setSecondaryDistanceUnitAdapter(ImperialUnitAdapter.INSTANCE);
        mapScaleBar.setScaleBarPosition(MapScaleBar.ScaleBarPosition.BOTTOM_LEFT);
        
        MapScaleBarLayer mapScaleBarLayer = new MapScaleBarLayer(mMap, mapScaleBar);
        mMap.layers().add(mapScaleBarLayer);
        addListener(new ActorGestureListener() { 
	       /* @Override
	        public boolean longPress(Actor actor, float x, float y) {
	            // Handle gesture on layers
	        	mMap.handleGesture(Gesture.LONG_PRESS, new GdxMotionEvent(MotionEvent.ACTION_DOWN, x, y));
	            return true;
	        }*/
	
	        @Override
	        public void tap(InputEvent event, float x, float y, int count, int button) {
	            // Handle double tap zoom
	            if (button == Input.Buttons.LEFT) {
	                if (count == 2) {
	                    float pivotX = x - mMap.getWidth() / 2;
	                    float pivotY = y - mMap.getHeight() / 2;
	                    mMap.animator().animateZoom(300, 2, pivotX, pivotY);
	                    mMap.updateMap(true);
	                }
	            }
	
	            // Handle gesture on layers
	            //mMap.handleGesture(Gesture.TAP, new GdxMotionEvent(MotionEvent.ACTION_UP, x, y, button));
	        }
	        @Override
			public void fling (InputEvent event, float VelocityX, float VelocityY, int button) {
				if (button == Input.Buttons.LEFT) {
					mMap.animator().animateFling(VelocityX/4, -VelocityY/4, -5000, 5000, -5000, 5000);
                    mMap.updateMap(true);
				}
	        }
        });
    }
    
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return this;
	}
    
    @Override
	public void draw(Batch batch, float parentAlpha) {
    	render();
    }
    
    public void render() {
    	if (!mRenderRequest)
            return;
    	mMapRenderer.onDrawFrame();
    	//mMapRenderer.animate();
    }
    
    
    public void dispose() {
    	mMap.destroy();
    }

/* private */ boolean mRenderWait;
/* private */ boolean mRenderRequest;
/* private */ boolean mUpdateRequest;
	private int width = Gdx.graphics.getWidth(), height = Gdx.graphics.getHeight(), xOffset, yOffset;

class aMap extends Map implements Map.UpdateListener {

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    private final Runnable mRedrawCb = new Runnable() {
        @Override
        public void run() {
            prepareFrame();
            Gdx.graphics.requestRendering();
        }
    };
    
    public void setMapPosAndSize(float newX, float newY, float newWidth, float newHeight) {
        width = (int) newWidth;
        height = (int) newHeight;

        xOffset = (int) newX;
        yOffset = (int) (Gdx.graphics.getHeight() - newY - newHeight);
    }

    public int getX_Offset() {
        return xOffset;
    }

    public int getY_Offset() {
        return yOffset;
}

    @Override
    public void updateMap(boolean forceRender) {
        synchronized (mRedrawCb) {
            if (!mRenderRequest) {
                mRenderRequest = true;
                Gdx.app.postRunnable(mRedrawCb);
            } else {
                mRenderWait = true;
            }
        }
    }

    @Override
    public void render() {
        synchronized (mRedrawCb) {
            mRenderRequest = true;
            if (mClearMap)
                updateMap(false);
            else {
                Gdx.graphics.requestRendering();
            }
        }
    }

    @Override
    public boolean post(Runnable runnable) {
        Gdx.app.postRunnable(runnable);
        return true;
    }

    @Override
    public boolean postDelayed(final Runnable action, long delay) {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                action.run();
            }
        }, delay / 1000f);
        return true;
    }

    @Override
    public void beginFrame() {
    }

    @Override
    public void doneFrame(boolean animate) {
        synchronized (mRedrawCb) {
            mRenderRequest = false;
            if (animate || mRenderWait) {
                mRenderWait = false;
                updateMap(true);
            }
        }
    }
    
    @Override
    public void onMapEvent(Event e, MapPosition mapPosition) {
        if (e == Map.ANIM_START) {
//            throw new RuntimeException("Use MapView animator instance of map.animator");
            mAnimator.cancel();
        } else if (e == Map.POSITION_EVENT) {
            {// set yOffset at dependency of tilt
                if (mapPosition.getTilt() > 0) {
                    float offset = Maths.linearInterpolation
                            (viewport().getMinTilt(), viewport().getMaxTilt(), 0, 0.8f, mapPosition.getTilt());
                    viewport().setMapViewCenter(offset);
                } else {
                    viewport().setMapViewCenter(0);
                }
            }
        }
        // mostly handled at MapView
}

	@Override
	public int getScreenWidth() {
		// TODO Auto-generated method stub
		return AssetLoader.width;
	}

	@Override
	public int getScreenHeight() {
		// TODO Auto-generated method stub
		return AssetLoader.height;
	}
}
}
