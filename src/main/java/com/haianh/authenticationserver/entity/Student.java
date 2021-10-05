package com.haianh.authenticationserver.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "mssv")
    private String mssv;

    @Column(name = "majorid")
    private String majorId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private Integer status;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "address")
    private String address;

    @Column(name = "avatarurl")
    private String avatarUrl;

    @Builder
    public Student(Integer id,
                   String email,
                   String mssv,
                   String majorId,
                   String name,
                   String phone,
                   Integer status,
                   Boolean gender,
                   LocalDate birthday,
                   String address,
                   String avatarUrl) {
        this.id = id;
        this.email = email;
        this.mssv = mssv;
        this.majorId = majorId;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.avatarUrl = avatarUrl;
    }
}
