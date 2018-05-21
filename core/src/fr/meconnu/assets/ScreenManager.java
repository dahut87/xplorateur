package fr.meconnu.assets;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import fr.meconnu.cache.Patrimoine;
import fr.meconnu.database.Base;
import fr.meconnu.screens.CompassScreen;
import fr.meconnu.screens.MenuScreen;
import fr.meconnu.screens.MapScreen;
import fr.meconnu.screens.PatrimoineScreen;
import fr.meconnu.screens.SearchScreen;

public class ScreenManager {

	static private Screen current,returning;
	static Patrimoine patrimoine=null;

    public enum Screentype {
        MENU(MenuScreen.class),COMPASS(CompassScreen.class),MAP(MapScreen.class),PATRIMOINE(PatrimoineScreen.class),SEARCH(SearchScreen.class);

        private final Class classe;

        public Screen toScreen() {
            try {
            	if (this.classe==PatrimoineScreen.class)
            	{
            		Class[] cArg = new Class[1];
            		cArg[0] = Patrimoine.class;
            		try {
						return (Screen) classe.getDeclaredConstructor(cArg).newInstance(patrimoine);
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
    
    static public void setPatrimoine(Patrimoine apatrimoine) 
    {
    	patrimoine=apatrimoine;
    }

    static public Screen getScreen()
    {
        return current;
    }

    static public void setScreen(Screentype screentype)
    {
        if (screentype==Screentype.PATRIMOINE)
            returning=current;
        else
        	current.dispose();
        current=screentype.toScreen();
        ((Game) Gdx.app.getApplicationListener()).setScreen(current);
    }

    static public void returnScreen()
    {
    	if (returning!=null)
    	{
    		current.dispose();
    		current=returning;
    		returning=null;
    		((Game) Gdx.app.getApplicationListener()).setScreen(current);
    	}
    }
    
    static public void initScreen()
    {
    	current=((Game) Gdx.app.getApplicationListener()).getScreen();
    	returning=null;
    }
    
    

}
