
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class ScopedSymbolTable {
    private Map<String, Symbol> symbols = new LinkedHashMap<>();
    private String scopeName;
    private ScopedSymbolTable parentScope;
    private ArrayList<ScopedSymbolTable> children;
    public int numBlockChildren;

    public ScopedSymbolTable(ScopedSymbolTable parentScope, String scopeName) {
        this.scopeName = scopeName;
        this.parentScope = parentScope;
        this.children = new ArrayList<ScopedSymbolTable>();
        this.numBlockChildren = 0;
    }

    public String getScopeName() {
        return this.scopeName;
    }

    public int getNumBlockChildren() {
        return this.numBlockChildren;
    }

    public void addChild(ScopedSymbolTable child) {
        this.children.add(child);
        if (Pattern.matches("BLOCK.*", child.scopeName)) {
            this.numBlockChildren ++;
        }
    }

    public ArrayList<ScopedSymbolTable> getChildren() {
        return this.children;
    }

    public void define(Symbol symbol) {
        symbols.put(symbol.getName(), symbol);
    }

    public Symbol lookup(String name) {
        return symbols.get(name);
    }

    public ScopedSymbolTable getParentScope() {
        return parentScope;
    }

    public ArrayList<Symbol> getSymbols() {

        ArrayList<Symbol> mySymbols = new ArrayList<Symbol>(this.symbols.values());

        return mySymbols;
    }

    public String getDeclarations() {
        StringBuilder output = new StringBuilder();

        for(Symbol symbol : this.getSymbols()) {
            output.append(symbol.getAsmDeclaration());
        }

        return output.toString();
    }

    @Override
    public String toString() {
        String output = "";
        output += String.format("Symbol table %s", this.scopeName);

        if (!this.getSymbols().isEmpty()) {
            output += "\n" + this.getSymbols().stream().map(Symbol::toString).collect(Collectors.joining("\n"));
        }

        return output;
    }
}
