// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.Deque;
import java.util.stream.Collectors;

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
//		stb.prettyPrint();
//		System.out.println();


		// Walk with AST builder
		ASTBuilder astBuilder = new ASTBuilder(stb);
		walker.walk(astBuilder, tree);

//		while(!astBuilder.stmtStack.isEmpty()) {
//			printStmtAST(astBuilder.stmtStack.pollLast(), 0);
//		}

		String asmCode = generateAsm(stb, astBuilder.stmtStack);
		System.out.println(asmCode);

		//YOU ARE NOT REQUIRED TO ADD ANY CODE HERE
	}

	private static String generateAsm(SimpleTableBuilder stb, Deque<Stmt> stmtStack) {
		String output = "";
		output += stb.getDeclarations();

		while(!stmtStack.isEmpty()) {
			Stmt stmt = stmtStack.pollLast();
			if (stmt instanceof AssignStmt) {
				output += stmt.getResultCode();
			} else if (stmt instanceof ReadStmt) {
				output += stmt.getResultCode();
			} else if (stmt instanceof WriteStmt) {
				output += stmt.getResultCode();
			}
		}

		output += "sys halt\n";

		return output;
	}
}
