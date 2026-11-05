package com.tuition.institute.user;

import java.util.List;

public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String role; // 'institute', 'teacher', 'student', 'parent'
    private String avatar;
    private String phone;
    private String instituteName;
    private String department;
    private String studentId;
    private String grade;
    private List<String> parentOf;

    public UserDTO() {}

    public UserDTO(String id, String name, String email, String role, String avatar, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatar = avatar;
        this.phone = phone;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getInstituteName() { return instituteName; }
    public void setInstituteName(String instituteName) { this.instituteName = instituteName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public List<String> getParentOf() { return parentOf; }
    public void setParentOf(List<String> parentOf) { this.parentOf = parentOf; }
}
