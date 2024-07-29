package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.handler.exception.InsufficientProductStock;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    protected Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Integer page, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = findById(id);
        return convertToDto(product);
    }

    @Transactional
    public Product save(Product product){
        return productRepository.save(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDto createProduct(CreateProductDto createProductDto) {
        Product newProduct = createProductFromDto(createProductDto);
        Product savedProduct = save(newProduct);
        return convertToDto(savedProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDto updateProduct(Long productId, CreateProductDto createProductDto) {
        Product toUpdateProduct = findById(productId);
        updateProductFromDto(toUpdateProduct, createProductDto);

        Product savedUpdatedProduct = save(toUpdateProduct);
        return convertToDto(savedUpdatedProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void reduceStock(Long productId, Integer quantity) {
        // Check latest stock availability
        Product product = findById(productId);
        product.setStockQuantity(product.getStockQuantity() - quantity);
        if(product.getStockQuantity() < 0) {
            throw new InsufficientProductStock("Product with Id: " + productId + " has insufficient stock");
        }
        save(product);
    }

    private Product createProductFromDto(CreateProductDto createProductDto) {
        Category category = categoryService.findById(createProductDto.getCategoryId());
        Product newProduct = modelMapper.map(createProductDto, Product.class);
        newProduct.setCategory(category);
        return newProduct;
    }

    private void updateProductFromDto(Product product, CreateProductDto createProductDto) {
        modelMapper.map(createProductDto, product);
        Category category = categoryService.findById(createProductDto.getCategoryId());
        product.setCategory(category);
    }

    private ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
