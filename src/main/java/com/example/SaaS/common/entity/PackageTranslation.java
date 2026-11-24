package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
name = "package_translations" ,
uniqueConstraints = {
    @UniqueConstraint(columnNames = {"package_id" ,"locale"})
}
)
@Builder
public class PackageTranslation {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     @Column(nullable = false )
     private  String name;
     private   String locale;

     private String description;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name="package_id")
     private  Package pkg;

}
