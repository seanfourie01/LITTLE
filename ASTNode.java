import java.util.*;

public abstract class ASTNode {
    protected List<ASTNode> children = new ArrayList<>();

    public void addChild(ASTNode child) {
        children.add(child);
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public abstract String toString(); // for pretty printing
}
