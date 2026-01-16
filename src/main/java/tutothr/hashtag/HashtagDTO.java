package tutothr.hashtag;

import tutothr.common.BaseDTO;

public class HashtagDTO extends BaseDTO {
	private String name;
	private Long creatorId;
	
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
	
	public Long getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
}
