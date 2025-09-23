package com.backend.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="NextCommentId")
public class CommentId {

    @Id
    @Column(name="charity")
    private String charity;

    @Column(name="commentId")
    private int id;

    protected CommentId() {}

    public String getCharity() {
        return charity;
    }

    public int getId() {
        return id;
    }
}
