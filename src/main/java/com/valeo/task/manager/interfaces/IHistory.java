package com.valeo.task.manager.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.valeo.task.manager.models.History;

@JsonDeserialize(as = History.class)
public interface IHistory {
	String getDate();
    void setDate(String date);
    String getText();
    void setText(String text);
}
