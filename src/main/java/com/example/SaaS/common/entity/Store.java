package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="stores")
@Getter  @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")

    private Company company;


    @OneToMany(mappedBy = "storeId", cascade = CascadeType.ALL)
    private List<Category> categories;

    @OneToMany(mappedBy = "storeId")
    private List<Cart> carts;

    @OneToMany(mappedBy = "storeId")
    private List<Order> orders;

    @OneToMany(mappedBy = "storeId")
    private List<Rate> rates;


}
