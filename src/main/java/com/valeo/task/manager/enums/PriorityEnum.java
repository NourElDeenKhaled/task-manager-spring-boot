package com.valeo.task.manager.enums;

public enum PriorityEnum {
	LOW (1, "Low"),
	MEDIUM (2, "Medium"),
	HIGH (3, "High");

	private final Integer id;
	private final String text;
	
	PriorityEnum(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public static PriorityEnum getById(int id) {
        for (PriorityEnum p : PriorityEnum.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return PriorityEnum.LOW;
    }
	
	@Override
    public String toString() {
        return this.getText();
    }
}
