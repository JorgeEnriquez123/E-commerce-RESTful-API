package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CategoryDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.dto.create.CreateProductDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.model.Product;
import com.jorge.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));
    }

    protected Category findCategoryEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto save(CreateCategoryDto createCategoryDto) {
        Category newCategory = createCategoryFromDto(createCategoryDto);
        Category savedCategory = categoryRepository.save(newCategory);
        return this.convertToDto(savedCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto update(Long categoryId, CreateCategoryDto createCategoryDto) {
        Category toUpdateCategory = updateCategoryFromDto(categoryId, createCategoryDto);
        Category savedUpdatedCategory = categoryRepository.save(toUpdateCategory);
        return convertToDto(savedUpdatedCategory);
    }

    public Category createCategoryFromDto(CreateCategoryDto createCategoryDto) {
        return modelMapper.map(createCategoryDto, Category.class);
    }

    public Category updateCategoryFromDto(Long categoryId, CreateCategoryDto createCategoryDto) {
        Category existingCategory = this.findCategoryEntityById(categoryId);
        modelMapper.map(createCategoryDto, existingCategory);
        return existingCategory;
    }

    public CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
