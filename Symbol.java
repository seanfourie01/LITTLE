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

    @Override
    public String toString() {
        String output = String.format("name %s type %s", this.name, this.type);
        if (this.value != null) {
            output += String.format(" value %s", this.value);
        }

        return output;
    }
}
