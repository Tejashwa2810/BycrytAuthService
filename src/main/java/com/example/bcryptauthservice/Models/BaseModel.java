package com.example.bcryptauthservice.Models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date created_at;
    private Date updated_at;
    private Date last_updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getLast_updated_at() {
        return last_updated_at;
    }

    public void setLast_updated_at(Date last_updated_at) {
        this.last_updated_at = last_updated_at;
    }
}
