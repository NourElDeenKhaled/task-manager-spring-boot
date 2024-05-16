package com.valeo.task.manager.enums;

import java.util.function.Predicate;

import com.valeo.task.manager.interfaces.ITask;

public enum CategoryEnum {
	WORK (1, "Work"),
	PERSONAL (2, "Personal"),
	SCHOOL (3, "School");

	private final Integer id;
	private final String text;
	
	CategoryEnum(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}
	
	public static CategoryEnum getById(int id) {
        for (CategoryEnum p : CategoryEnum.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return CategoryEnum.PERSONAL;
    }
	
	public static Predicate<ITask> getCategoryFilterPredicate(CategoryEnum searchType) {
		switch (searchType) {
		case WORK:
			return t -> t.getCategory().equals(WORK);
		case PERSONAL:
			return t -> t.getCategory().equals(PERSONAL);
		case SCHOOL:
			return t -> t.getCategory().equals(SCHOOL);
		default:
			return null;
		}
	}
	
	@Override
    public String toString() {
        return this.getText();
    }
}
