package expr.builder;

public class EqualityExpression implements BooleanExpression {

	private final Field lhs;
	private final Expression rhs;

	public EqualityExpression(Field lhs, Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	public String toClassInfo() {
		return "EqualityExpression [lhs=" + lhs + ", rhs=" + rhs + "]";
	}

	@Override
	public String toString() {
		return lhs + " = " + rhs;
	}

}
