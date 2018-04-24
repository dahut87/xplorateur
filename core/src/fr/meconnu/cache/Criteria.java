package fr.meconnu.cache;

import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoine.Particularitetype;

public class Criteria implements Cloneable {
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

}
