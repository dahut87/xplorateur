package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine.FieldSizeType;
import fr.meconnu.cache.Patrimoine.FieldType;

public class Sizer extends SelectBox {
	FieldSizeType[] fields = new FieldSizeType[] {FieldSizeType.PETITE, FieldSizeType.MOYENNE, FieldSizeType.GRANDE, FieldSizeType.MAXIMALE};
	String[] items= new String[] {"Faible","Moyen","Important", "Maximal"};

	public Sizer() {
		super(AssetLoader.Skin_images);
		// TODO Auto-generated constructor stub
		for(FieldSizeType type:FieldSizeType.values())
			this.getItems().add(type.toString());
		//this.setItems(items);
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
				int index=0;
				for(FieldSizeType type:FieldSizeType.values())
				{
					if (criteria.getValues()==type)
					{
						this.setSelectedIndex(index);
						break;
					}
					index++;
				}
			}
		}
	}
	
	public FieldSizeType getField() {
		return fields[this.getSelectedIndex()];
	}

}
