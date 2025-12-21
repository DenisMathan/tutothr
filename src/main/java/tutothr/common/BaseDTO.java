package tutothr.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tutothr.common.interfaces.DTOI;
import tutothr.common.models.Field;

public class BaseDTO implements DTOI {
    private Long id;
    private Map<String, String> validationErrors = new HashMap<>();
    protected List<Field> formFields;
    private String submitLabel = "Speichern";
    public BaseDTO() {
        initFields();
    }

    public String getSubmitLabel() {
        return submitLabel;
    }
    public void setSubmitLabel(String submitLabel) {
        this.submitLabel = submitLabel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Map<String, String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }

    public void addValidationError(String field, String message) {
        validationErrors.put(field, message);
    }
    public String getValidationError(String field) {
        return validationErrors.get(field);
    }
    public boolean hasValidationError(String field) {
        return validationErrors.containsKey(field);
    }

	public List<Field> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<Field> formFields) {
		this.formFields = formFields;
	}
    public void initFields() {
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }

}
