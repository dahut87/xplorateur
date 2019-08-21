package fr.meconnu.dialogs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fr.meconnu.assets.AssetLoader;

public class WarningDialog extends Dialog {
	Label thelabel;

	public WarningDialog() {
		super("Attention", AssetLoader.Skin_images);
		// TODO Auto-generated constructor stub
		this.getContentTable().add(new ImageButton(AssetLoader.Skin_images, "attention")).left();
		thelabel = new Label("", AssetLoader.Skin_images,"Informations");
		this.getContentTable().add(thelabel).right();
		this.setModal(true);
		this.button("Valider");
		this.key(Input.Keys.ENTER, true);
	}

	public void show(String info, Stage stage) {
		this.thelabel.setText(info);
		this.show(stage);
	}
}