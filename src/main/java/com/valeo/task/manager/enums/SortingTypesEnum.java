package com.valeo.task.manager.enums;

public enum SortingTypesEnum {
	LOW_HIGH (1, "Low to High"),
	HIGH_LOW (2, "High to Low");
	
	private final Integer id;
	private final String text;
	
	SortingTypesEnum(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public static SortingTypesEnum getById(int id) {
        for (SortingTypesEnum st : SortingTypesEnum.values()) {
            if (st.getId() == id) {
                return st;
            }
        }
        return SortingTypesEnum.HIGH_LOW;
    }
	
	@Override
    public String toString() {
        return this.getText();
    }
}
