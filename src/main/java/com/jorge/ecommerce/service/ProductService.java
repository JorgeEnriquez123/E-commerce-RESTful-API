package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.ProductDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.dto.update.UpdateProductDto;
import com.jorge.ecommerce.handler.exception.InsufficientProductStockException;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    protected Product findById(Long id) {
        log.debug("Finding product by id: {} using repository", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    protected Product save(Product product){
        log.debug("Saving product: {} using repository", product);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAll(Integer page, Integer pageSize, String sortOrder, String sortBy) {
        log.debug("Finding all products");

        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        if(page <= 1) {
            page = 1;
        }
        if(pageSize <= 1) {
            pageSize = 1;
        }Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        log.debug("Getting product by id: {}", id);
        Product product = findById(id);
        return convertToDto(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDto createProduct(CreateProductDto createProductDto) {
        log.debug("Creating product: {}", createProductDto);
        Product newProduct = createProductFromDto(createProductDto);
        Product savedProduct = save(newProduct);
        return convertToDto(savedProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDto updateProduct(Long productId, UpdateProductDto updateProductDto) {
        log.debug("Updating product by id: {}, new info: {}", productId, updateProductDto);
        Product toUpdateProduct = findById(productId);
        updateProductFromDto(toUpdateProduct, updateProductDto);

        Product savedUpdatedProduct = save(toUpdateProduct);
        return convertToDto(savedUpdatedProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    protected ProductDto reduceStock(Long productId, Integer quantity) {
        log.debug("Reduce stock of product by id: {}, quantity: {}", productId, quantity);
        // Check latest stock availability
        Product product = findById(productId);
        product.setStockQuantity(product.getStockQuantity() - quantity);
        if(product.getStockQuantity() < 0) {
            throw new InsufficientProductStockException("Product with Id: " + productId + " has insufficient stock");
        }
        Product reducedStockProduct = save(product);
        return convertToDto(reducedStockProduct);
    }

    private Product createProductFromDto(CreateProductDto createProductDto) {
        log.debug("Creating Product from Dto: {}", createProductDto);
        Category category = categoryService.findById(createProductDto.getCategoryId());
        Product newProduct = modelMapper.map(createProductDto, Product.class);
        newProduct.setCategory(category);
        return newProduct;
    }

    private void updateProductFromDto(Product product, UpdateProductDto updateProductDto) {
        log.debug("Updating Product from Dto: {}", updateProductDto);
        modelMapper.map(updateProductDto, product);
        Category category = categoryService.findById(updateProductDto.getCategoryId());
        product.setCategory(category);
    }

    private ProductDto convertToDto(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }
}
