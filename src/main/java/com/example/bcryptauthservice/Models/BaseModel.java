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
}
