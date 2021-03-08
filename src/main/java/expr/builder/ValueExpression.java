package expr.builder;

public class ValueExpression implements Expression {
	
	private final String value;

	public ValueExpression(String value) {
		this.value = value;
	}

	public String toClassInfo() {
		return "ValueExpression [value=" + value + "]";
	}

	@Override
	public String toString() {
		return value;
	}
	
}
