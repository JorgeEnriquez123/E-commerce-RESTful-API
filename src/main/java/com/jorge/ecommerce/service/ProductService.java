package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.CategoryRepository;
import com.jorge.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(Product::toDto)
                .toList();
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id)
                .map(Product::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + id + " not found"));
    }
    public ProductDto save(CreateProductDto createProductDto) {
        Product newProduct = createProductDto.toEntity();
        Category assignedCategory = null;
        if(createProductDto.getCategoryId() != null) {
            assignedCategory = categoryRepository.findById(createProductDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category with id: " + createProductDto.getCategoryId() + " not found"));
        }
        newProduct.setCategory(assignedCategory);
        productRepository.save(newProduct);
        return newProduct.toDto();
    }

    public ProductDto update(Long productId, CreateProductDto createProductDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + productId + " not found"));

        if(createProductDto.getCategoryId() != null) {
            Category assignedCategory = categoryRepository.findById(createProductDto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category with id: " + createProductDto.getCategoryId() + " not found"));
            existingProduct.setCategory(assignedCategory);
        }

        productRepository.save(existingProduct.updateFromCreateDto(createProductDto));
        return existingProduct.toDto();
    }
}
