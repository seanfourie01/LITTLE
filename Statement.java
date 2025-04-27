import java.util.List;

interface Stmt {
    String toString();
    String getResultCode();
}

class AssignStmt implements Stmt {
    String id;
    DataType type;
    Expr value;

    AssignStmt(String id, DataType type, Expr value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type + " " + id;
    }

    @Override
    public String getResultCode() {
        String output = "";
        if (this.type != DataType.STRING && this.value instanceof LiteralExpr ) {
            output += "move " + ((LiteralExpr) this.value).value.toString() + " " + this.id + "\n";
        } else {
            output += this.value.getResultCode();
            output += "move " + this.value.getResultTemporary() + " " + this.id + "\n";
        }
        return output;
        // a := 2 + 2

    }
}

class ReadStmt implements Stmt {
    public List<Symbol> symbols;
    ReadStmt(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String getResultCode() {
        String output = "";
        for(Symbol symbol : symbols) {
            String type = "";
            switch (symbol.type) {
                case "INT": type = "i";break;
                case "FLOAT": type = "r";break;
                case "STRING": type = "s";break;
            }
            output += String.format("sys read%s %s\n", type, symbol.getName());
        }

        return output;
    }
}

class WriteStmt implements Stmt {
    List<Symbol> symbols;
    WriteStmt(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String getResultCode() {
        String output = "";
        for(Symbol symbol : symbols) {
            String type = "";
            switch (symbol.type) {
                case "INT": type = "i";break;
                case "FLOAT": type = "r";break;
                case "STRING": type = "s";break;
            }
            output += String.format("sys write%s %s\n", type, symbol.getName());
        }

        return output;
    }
}
