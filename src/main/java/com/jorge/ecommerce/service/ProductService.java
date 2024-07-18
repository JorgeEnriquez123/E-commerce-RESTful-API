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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    public List<ProductDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public ProductDto findById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + id + " not found"));
    }

    protected Product findProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDto save(CreateProductDto createProductDto) {
        Product newProduct = this.createProductFromDto(createProductDto);
        Product savedProduct = productRepository.save(newProduct);
        return this.convertToDto(savedProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDto update(Long productId, CreateProductDto createProductDto) {
        Product toUpdateProduct = this.updateProductFromDto(productId, createProductDto);
        Product savedUpdatedProduct = productRepository.save(toUpdateProduct);
        return this.convertToDto(savedUpdatedProduct);
    }

    public Product createProductFromDto(CreateProductDto createProductDto) {
        Category category = categoryService.findCategoryEntityById(createProductDto.getCategoryId());
        Product newProduct = modelMapper.map(createProductDto, Product.class);
        newProduct.setCategory(category);
        return newProduct;
    }

    public Product updateProductFromDto(Long productId, CreateProductDto createProductDto) {
        Product toUpdateProduct = this.findProductEntityById(productId);
        modelMapper.map(createProductDto, toUpdateProduct);

        Category category = categoryService.findCategoryEntityById(createProductDto.getCategoryId());
        toUpdateProduct.setCategory(category);

        return toUpdateProduct;
    }

    public ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
