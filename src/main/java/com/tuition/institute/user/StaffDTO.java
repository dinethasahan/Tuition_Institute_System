package com.tuition.institute.user;

import java.util.List;

public class StaffDTO {
    private String id;
    private String name;
    private String role;
    private String department;
    private String email;
    private String phone;
    private String status;
    private List<String> permissions;

    public StaffDTO() {}

    public StaffDTO(String id, String name, String role, String department, String email, String phone, String status, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.permissions = permissions;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}
