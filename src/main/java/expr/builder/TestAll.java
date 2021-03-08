package expr.builder;

import org.junit.Test;

public class TestAll {

	@Test
	public void testClunky() {
		BooleanExpression root = new AndExpression( //
				new OrExpression( //
						new EqualityExpression(new Field("lastName"), new ValueExpression("Jenkins")), //
						new EqualityExpression(new Field("empId"), new ValueExpression("15")), true), //
				new NotNullExpression(new Field("shape")));

		System.out.println(root);
	}

	@Test
	public void testFluent() {
		BooleanExpression root = new ExpressionBuilder() //
				.openScope() //
				.openScope() //
				.equality("empId", "15").or().equality("lastName", "Jenkins") //
				.and() //
				.equality("empId", "16").or().equality("lastName", "Robbins") //
				.closeScope() //
				.and().notNull("shape") //
				.closeScope() //
				.build();
		System.out.println("");
		System.out.println("result: " + root);
	}

}
