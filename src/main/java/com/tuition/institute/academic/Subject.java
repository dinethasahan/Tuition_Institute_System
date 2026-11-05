package com.tuition.institute.academic;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 10)
    private String medium = "EN";

    public Subject() {}

    public Subject(String name, String medium) {
        this.name = name;
        this.medium = medium;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMedium() { return medium; }
    public void setMedium(String medium) { this.medium = medium; }
}
