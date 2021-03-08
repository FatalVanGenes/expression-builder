package expr.builder;

public interface BooleanExpression extends Expression {
	// identity
	default void useParens(boolean flag) {
		throw new RuntimeException("not supported");
	}
}
