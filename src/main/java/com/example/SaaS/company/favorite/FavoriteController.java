package com.example.SaaS.company.favorite;

import com.example.SaaS.common.dto.ApiResponse;
import com.example.SaaS.common.entity.Favorite;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final FavoriteMapper favoriteMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> addFavorite(
            @RequestBody @Valid FavoriteRequest request
    ) {

        Favorite favorite =
                favoriteService.addToFavorite(request.getProductId());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Added successfully",
                        favoriteMapper.toResponse(favorite)
                )
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            @PathVariable Long productId
    ) {

        favoriteService.removeFromFavorite(productId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Removed successfully",
                        null
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FavoriteResponse>>> getFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<FavoriteResponse> response =
                favoriteService.getFavorites(pageable)
                        .map(favoriteMapper::toResponse);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Retrieved successfully",
                        response
                )
        );
    }
}