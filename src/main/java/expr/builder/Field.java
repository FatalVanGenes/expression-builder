package expr.builder;

public class Field implements Expression {

	private final String name;

	public Field(String name) {
		this.name = name;
	}

	public String toClassInfo() {
		return "Field [name=" + name + "]";
	}

	@Override
	public String toString() {
		return name;
	}

}
