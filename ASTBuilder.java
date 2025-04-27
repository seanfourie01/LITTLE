import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ASTBuilder extends LittleBaseListener {
    public Deque<Expr> exprStack = new ArrayDeque<>();
    public Deque<Stmt> stmtStack = new ArrayDeque<>();
    public int currentRegisterNumber = 0;

    public SimpleTableBuilder stb;

    public ASTBuilder(SimpleTableBuilder stb) {
        this.stb = stb;
    }

    @Override
    public void exitPrimary(LittleParser.PrimaryContext ctx) {
        if (ctx.INTLITERAL() != null) {
            exprStack.push(new LiteralExpr(
                    Integer.parseInt(ctx.INTLITERAL().getText()),
                    DataType.INT,
                    "r"+this.currentRegisterNumber++
            ));
        } else if (ctx.FLOATLITERAL() != null) {
            exprStack.push(new LiteralExpr(
                    Double.parseDouble(ctx.FLOATLITERAL().getText()),
                    DataType.FLOAT,
                    "r"+this.currentRegisterNumber++
            ));
        } else if (ctx.id() != null) {
            String varName = ctx.id().getText();
            switch(this.stb.lookup(varName).type) {
                case "INT":
                    exprStack.push(new IdentifierExpr(varName, DataType.INT, "r"+this.currentRegisterNumber++));
                    break;

                case "FLOAT":
                    exprStack.push(new IdentifierExpr(varName, DataType.FLOAT, "r"+this.currentRegisterNumber++));
                    break;

                case "STRING":
                    exprStack.push(new IdentifierExpr(varName, DataType.STRING, "r"+this.currentRegisterNumber++));
                    break;
            };
        } else {
            // expression already on stack
        }
    }

    @Override
    public void exitCall_expression(LittleParser.Call_expressionContext ctx) {
        List<Expr> args = new ArrayList<>();
        if (ctx.expression_list() != null) {
            int numArgs = ctx.expression_list().expression().size();
            for (int i = 0; i < numArgs; i++) {
                args.add(0, exprStack.pop()); // reverse order
            }
        }
        exprStack.push(new CallExpr(ctx.id().getText(), args));
    }

//    @Override
//    public void exitPostfix_expression(LittleParser.Postfix_expressionContext ctx) {
//        System.out.println("DEBUG: exitPostfix_expression isn't implemented yet");
//    }

    @Override
    public void exitFactor(LittleParser.FactorContext ctx) {
        List<Expr> postfixes = new ArrayList<>();
        for (int i = 0; i < ctx.postfix_expression().size(); i++) {
            postfixes.add(0, exprStack.pop()); // reverse order
        }

        Expr expr = postfixes.get(0);
        for (int i = 1; i < postfixes.size(); i++) {
            String op = ctx.mulop(i - 1).getText();
            expr = new BinaryExpr(expr, op, postfixes.get(i));
        }
        exprStack.push(expr);
    }

    @Override
    public void exitExpression(LittleParser.ExpressionContext ctx) {
        List<Expr> factors = new ArrayList<>();
        for (int i = 0; i < ctx.factor().size(); i++) {
            factors.add(0, exprStack.pop()); // reverse order
        }

        Expr expr = factors.get(0);
        for (int i = 1; i < factors.size(); i++) {
            String op = ctx.addop(i - 1).getText();
            expr = new BinaryExpr(expr, op, factors.get(i));
        }
        exprStack.push(expr);
    }

    @Override
    public void exitAssign_stmt(LittleParser.Assign_stmtContext ctx) {
        Expr value = exprStack.pop();
        String varName = ctx.id().getText();

        switch(this.stb.lookup(varName).type) {
            case "INT":
                stmtStack.push(new AssignStmt(varName, DataType.INT, value));
                break;

            case "FLOAT":
                stmtStack.push(new AssignStmt(varName, DataType.FLOAT, value));
                break;

            case "STRING":
                stmtStack.push(new AssignStmt(varName, DataType.STRING, value));
                break;
        };

    }

    @Override
    public void exitRead_stmt(LittleParser.Read_stmtContext ctx) {
        ArrayList<Symbol> ids = new ArrayList<>();
        for(LittleParser.IdContext idContext : ctx.id()) {
            Symbol sym = this.stb.lookup(idContext.getText());
            if(sym == null) throw new RuntimeException("Attempted to read into a variable that doesn't exist:\n\t" + ctx.getStart().getLine() + ":" + ctx.getStart().getCharPositionInLine() + " - " + ctx.getText());
            ids.add(sym);
        }
        stmtStack.push(new ReadStmt(ids));
    }

    @Override
    public void exitWrite_stmt(LittleParser.Write_stmtContext ctx) {
        ArrayList<Symbol> ids = new ArrayList<>();
        for(LittleParser.IdContext idContext : ctx.id()) {
            Symbol sym = this.stb.lookup(idContext.getText());
            if(sym == null) throw new RuntimeException("Attempted to read into a variable that doesn't exist:\n\t" + ctx.getStart().getLine() + ":" + ctx.getStart().getCharPositionInLine() + " - " + ctx.getText());
            ids.add(sym);
        }
        stmtStack.push(new WriteStmt(ids));
    }

}
