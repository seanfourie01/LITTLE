import java.util.ArrayList;
import java.util.List;
import generated.LittleBaseListener;
import generated.LittleParser;

public class ASTBuilder extends LittleBaseListener {
    private ProgramNode root;
    private List<ASTNode> currentStatements = new ArrayList<>();

    @Override
    public void exitProgram(LittleParser.ProgramContext ctx) {
        root = new ProgramNode(currentStatements);
    }

    @Override
    public void exitAssign_stmt(LittleParser.Assign_stmtContext ctx) {
        String varName = ctx.id().getText();
        String exprText = ctx.expression().getText();

        StatementNode assign = new StatementNode("assign " + varName + " := " + exprText);
        currentStatements.add(assign);
    }

    @Override
    public void exitRead_stmt(LittleParser.Read_stmtContext ctx) {
        StringBuilder sb = new StringBuilder("read ");
        for (var id : ctx.id()) {
            sb.append(id.getText()).append(" ");
        }
        currentStatements.add(new StatementNode(sb.toString().trim()));
    }

    @Override
    public void exitWrite_stmt(LittleParser.Write_stmtContext ctx) {
        StringBuilder sb = new StringBuilder("write ");
        for (var id : ctx.id()) {
            sb.append(id.getText()).append(" ");
        }
        currentStatements.add(new StatementNode(sb.toString().trim()));
    }

    public ProgramNode getRoot() {
        return root;
    }
}
