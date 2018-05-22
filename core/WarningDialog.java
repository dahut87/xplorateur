package fr.evolving.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fr.evolving.assets.AssetLoader;

public class WarningDialog extends Dialog {
	Label thelabel;

	public WarningDialog() {
		super(AssetLoader.language.get("[dialog-window]"), AssetLoader.Skin_ui);
		// TODO Auto-generated constructor stub
		this.getContentTable().add(new ImageButton(AssetLoader.Skin_level, "Warnerbros")).left();
		thelabel = new Label("", AssetLoader.Skin_level);
		this.getContentTable().add(thelabel).right();
		this.setModal(true);
		this.button("Ok");
		this.key(Input.Keys.ENTER, true);
	}

	public void show(String info, Stage stage) {
		this.thelabel.setText(info);
		this.show(stage);
	}
}