package com.jorge.ecommerce.dto.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// This class' purpose is to wrap the Pageable (VIA_DTO) information to show the proper returned schema on Swagger
public class PageDto {
    private int size;
    private int number;
    private int totalElements;
    private int totalPages;
}
