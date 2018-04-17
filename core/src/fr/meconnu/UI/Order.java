package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine.FieldSizeType;
import fr.meconnu.cache.Patrimoine.FieldType;

public class Order extends SelectBox {
	FieldType[] fields = new FieldType[] {FieldType.TITRE, FieldType.PROXIMITE, FieldType.INTERET, FieldType.DUREE, FieldType.TYPE};
	String[] items = new String[]{"Titre", "Proximité", "Intérêt", "Durée visite","Type"};

	public Order() {
		super(AssetLoader.Skin_images);
		// TODO Auto-generated constructor stub
		this.setItems(items);
		this.setSize(250f, 50f);
	}
	
	public FieldType getField() {
		return fields[this.getSelectedIndex()];
	}
	
	public Array<Criteria> getCriterias() {
		Array<Criteria> result=new Array<Criteria>();
		result.add(new Criteria(FieldType.ORDRE,getField()));
		return result;
	}
	
	public void setCriterias(Array<Criteria> criterias) {
		for(Criteria criteria:criterias)
		{
			if (criteria.getTypes()==FieldType.ORDRE)
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

}
