package fr.meconnu.cache;

import com.badlogic.gdx.utils.Array;

import fr.meconnu.cache.Patrimoine.FieldSizeType;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoine.Particularitetype;

public class Criteria implements Cloneable,Comparable {
	private Object value;
	private FieldType type;
	
	public Criteria() {
		CreateCriteria(FieldType.TITRE,"");
	}
	
	public Criteria(FieldType type, Object value) {
		CreateCriteria(type,value);
	}

	public void CreateCriteria(FieldType type,Object value)
	{
		this.type=type;
		this.value=value;
	}
	
	public Criteria clone() {
		Criteria criteria;
		criteria=new Criteria();
		criteria.type=this.type;
		criteria.value=this.value;
		return criteria;
	}
	
	public String getRequest() {
		return this.type.toString()+"="+this.value;
	}

	public FieldType getTypes() {
		return this.type;
	}
	
	public Object getValues() {
		return this.value;
	}
	
	static public FieldType getOrder(Array<Criteria> criterias) {
		for(Criteria criteria:criterias)
			if (criteria.getTypes()==FieldType.ORDRE)
				return (FieldType)criteria.getValues();
		return FieldType.PROXIMITE;		
	}
	
	static public void setOrder(Array<Criteria> criterias,FieldType fieldtype) {
		for(Criteria criteria:criterias)
			if (criteria.getTypes()==FieldType.ORDRE)
				criterias.removeValue(criteria,true);
			criterias.add(new Criteria(FieldType.ORDRE,fieldtype));	
	}
	
	static public FieldSizeType getResultSize(Array<Criteria> criterias) {
		for(Criteria criteria:criterias)
			if (criteria.getTypes()==FieldType.RESULTAT)
				return (FieldSizeType)criteria.getValues();
		return FieldSizeType.PETITE;		
	}
	
	static public void setResultSize(Array<Criteria> criterias,FieldSizeType fieldSizeType) {
		for(Criteria criteria:criterias)
			if (criteria.getTypes()==FieldType.RESULTAT)
				criterias.removeValue(criteria,true);
			criterias.add(new Criteria(FieldType.RESULTAT,fieldSizeType));	
	}
	
	static public void Filter(Array<Criteria> criterias,FieldType arg)
	{
		for(Criteria criteria:criterias)
			if (criteria.getTypes()!=arg)
				criterias.removeValue(criteria, true);
	}

	@Override
	public int compareTo(Object arg0) {
		return this.getTypes().ordinal()-((Criteria) arg0).getTypes().ordinal();
	}

}
