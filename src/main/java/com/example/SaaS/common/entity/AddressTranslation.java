package com.example.SaaS.common.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "address_translations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AddressTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "lang_code", nullable = false, length = 5)
    private String langCode;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

}
