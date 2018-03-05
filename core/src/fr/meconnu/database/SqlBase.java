package fr.meconnu.database;

public class SqlBase extends Base {

	public SqlBase(datatype model, String param) {
		super(model, param);
	}

	public SqlBase() {
	}

	@Override
	public String getPrefix() {
		return "mysql";
	}

	public static boolean isHandling(datatype base) {
		if (base == datatype.patrimoine)
			return false;
		else
			return true;
	}
}