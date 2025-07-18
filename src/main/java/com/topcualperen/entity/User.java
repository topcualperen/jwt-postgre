package com.topcualperen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users", schema = "jwttt")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Column(unique = true)
    private String username;

    @Email(message = "Geçerli bir email adresi giriniz")
    @NotBlank(message = "Email boş olamaz")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // Default olarak USER rolü atanıyor

    public User(){}

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
