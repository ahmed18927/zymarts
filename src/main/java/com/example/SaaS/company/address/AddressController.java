//package com.example.SaaS.company.address;
//
//import com.example.SaaS.common.entity.Address;
//import com.example.SaaS.common.response.ApiResponse;
//import com.example.SaaS.company.address.DTOS.AddressRequest;
//import com.example.SaaS.company.address.DTOS.AddressResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/addresses")
//@RequiredArgsConstructor
//public class AddressController {
//    private final AddressService addressService;
//    private final AddressMapper addressMapper;
//
//    /* ============ CREATE ============ */
//    @PostMapping
//    public ResponseEntity<ApiResponse<AddressResponse>> create(
//            @Valid @RequestBody AddressRequest request) {
//
//        Address created = addressService.create(request);
//
//        AddressResponse response = addressMapper.toResponse(
//                created,
//                addressService.getTranslatedAddress(created, "en"),
//                addressService.getTranslatedNote(created, "en")
//        );
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ApiResponse<>(true, "Address created successfully", response));
//    }
//
//    /* ============ GET ALL ============ */
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAll(
//            @RequestParam(defaultValue = "en") String lang) {
//
//        List<AddressResponse> response = addressService.getAll()
//                .stream()
//                .map(address -> addressMapper.toResponse(
//                        address,
//                        addressService.getTranslatedAddress(address, lang),
//                        addressService.getTranslatedNote(address, lang)
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Addresses retrieved successfully", response)
//        );
//    }
//
//    /* ============ GET BY CUSTOMER ID ============ */
//    @GetMapping("/customer/{customerId}")
//    public ResponseEntity<ApiResponse<List<AddressResponse>>> getByCustomerId(
//            @PathVariable Long customerId,
//            @RequestParam(defaultValue = "en") String lang) {
//
//        List<AddressResponse> response = addressService.getByCustomerId(customerId)
//                .stream()
//                .map(address -> addressMapper.toResponse(
//                        address,
//                        addressService.getTranslatedAddress(address, lang),
//                        addressService.getTranslatedNote(address, lang)
//                ))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Customer addresses retrieved successfully", response)
//        );
//    }
//
//    /* ============ GET BY ID ============ */
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<AddressResponse>> getById(
//            @PathVariable Long id,
//            @RequestParam(defaultValue = "en") String lang) {
//
//        Address address = addressService.getById(id);
//
//        AddressResponse response = addressMapper.toResponse(
//                address,
//                addressService.getTranslatedAddress(address, lang),
//                addressService.getTranslatedNote(address, lang)
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Address retrieved successfully", response)
//        );
//    }
//
//    /* ============ UPDATE ============ */
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<AddressResponse>> update(
//            @PathVariable Long id,
//            @Valid @RequestBody AddressRequest request) {
//
//        Address updated = addressService.update(id, request);
//
//        AddressResponse response = addressMapper.toResponse(
//                updated,
//                addressService.getTranslatedAddress(updated, "en"),
//                addressService.getTranslatedNote(updated, "en")
//        );
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Address updated successfully", response)
//        );
//    }
//
//    /* ============ DELETE ============ */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
//
//        addressService.delete(id);
//
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Address deleted successfully", null)
//        );
//    }
//}
package com.example.SaaS.company.address;

import com.example.SaaS.common.entity.Address;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.address.DTOS.AddressRequest;
import com.example.SaaS.company.address.DTOS.AddressResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j // ⬅️ أضف ده
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    /* ============ CREATE ============ */
    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(
            @Valid @RequestBody AddressRequest request) {

        log.info("📨 POST /api/addresses - Creating new address");
        log.info("👤 Customer ID: {}", request.getCustomerId());

        Address created = addressService.create(request);

        AddressResponse response = addressMapper.toResponse(
                created,
                addressService.getTranslatedAddress(created, "en"),
                addressService.getTranslatedNote(created, "en")
        );

        log.info("✅ Address created with ID: {}", response.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Address created successfully", response));
    }

    /* ============ GET ALL ============ */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AddressResponse>>> getAll(
            @RequestParam(defaultValue = "en") String lang,
            @PageableDefault(size = 10) Pageable pageable) {

        log.info("📨 GET /api/addresses - Getting all addresses (lang: {})", lang);

        Page<AddressResponse> response = addressService.getAll(pageable)
                .map(address -> addressMapper.toResponse(
                        address,
                        addressService.getTranslatedAddress(address, lang),
                        addressService.getTranslatedNote(address, lang)
                ));

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Addresses retrieved successfully", response)
        );
    }

    /* ============ GET BY CUSTOMER ID ============ */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getByCustomerId(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "en") String lang) {

        log.info("📨 GET /api/addresses/customer/{} - Getting addresses by customer", customerId);

        List<AddressResponse> response = addressService.getByCustomerId(customerId)
                .stream()
                .map(address -> addressMapper.toResponse(
                        address,
                        addressService.getTranslatedAddress(address, lang),
                        addressService.getTranslatedNote(address, lang)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Customer addresses retrieved successfully", response)
        );
    }

    /* ============ GET BY ID ============ */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "en") String lang) {

        log.info("📨 GET /api/addresses/{} - Getting address by ID (lang: {})", id, lang);

        Address address = addressService.getById(id);

        AddressResponse response = addressMapper.toResponse(
                address,
                addressService.getTranslatedAddress(address, lang),
                addressService.getTranslatedNote(address, lang)
        );

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Address retrieved successfully", response)
        );
    }

    /* ============ UPDATE ============ */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {

        log.info("📨 PUT /api/addresses/{} - Updating address", id);

        Address updated = addressService.update(id, request);

        AddressResponse response = addressMapper.toResponse(
                updated,
                addressService.getTranslatedAddress(updated, "en"),
                addressService.getTranslatedNote(updated, "en")
        );

        log.info("✅ Address updated successfully");

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Address updated successfully", response)
        );
    }

    /* ============ DELETE ============ */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        log.info("📨 DELETE /api/addresses/{} - Deleting address", id);

        addressService.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Address deleted successfully", null)
        );
    }
}