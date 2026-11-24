package com.example.SaaS.company.address;


import com.example.SaaS.common.entity.Address;
import com.example.SaaS.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCustomerId(Customer customerId);
}
