import java.util.*;

public class SimpleTableBuilder extends LittleBaseListener {
	private ScopedSymbolTable currentScope = new ScopedSymbolTable(null, "GLOBAL"); // Global Scope
	private int numBlocks = 1;

	@Override public void enterFunction_declaration(LittleParser.Function_declarationContext ctx) {
		String funcName = ctx.id().getText();
		ScopedSymbolTable scopedTable = new ScopedSymbolTable(currentScope, funcName);
		currentScope.addChild(scopedTable);
		currentScope = scopedTable;


		if(ctx.params() != null) {
			List<LittleParser.Var_typeContext> types = ctx.params().var_type();
			List<LittleParser.IdContext> ids = ctx.params().id();
			for(int i=0 ; i < types.size() ; i++) {
				String type = types.get(i).getText();
				String id = ids.get(i).getText();
				VariableSymbol symbol = new VariableSymbol(id, type);
				currentScope.define(symbol);
			}
		}

	}

	@Override public void exitFunction_declaration(LittleParser.Function_declarationContext ctx) {
		currentScope = currentScope.getParentScope(); // Exit function scope
	}

	@Override public void enterVar_declaration(LittleParser.Var_declarationContext ctx) {
		String varType = ctx.var_type().getText();

		for (LittleParser.IdContext varName : ctx.id()) {
			if (currentScope.lookup(varName.getText()) != null) {
				System.out.println("DECLARATION ERROR " + varName.getText());
				System.exit(1);
			} else {
				currentScope.define(new VariableSymbol(varName.getText(), varType));
			}
		}
	}

	@Override
	public void enterString_declaration(LittleParser.String_declarationContext ctx) {
		String id = ctx.id().getText();
		String type = ctx.STRING().getText();
		String val = ctx.STRINGLITERAL().getText();

		currentScope.define(new StringSymbol(id, type, val));
	}

	private void enterBlock() {
		String scopeName = String.format("BLOCK %d", this.numBlocks++);
		ScopedSymbolTable scope = new ScopedSymbolTable(this.currentScope, scopeName);
		this.currentScope.addChild(scope);
		this.currentScope = scope;
	}

	private void exitBlock() {
		this.currentScope = this.currentScope.getParentScope();
	}

	@Override
	public void enterWhile_stmt(LittleParser.While_stmtContext ctx) {
		this.enterBlock();
	}


	@Override
	public void exitWhile_stmt(LittleParser.While_stmtContext ctx) {
		this.exitBlock();
	}

	@Override
	public void enterIf_stmt(LittleParser.If_stmtContext ctx) {
		this.enterBlock();
	}

	@Override
	public void enterElse_part(LittleParser.Else_partContext ctx) {
		this.exitBlock();
		this.enterBlock();
	}

	@Override
	public void exitIf_stmt(LittleParser.If_stmtContext ctx) {
		this.exitBlock();
	}

	@Override
	public void exitElse_part(LittleParser.Else_partContext ctx) {
		super.exitElse_part(ctx);
	}

	public void prettyPrint(){
		Stack<ScopedSymbolTable> stack = new Stack<>();
		ArrayList<String> outputs = new ArrayList<>();

		ScopedSymbolTable root = currentScope;
		while(root.getParentScope() != null) {
			root = root.getParentScope();
		}

		stack.add(root);

		while(!stack.isEmpty()) {
			ScopedSymbolTable cur = stack.pop();

			ArrayList<ScopedSymbolTable> children = cur.getChildren();
			Collections.reverse(children);
            stack.addAll(children);

			outputs.add(cur.toString());
		}

		System.out.print(String.join("\n\n", outputs));
	}
}
