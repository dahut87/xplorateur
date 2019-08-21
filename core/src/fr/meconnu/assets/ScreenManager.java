package fr.meconnu.assets;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.screens.CompassScreen;
import fr.meconnu.screens.MenuScreen;
import fr.meconnu.screens.MapScreen;
import fr.meconnu.screens.PatrimoineScreen;
import fr.meconnu.screens.SearchScreen;

public class ScreenManager {

	static private Array<Screen> calls;
	static Object argument=null;

    public enum Screentype {
        MENU(MenuScreen.class),COMPASS(CompassScreen.class),MAP(MapScreen.class),PATRIMOINE(PatrimoineScreen.class),SEARCH(SearchScreen.class);

        private final Class classe;

        public Screen toScreen() {
            try {
            	if (this.classe==PatrimoineScreen.class || this.classe==SearchScreen.class)
            	{
            		Class[] cArg = new Class[1];
            		cArg[0] = Object.class;
            		try {
						return (Screen) classe.getDeclaredConstructor(cArg).newInstance(argument);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	else
                	return (Screen) classe.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
			return null;
        }

        private Screentype(final Class screen) {
            this.classe = screen;
        }
    }
    
    static public void setArgument(Object arg) 
    {
    	argument=arg;
    }

    static public Screen getScreen()
    {
        return calls.peek();
    }

    static public void setScreen(Screentype screentype)
    {
        Gdx.app.debug("xplorateur-screenmanager","SetScreen "+screentype.toString());
    	Screen dest=screentype.toScreen();
    	calls.peek().dispose();
    	calls.clear();
        calls.add(dest);
        ((Game) Gdx.app.getApplicationListener()).setScreen(dest);
    }
    
    static public void callScreen(Screentype screentype)
    {
        Gdx.app.debug("xplorateur-screenmanager","CallScreen "+screentype.toString());
    	calls.add(calls.peek());
    	Screen dest=screentype.toScreen();
        calls.set(calls.size-1, dest);
        ((Game) Gdx.app.getApplicationListener()).setScreen(dest);
    }
    
    static public boolean isCalled() {
    	return (calls.size>1);
    }
    
    static public void returnorsetScreen(Screentype screentype)
    {
    	if (isCalled())
    		returnScreen();
    	else
    		setScreen(screentype);
    }

    static public void returnScreen()
    {
    	calls.pop().dispose();
    	((Game) Gdx.app.getApplicationListener()).setScreen(calls.peek());
    }
    
    static public void initScreen()
    {
        Gdx.app.debug("xplorateur-screenmanager","init");
    	calls=new Array<Screen>();
    	calls.add(((Game) Gdx.app.getApplicationListener()).getScreen());
    }
}
