package com.valeo.task.manager.enums;

import java.util.function.Predicate;

import com.valeo.task.manager.interfaces.ITask;

public enum SearchTypesEnum {
	TITLE (1, "Title"),
	DUE_DATE (2, "Due date"),
	STATUS (3, "Status"),
	MULTIPLE (4, "Multiple Options");
	
	private final Integer id;
	private final String text;
	
	SearchTypesEnum(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public static SearchTypesEnum getById(int id) {
        for (SearchTypesEnum day : SearchTypesEnum.values()) {
            if (day.getId() == id) {
                return day;
            }
        }
        return SearchTypesEnum.TITLE;
    }
	
	public static Predicate<ITask> getSearchTypePredicate(SearchTypesEnum searchType, String text) {
		switch (searchType) {
		case TITLE:
			return t -> t.getTitle().trim().toLowerCase().contains(text);
		case DUE_DATE:
			return t -> t.getDueDate().trim().contains(text);
		case STATUS:
			return t -> t.getStatus().getText().trim().toLowerCase().contains(text);
		default:
			return null;
		}
	}
	
	@Override
    public String toString() {
        return this.getText();
    }
}
