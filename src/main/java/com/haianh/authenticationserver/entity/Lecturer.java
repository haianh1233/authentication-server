package com.haianh.authenticationserver.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lecturer")
@Data
@NoArgsConstructor
public class Lecturer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "meetingurl")
    private String meetingUrl;

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
    public Lecturer(int id,
                    String email,
                    String name,
                    String phone,
                    String meetingUrl,
                    Integer status,
                    Boolean gender,
                    LocalDate birthday,
                    String address,
                    String avatarUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.meetingUrl = meetingUrl;
        this.status = status;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.avatarUrl = avatarUrl;
    }
}
