package com.example.SaaS.company.banner;

import com.example.SaaS.common.entity.Banner;
import com.example.SaaS.common.entity.CategoryTransaction;
import com.example.SaaS.common.response.ApiResponse;
import com.example.SaaS.company.category.CategoryResourceDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/company/banner")
public class BannerController {
    private final BannerService bannerService;
    private final BannerMapper  bannerMapper;
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BannerResourceDTO>>> getAllBanners(
            @RequestParam(defaultValue = "0") int page , @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<BannerResourceDTO> responce =
                bannerService.findAll(pageable).map(
                        banner -> bannerMapper.toResponce(banner ,"en")
                        );
        return  ResponseEntity.ok(new ApiResponse<>(
                true ,"retrived successfully" ,responce));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BannerResourceDTO>> getBanner(@PathVariable Long id){
        Banner banner =bannerService.getBannerById(id);
        return  ResponseEntity.ok(new ApiResponse<>(true ,"retrived successfully" ,bannerMapper.toResponce(banner ,"en")));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBanner(@PathVariable Long id){
        bannerService.deleteBanner(id);
        return  ResponseEntity.ok(new ApiResponse<>(true ,"deleted successfully" ,null));

    }
     @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BannerResourceDTO>> saveBanner(@Valid @ModelAttribute BannerRequest request){

         Banner saved =     bannerService.createBanner(request);
       return  ResponseEntity.ok(new ApiResponse<>(true ,"saved successfully" ,bannerMapper.toResponce(saved ,"en")));
     }

     @PutMapping(
             value = "/{id}" ,
             consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BannerResourceDTO>>  updateBanner(@PathVariable Long id, @Valid @ModelAttribute BannerRequest request){

        Banner updated = bannerService.updateBanner(id, request);
        return  ResponseEntity.ok(new ApiResponse<>(true ,"updated successfully",bannerMapper.toResponce(updated ,"en")));
     }
}
