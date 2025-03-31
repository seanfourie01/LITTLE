class VariableSymbol extends Symbol {
    public VariableSymbol(String name, String type) {
        super(name, type);
    }
}

class StringSymbol extends Symbol {
    public StringSymbol(String name, String type, String value) {
        super(name, type, value);
    }
}