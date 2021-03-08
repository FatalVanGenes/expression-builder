package expr.builder;

public class NotNullExpression implements BooleanExpression {

	private final Field lhs;

	public NotNullExpression(Field lhs) {
		this.lhs = lhs;
	}

	public String toClassInfo() {
		return "NotNullExpression [lhs=" + lhs + "]";
	}
	
	@Override
	public String toString() {
		return lhs + " IS NOT NULL";
	}
}
