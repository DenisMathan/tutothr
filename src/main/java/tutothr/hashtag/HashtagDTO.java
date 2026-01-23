package tutothr.hashtag;

import tutothr.common.BaseDTO;

public class HashtagDTO extends BaseDTO {
	private String name;
	
	@Override
	public void initFields() {
		// Kein Formular noetig
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
