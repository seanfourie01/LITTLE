// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

//YOU ARE NOT REQUIRED TO MODIFY THIS CLASS

public class Driver {
	
	public static void main(String[] args) throws Exception {

		// create a CharStream that reads from standard input
		CharStream input = CharStreams.fromStream(System.in);

		// create a lexer that feeds off of input CharStream
		LittleLexer lexer = new LittleLexer(input);

		// create a buffer of tokens pulled from the lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		// create a parser that feeds off the tokens buffer
		LittleParser parser = new LittleParser(tokens);

		ParseTree tree = parser.program(); // begin parsing at prog rule

		// Create a generic parse tree walker that can trigger callbacks
		ParseTreeWalker walker = new ParseTreeWalker();

		//create a symbol table object stb
		SimpleTableBuilder stb = new SimpleTableBuilder();

		// Walk the tree created during the parse, trigger callbacks
		walker.walk(stb, tree);
		
		// print the symbol table entries
		stb.prettyPrint();

		// Walk with AST builder
		ASTBuilder astBuilder = new ASTBuilder();
		walker.walk(astBuilder, tree);
		
		// Output AST
		ProgramNode astRoot = astBuilder.getRoot();
		printAST(astRoot, 0);

		//YOU ARE NOT REQUIRED TO ADD ANY CODE HERE
	}

	private static void printAST(ASTNode node, int indent) {
		if (node == null) return;
		System.out.println("  ".repeat(indent) + node.toString());
		for (ASTNode child : node.getChildren()) {
			printAST(child, indent + 1);
		}
	}
}
