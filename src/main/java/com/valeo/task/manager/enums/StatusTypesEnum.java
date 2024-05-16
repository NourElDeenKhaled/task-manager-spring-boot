package com.valeo.task.manager.enums;

public enum StatusTypesEnum {
	OPEN (1, "Open"),
	IN_PROGRESS (2, "In Progress"),
	CLOSED (3, "Closed");

	private final Integer id;
	private final String text;
	
	StatusTypesEnum(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public static StatusTypesEnum getById(int id) {
        for (StatusTypesEnum st : StatusTypesEnum.values()) {
            if (st.getId() == id) {
                return st;
            }
        }
        return StatusTypesEnum.OPEN;
    }
	
	@Override
    public String toString() {
        return this.getText();
    }
}
