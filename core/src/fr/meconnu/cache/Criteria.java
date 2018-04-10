package fr.meconnu.cache;

import fr.meconnu.cache.Patrimoine.Particularitetype;

public class Criteria {
	private String value;
	private Criteriatype type;
	
	public enum Criteriatype {
		Titre("title"),Mot_cle("mots"),Commune("insee"),Type("type"),Texte("text");

		private final String text;
		
		private Criteriatype(final String text) {
			this.text = text;
		}
		
		public static Criteriatype getCriteriatype(String text) {
			for(Criteriatype item :Criteriatype.values())
				if (item.toString().equals(text))
					return item;
			return null;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public Criteria() {
		CreateCriteria(Criteriatype.Titre,"");
	}
	
	public Criteria(Criteriatype type, String text) {
		CreateCriteria(type,text);
	}

	public void CreateCriteria(Criteriatype type,String value)
	{
		this.type=type;
		this.value=value;
	}
	
	public String getRequest() {
		return this.type.toString()+"="+this.value;
	}

	public Criteriatype getTypes() {
		return this.type;
	}
	
	public String getValues() {
		return this.value;
	}

}
