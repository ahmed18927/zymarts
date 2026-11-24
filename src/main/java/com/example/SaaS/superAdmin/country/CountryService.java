package com.example.SaaS.superAdmin.country;

import com.example.SaaS.common.entity.Country;
import com.example.SaaS.common.exception.BusinessException;
import com.example.SaaS.common.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final FileStorageService fileStorageService;

    public Country create(Country country, MultipartFile flagFile) {

        if (countryRepository.existsByCode(country.getCode())) {
            throw new BusinessException("Country already exists");
        }

        if (flagFile != null && !flagFile.isEmpty()) {
            String flagPath = fileStorageService.saveFile(flagFile);
            country.setFlag(flagPath);
        }

        country.setCreatedAt(Instant.now());
        country.setUpdatedAt(Instant.now());
        return countryRepository.save(country);
    }

    public Country update(Long id, Country country, MultipartFile flagFile) {

        Country existing = countryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Country not found"));

        existing.setName(country.getName());
        existing.setCode(country.getCode());

        if (flagFile != null && !flagFile.isEmpty()) {
            if (existing.getFlag() != null) {
                fileStorageService.deleteFile(existing.getFlag());
            }
            String newFlagPath = fileStorageService.saveFile(flagFile);
            existing.setFlag(newFlagPath);
        }

        existing.setUpdatedAt(Instant.now());
        return countryRepository.save(existing);
    }

    public Country getById(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Country not found"));
    }

    public Page<Country> getAll(Pageable  pageable) {
        return countryRepository.findAll(pageable);
    }

    public void delete(Long id) {

        Country country = getById(id);

        if (country.getFlag() != null) {
            fileStorageService.deleteFile(country.getFlag());
        }

        countryRepository.delete(country);
    }

    /* ===== helper for language ===== */
    public String resolveNameByLang(Map<String, String> name, String lang) {
        if (name == null) return null;
        if (lang == null) return name.get("en");
        return name.getOrDefault(lang.toLowerCase(), name.get("en"));
    }
}
