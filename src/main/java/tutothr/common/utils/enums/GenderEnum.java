package tutothr.common.utils.enums;

public enum GenderEnum {
	
	
	MALE ("M") , FEMALE("F"), OTHER ("O");
	
	private final String displayValue;
    
    
    private GenderEnum(String displayValue) {
        this.displayValue = displayValue;
    }
    
    public String getDisplayValue() {
        return displayValue;
    }

}
