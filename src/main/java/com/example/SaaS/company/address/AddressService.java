//package com.example.SaaS.company.address;
//
//import com.example.SaaS.common.entity.*;
//import com.example.SaaS.common.exception.BusinessException;
//import com.example.SaaS.company.address.DTOS.AddressRequest;
//import com.example.SaaS.company.customer.CustomerRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AddressService {
//
//    private final AddressRepository addressRepository;
//    private final AddressTranslationRepository translationRepository;
//    private final CustomerRepository customerRepository;
//
//    @Transactional
//    public Address create(AddressRequest request){
//        Customer customer = customerRepository.findById(request.getCustomerId())
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        City city = new City();
//        city.setId(request.getCityId());
//
//        Country country = new Country();
//        country.setId(request.getCountryId());
//
//        Address address = Address.builder()
//                .customerId(customer)
//                .address(request.getAddressEn())
//                .cityId(city)
//                .countryId(country)
//                .isDefault(request.getIsDefault())
//                .isActive(request.getIsActive())
//                .note(request.getNoteEn())
//                .build();
//        address = addressRepository.save(address);
//
//        AddressTranslation translationAr = AddressTranslation.builder()
//                .address(address)
//                .langCode("ar")
//                .addressLine(request.getAddressAr())
//                .note(request.getNoteAr())
//                .build();
//        translationRepository.save(translationAr);
//
//        AddressTranslation translationEn = AddressTranslation.builder()
//                .address(address)
//                .langCode("en")
//                .addressLine(request.getAddressEn())
//                .note(request.getNoteEn())
//                .build();
//        translationRepository.save(translationEn);
//        return address;
//
//    }
//
//    @Transactional
//    public Address update(Long id, AddressRequest request) {
//        Address address = addressRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Address not found"));
//
//        address.setAddress(request.getAddressEn());
//
//        City city = new City();
//        city.setId(request.getCityId());
//        address.setCityId(city);
//
//        Country country = new Country();
//        country.setId(request.getCountryId());
//        address.setCountryId(country);
//
//        address.setIsDefault(request.getIsDefault());
//        address.setIsActive(request.getIsActive());
//        address.setNote(request.getNoteEn());
//
//        AddressTranslation translationAr = translationRepository
//                .findByAddressAndLangCode(address, "ar")
//                .orElse(AddressTranslation.builder()
//                        .address(address)
//                        .langCode("ar")
//                        .build());
//        translationAr.setAddressLine(request.getAddressAr());
//        translationAr.setNote(request.getNoteAr());
//        translationRepository.save(translationAr);
//
//        AddressTranslation translationEn = translationRepository
//                .findByAddressAndLangCode(address, "en")
//                .orElse(AddressTranslation.builder()
//                        .address(address)
//                        .langCode("en")
//                        .build());
//        translationEn.setAddressLine(request.getAddressEn());
//        translationEn.setNote(request.getNoteEn());
//        translationRepository.save(translationEn);
//
//        return addressRepository.save(address);
//    }
//
//    public Address getById(Long id) {
//        return addressRepository.findById(id)
//                .orElseThrow(() -> new BusinessException("Address not found"));
//    }
//
//    public List<Address> getAll() {
//        return addressRepository.findAll();
//    }
//
//    public List<Address> getByCustomerId(Long customerId) {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new BusinessException("Customer not found"));
//        return addressRepository.findByCustomerId(customer);
//    }
//
//    @Transactional
//    public void delete(Long id) {
//        Address address = getById(id);
//        addressRepository.delete(address);
//    }
//
//    public String getTranslatedAddress(Address address, String lang) {
//        return translationRepository.findByAddressAndLangCode(address, lang)
//                .map(AddressTranslation::getAddressLine)
//                .orElse(translationRepository.findByAddressAndLangCode(address, "en")
//                        .map(AddressTranslation::getAddressLine)
//                        .orElse(address.getAddress()));
//    }
//
//    public String getTranslatedNote(Address address, String lang) {
//        return translationRepository.findByAddressAndLangCode(address, lang)
//                .map(AddressTranslation::getNote)
//                .orElse(translationRepository.findByAddressAndLangCode(address, "en")
//                        .map(AddressTranslation::getNote)
//                        .orElse(address.getNote()));
//    }
//}
package com.example.SaaS.company.address;

