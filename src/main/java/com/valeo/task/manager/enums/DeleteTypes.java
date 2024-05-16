package com.valeo.task.manager.enums;

public enum DeleteTypes {
	Title (1, "Title"),
	ID (2, "ID");

	private final Integer id;
	private final String text;
	
	DeleteTypes(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public static DeleteTypes getById(int id) {
        for (DeleteTypes day : DeleteTypes.values()) {
            if (day.getId() == id) {
                return day;
            }
        }
        return DeleteTypes.ID;
    }
	
	@Override
    public String toString() {
        return this.getText();
    }
}
