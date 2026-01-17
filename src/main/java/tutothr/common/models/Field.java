package tutothr.common.models;

import java.util.List;
import java.util.ArrayList;

public class Field {
    private String name;
    private String label;
    private String type;
    private List<Field> subFields = new ArrayList<>();

    // Konstruktor, Getter, Setter
    public Field(String name, String label, String type) {
        this.name = name;
        this.label = label;
        this.type = type;
    }

    public Field(String name, String label, String type, List<Field> subFields) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.subFields = subFields;
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
    public List<Field> getSubFields() {
        return subFields;
    }
    public void setSubFields(List<Field> subFields) {
        this.subFields = subFields;
    }
}
