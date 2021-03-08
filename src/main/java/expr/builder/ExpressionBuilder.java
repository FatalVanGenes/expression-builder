package expr.builder;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBuilder {

	List<Expression> stack = new ArrayList<>();
	private int scopeLevel = 0;
	private boolean itsGood = true;
	private int reduceIteration = 0;

	private static final Expression ANDEXPR = new Expression() {
		@Override
		public String toString() {
			return "ANDEXPR";
		}
	};
	private static final Expression OREXPR = new Expression() {
		@Override
		public String toString() {
			return "OREXPR";
		}
	};
	private static final Expression CLOSESCOPE = new Expression() {
		@Override
		public String toString() {
			return "CLOSESCOPE";
		}
	};
	private static final Expression OPENSCOPE = new Expression() {
		@Override
		public String toString() {
			return "OPENSCOPE";
		}
	};

	private static final List<Class<?>> joinClauses = getJoinClauses();
	private static final List<Class<?>> terminalClauses = getTerminalClauses();

	private void reduce(int startIdx, int endIdx) {
		System.out.printf("%nreduce iteration: %d, start: %d, end: %d%n", reduceIteration, startIdx, endIdx);
		reduceIteration++;
		stack.stream().forEach(System.out::println);
		if (stack.size() == 1) {
			return;
		}
		// scan for first endScope and then work back to find first
		int closeIdx = 0;
		for (; closeIdx < stack.size(); closeIdx++) {
			if (stack.get(closeIdx) == CLOSESCOPE) {
				break;
			}
		}
		if (closeIdx < stack.size() && closeIdx <= endIdx) {
			int openIdx = closeIdx;
			for (; openIdx >= 0; openIdx--) {
				if ((stack.get(openIdx) == OPENSCOPE) && (openIdx > startIdx)) {
					reduce(openIdx, closeIdx);
					break;
				}
			}
			if (openIdx >= 0 && (stack.get(openIdx).getClass() == OPENSCOPE.getClass())
					&& (stack.get(closeIdx).getClass() == CLOSESCOPE.getClass())) {
				stack.remove(closeIdx);
				stack.remove(openIdx);
				return;
			}
			startIdx = Math.max(openIdx, startIdx);
			if (startIdx < 1) {
				startIdx = 1;
			}
			endIdx = Math.min(closeIdx, endIdx);
			// go for a reduction
			for (int x = startIdx; x < endIdx; x++) {
				System.out.println(stack.get(x).getClass() + " between " + stack.get(x - 1).getClass() + " and "
						+ stack.get(x + 1).getClass());
				if ((stack.get(x - 1).getClass() == OPENSCOPE.getClass())
						&& (stack.get(x + 1).getClass() == CLOSESCOPE.getClass())) {
					stack.remove(x + 1);
					stack.remove(x - 1);
					((BooleanExpression)stack.get(x-1)).useParens(true);
					return;
				}
				if (joinClauses.contains(stack.get(x).getClass())
						&& terminalClauses.contains(stack.get(x - 1).getClass())
						&& terminalClauses.contains(stack.get(x + 1).getClass())) {
					System.out.println("hello Bob!");
					if (stack.get(x).getClass() == ANDEXPR.getClass()) {
						int markedForDeath = x - 1;
						BooleanExpression ander = new AndExpression((BooleanExpression) stack.get(x - 1),
								(BooleanExpression) stack.get(x + 1));
						// remove three items
						stack.remove(markedForDeath);
						stack.remove(markedForDeath);
						stack.remove(markedForDeath);
						stack.add(markedForDeath, ander);
						return;
					} else if (stack.get(x).getClass() == OREXPR.getClass()) {
						int markedForDeath = x - 1;
						BooleanExpression orer = new OrExpression((BooleanExpression) stack.get(x - 1),
								(BooleanExpression) stack.get(x + 1));
						// remove three items
						stack.remove(markedForDeath);
						stack.remove(markedForDeath);
						stack.remove(markedForDeath);
						stack.add(markedForDeath, orer);
						return;
					}
				}
			}
		}

	}

	public BooleanExpression build() {
		verifyScopeUsage();
		// last check to see if we are done
		while (stack.size() > 1) {
			reduce(0, stack.size());
		}
		return (BooleanExpression) stack.get(0);
	}

	private boolean verifyScopeUsage() {
		long openCount = stack.stream().filter(OPENSCOPE::equals).count();
		long closeCount = stack.stream().filter(CLOSESCOPE::equals).count();
		if (openCount > 0 || closeCount > 0) {
			if (openCount != closeCount) {
				throw new RuntimeException("imbalanced use of scope(s)");
			}
			scopeLevel = 0;
			itsGood = true;
			stack.forEach(item -> {
				if (item == OPENSCOPE) {
					scopeLevel++;
				} else if (item == CLOSESCOPE) {
					scopeLevel--;
				}
				if (scopeLevel < 0) {
					itsGood = false;
				}
			});
			if (scopeLevel != 0) {
				itsGood = false;
			}
			if (!itsGood) {
				throw new RuntimeException("incorrect use of scope(s)");
			}
			return true; // has any scope usage
		}
		return false; // does not use scope
	}

	public ExpressionBuilder and() {
		stack.add(ANDEXPR);
		return this;
	}

	public ExpressionBuilder or() {
		stack.add(OREXPR);
		return this;
	}

	public ExpressionBuilder openScope() {
		stack.add(OPENSCOPE);
		return this;
	}

	public ExpressionBuilder closeScope() {
		stack.add(CLOSESCOPE);
		return this;
	}

	public ExpressionBuilder equality(String field, String value) {
		stack.add(new EqualityExpression(new Field(field), new ValueExpression(value)));
		return this;
	}

	public ExpressionBuilder notNull(String field) {
		stack.add(new NotNullExpression(new Field(field)));
		return this;
	}

	private static List<Class<?>> getJoinClauses() {
		List<Class<?>> l = new ArrayList<>();
		l.add(ANDEXPR.getClass());
		l.add(OREXPR.getClass());
		return l;
	}

	private static List<Class<?>> getTerminalClauses() {
		List<Class<?>> l = new ArrayList<>();
		l.add(EqualityExpression.class);
		l.add(NotNullExpression.class);
		l.add(AndExpression.class);
		l.add(OrExpression.class);
		return l;
	}

}
