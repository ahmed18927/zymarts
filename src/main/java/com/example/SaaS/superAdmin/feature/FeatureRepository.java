package com.example.SaaS.superAdmin.feature;

import com.example.SaaS.common.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature,Long> {

}
