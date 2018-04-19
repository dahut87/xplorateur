package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine.FieldSizeType;
import fr.meconnu.cache.Patrimoine.FieldType;

public class Sizer extends SelectBox {
	FieldSizeType[] fields = new FieldSizeType[] {FieldSizeType.PETITE, FieldSizeType.MOYENNE, FieldSizeType.GRANDE, FieldSizeType.MAXIMALE};

	public Sizer() {
		super(AssetLoader.Skin_images);
		// TODO Auto-generated constructor stub
		this.setItems(fields);
		this.setSize(250f, 50f);
	}
	
	public Array<Criteria> getCriterias() {
		Array<Criteria> result=new Array<Criteria>();
		result.add(new Criteria(FieldType.RESULTAT,getField()));
		return result;
	}
	
	public void setCriterias(Array<Criteria> criterias) {
		for(Criteria criteria:criterias)
		{
			if (criteria.getTypes()==FieldType.RESULTAT)
			{
				this.setSelected(criteria.getValues());
				break;
			}
		}
	}
	
	public FieldSizeType getField() {
		return (FieldSizeType)this.getSelected();
	}

}
