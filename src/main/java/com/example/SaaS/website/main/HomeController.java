package com.example.SaaS.website.main;


import com.example.SaaS.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private HomeService homeService;
//    @GetMapping("/home")
//    public ResponseEntity<ApiResponse<HomeResponse>> getHome(
//            @RequestHeader(value = "lang", defaultValue = "en") String lang
//    ) {
//        return ResponseEntity.ok(
//                new ApiResponse<>(homeService.getHomeData(lang))
//        );
//    }
    @GetMapping("/home")
    public ResponseEntity<ApiResponse<HomeResponse>> getHome(
            @RequestHeader(value = "lang", defaultValue = "en") String lang
    ) {

        HomeResponse response = homeService.getHomeData(lang);

        return ResponseEntity.ok(
                new com.example.SaaS.common.response.ApiResponse<>(
                        true,
                        "home data retrieved successfully",
                        response
                )
        );
    }

}
