public class Symbol {
    protected String name;
    protected String type;
    protected String value;

    public Symbol(String name, String type) {
        this.name = name;
        this.type = type;
        this.value = null;
    }

    public Symbol(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAsmDeclaration() {
        switch (this.type) {
            case "INT":
                return String.format("var %s\n", this.name);
            case "FLOAT":
                return String.format("var %s\n", this.name);

            case "STRING":
                return String.format("str %s%s\n", this.name, this.value!=null ? " "+this.value : "");

            default:
                return "";
        }
    }

    @Override
    public String toString() {
        String output = String.format("name %s type %s", this.name, this.type);
        if (this.value != null) {
            output += String.format(" value %s", this.value);
        }

        return output;
    }
}
