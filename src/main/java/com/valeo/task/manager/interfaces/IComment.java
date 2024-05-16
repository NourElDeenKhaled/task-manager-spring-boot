package com.valeo.task.manager.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.valeo.task.manager.models.Comment;

@JsonDeserialize(as = Comment.class)
public interface IComment {
	String getDate();
    void setDate(String date);
    String getText();
    void setText(String text);
}
