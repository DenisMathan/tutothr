package tutothr.common.models;

public class Field {
    private String name;
    private String label;
    private String type;

    // Konstruktor, Getter, Setter
    public Field(String name, String label, String type) {
        this.name = name;
        this.label = label;
        this.type = type;
    }
    // Getter und Setter ...
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}