import com.example.SaaS.common.entity.*;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.company.address.DTOS.AddressRequest;
import com.example.SaaS.company.customer.CustomerRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressTranslationRepository translationRepository;
    private final CustomerRepository customerRepository;
    private final Validator validator; // ⬅️ أضف ده

    @Transactional
    public Address create(AddressRequest request) {

        // ✅ Validate Request
        validateRequest(request);

        log.info("📥 Creating address for customer: {}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("Customer not found"));

        City city = new City();
        city.setId(request.getCityId());

        Country country = new Country();
        country.setId(request.getCountryId());

        Address address = Address.builder()
                .customerId(customer)
                .address(request.getAddressEn())
                .cityId(city)
                .countryId(country)
                .isDefault(request.getIsDefault())
                .isActive(request.getIsActive())
                .note(request.getNoteEn())
                .build();
        address = addressRepository.save(address);

        // Save Arabic Translation
        AddressTranslation translationAr = AddressTranslation.builder()
                .address(address)
                .langCode("ar")
                .addressLine(request.getAddressAr())
                .note(request.getNoteAr())
                .build();
        translationRepository.save(translationAr);

        // Save English Translation
        AddressTranslation translationEn = AddressTranslation.builder()
                .address(address)
                .langCode("en")
                .addressLine(request.getAddressEn())
                .note(request.getNoteEn())
                .build();
        translationRepository.save(translationEn);

        log.info("✅ Address created with ID: {}", address.getId());

        return address;
    }

    @Transactional
    public Address update(Long id, AddressRequest request) {

        // ✅ Validate Request
        validateRequest(request);

        log.info("🔄 Updating address ID: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Address not found"));

        address.setAddress(request.getAddressEn());

        City city = new City();
        city.setId(request.getCityId());
        address.setCityId(city);

        Country country = new Country();
        country.setId(request.getCountryId());
        address.setCountryId(country);

        address.setIsDefault(request.getIsDefault());
        address.setIsActive(request.getIsActive());
        address.setNote(request.getNoteEn());

        // Update Arabic Translation
        AddressTranslation translationAr = translationRepository
                .findByAddressAndLangCode(address, "ar")
                .orElse(AddressTranslation.builder()
                        .address(address)
                        .langCode("ar")
                        .build());
        translationAr.setAddressLine(request.getAddressAr());
        translationAr.setNote(request.getNoteAr());
        translationRepository.save(translationAr);

        // Update English Translation
        AddressTranslation translationEn = translationRepository
                .findByAddressAndLangCode(address, "en")
                .orElse(AddressTranslation.builder()
                        .address(address)
                        .langCode("en")
                        .build());
        translationEn.setAddressLine(request.getAddressEn());
        translationEn.setNote(request.getNoteEn());
        translationRepository.save(translationEn);

        Address updated = addressRepository.save(address);
        log.info("✅ Address updated successfully");

        return updated;
    }

    // ✅ Validation Method
    private void validateRequest(AddressRequest request) {
        Set<ConstraintViolation<AddressRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            log.error("❌ Validation failed: {}", errorMessage);
            throw new BusinessException(errorMessage);
        }
    }

    public Address getById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Address not found"));
    }

    public Page<Address> getAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public List<Address> getByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Customer not found"));
        return addressRepository.findByCustomerId(customer);
    }

    @Transactional
    public void delete(Long id) {
        Address address = getById(id);
        addressRepository.delete(address);
        log.info("✅ Address deleted: {}", id);
    }

    public String getTranslatedAddress(Address address, String lang) {
        return translationRepository.findByAddressAndLangCode(address, lang)
                .map(AddressTranslation::getAddressLine)
                .orElse(translationRepository.findByAddressAndLangCode(address, "en")
                        .map(AddressTranslation::getAddressLine)
                        .orElse(address.getAddress()));
    }

    public String getTranslatedNote(Address address, String lang) {
        return translationRepository.findByAddressAndLangCode(address, lang)
                .map(AddressTranslation::getNote)
                .orElse(translationRepository.findByAddressAndLangCode(address, "en")
                        .map(AddressTranslation::getNote)
                        .orElse(address.getNote()));
    }
}