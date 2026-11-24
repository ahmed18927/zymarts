package com.example.SaaS.common.entity;

import com.example.SaaS.common.converter.JsonMapConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="cities")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;
//    @Column(name ="country_id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "country_id")
//    private Country countryId;


//    private String name;


    @Convert(converter =  JsonMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, String> name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

}
