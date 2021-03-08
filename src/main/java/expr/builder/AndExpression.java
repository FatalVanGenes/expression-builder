package expr.builder;

public class AndExpression implements BooleanExpression {

	private final BooleanExpression lhs;
	private final BooleanExpression rhs;
	private boolean parens;

	public AndExpression(BooleanExpression lhs, BooleanExpression rhs, boolean parens) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.parens = parens;
	}
	
	public AndExpression(BooleanExpression lhs, BooleanExpression rhs) {
		this(lhs, rhs, false);
	}

	public String toClassInfo() {
		return "AndExpression [lhs=" + lhs + ", rhs=" + rhs + "]";
	}

	@Override
	public void useParens(boolean flag) {
		this.parens = flag;
	}

	@Override
	public String toString() {
		String expr = lhs + " AND " + rhs;
		return (parens) ? String.format("(%s)", expr) : expr;
	}
}
