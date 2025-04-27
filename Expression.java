import java.util.ArrayList;
import java.util.List;

enum DataType {
    INT,
    FLOAT,
    STRING,
}

interface Expr {
    List<Expr> getChildren();

    String toString();

    String getResultCode();

    String getResultTemporary();

    DataType getResultType();
}

class BinaryExpr implements Expr {
    Expr left;
    String op;
    Expr right;
    public DataType resultType;
    public String resultTemporary;

    BinaryExpr(Expr left, String op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;

        if (left.getResultType() != right.getResultType())
            throw new RuntimeException("ERROR: expression has different data types for operands");

        this.resultTemporary = right.getResultTemporary();

        this.resultType = left.getResultType();
    }

    @Override
    public List<Expr> getChildren() {
        ArrayList<Expr> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        return children;
    }

    @Override
    public String toString() {
        return "BinaryExpr(" + this.op + ")";
    }

    @Override
    public String getResultCode() {
        String result = "";

        result += this.left.getResultCode();
        result += this.right.getResultCode();
        String opType = "";
        switch (this.left.getResultType()) {
            case INT:
                opType = "i";
                break;
            case FLOAT:
                opType = "r";
                break;
            case STRING: throw new RuntimeException("ERROR: can't perform a mathematical expression with strings");
        };
        String asmOp = "";
        switch (this.op) {
            case "+":
                asmOp = "add";
                break;
            case "-":
                asmOp = "sub";
                break;
            case "*":
                asmOp = "mul";
                break;
            case "/":
                asmOp = "div";
                break;
        };
        result += String.format("%s%s %s %s\n", asmOp, opType, this.right.getResultTemporary(), this.left.getResultTemporary());
        this.resultTemporary = this.left.getResultTemporary();
        return result;
    }

    @Override
    public DataType getResultType() {
        return this.resultType;
    }

    @Override
    public String getResultTemporary() {
        return this.resultTemporary;
    }
}

class LiteralExpr implements Expr {
    Object value;
    DataType type;
    String destRegister;

    LiteralExpr(Object value, DataType type, String destRegister) {
        this.value = value;
        this.type = type;
        this.destRegister = destRegister;
    }

    @Override
    public List<Expr> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Literal( (" + this.type + ") " + this.value + ")";
    }

    @Override
    public String getResultCode() {
        return String.format("move %s %s\n", this.value.toString(), this.destRegister);
    }

    @Override
    public String getResultTemporary() {
        return this.destRegister;
    }

    @Override
    public DataType getResultType() {
        return this.type;
    }
}

class IdentifierExpr implements Expr {
    String name;
    DataType type;
    String resultTemporary;

    IdentifierExpr(String name, DataType type, String resultTemporary) {
        this.name = name;
        this.type = type;
        this.resultTemporary = resultTemporary;
    }

    @Override
    public List<Expr> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Identifier( (" + this.type + ") " + this.name + ")";
    }

    @Override
    public String getResultCode() {
        return String.format("move %s %s\n", this.name, this.resultTemporary);
    }

    @Override
    public String getResultTemporary() {
        return this.resultTemporary;
    }

    @Override
    public DataType getResultType() {
        return this.type;
    }
}

class CallExpr implements Expr {
    String function;
    List<Expr> args;

    CallExpr(String function, List<Expr> args) {
        this.function = function;
        this.args = args;
    }

    @Override
    public List<Expr> getChildren() {
        return this.args;
    }

    @Override
    public String toString() {
        return "Call(" + this.function + ")";
    }

    @Override
    public String getResultCode() {
        return "";
    }

    @Override
    public String getResultTemporary() {
        return "";
    }

    @Override
    public DataType getResultType() {
        return null;
    }

}