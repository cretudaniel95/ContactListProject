package com.onlineshop.business.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;


@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String imageName;

    @Transient
    public String getPhotosImagePath() {
        return "/photos/"+ imageName;
    }
}
