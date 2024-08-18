package com.jorge.ecommerce.dto.pagination;

import com.jorge.ecommerce.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// This class' purpose is to show the proper returned schema on Swagger
public class PaginatedProductResponse {
    private List<ProductDto> content;
    private PageDto page;
}
