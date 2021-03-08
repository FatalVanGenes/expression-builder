package expr.builder;

public class OrExpression implements BooleanExpression {

	private final BooleanExpression lhs;
	private final BooleanExpression rhs;
	private boolean parens;

	public OrExpression(BooleanExpression lhs, BooleanExpression rhs, boolean parens) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.parens = parens;
	}

	public OrExpression(BooleanExpression lhs, BooleanExpression rhs) {
		this(lhs, rhs, false);
	}

	public String toClassInfo() {
		return "OrExpression [lhs=" + lhs + ", rhs=" + rhs + "]";
	}

	public String toString() {
		String expr = lhs + " OR " + rhs;
		return (parens) ? String.format("(%s)", expr) : expr;
	}

	@Override
	public void useParens(boolean flag) {
		this.parens = flag;
	}
}
