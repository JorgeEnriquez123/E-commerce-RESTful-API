package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CategoryDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.handlers.exception.EntityNotFoundException;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = findById(id);
        return convertToDto(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto save(CreateCategoryDto createCategoryDto) {
        Category newCategory = createCategoryFromDto(createCategoryDto);
        Category savedCategory = categoryRepository.save(newCategory);
        return this.convertToDto(savedCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto update(Long categoryId, CreateCategoryDto createCategoryDto) {
        Category toUpdateCategory = findById(categoryId);
        updateCategoryFromDto(toUpdateCategory, createCategoryDto);

        Category savedUpdatedCategory = categoryRepository.save(toUpdateCategory);
        return convertToDto(savedUpdatedCategory);
    }

    public Category createCategoryFromDto(CreateCategoryDto createCategoryDto) {
        return modelMapper.map(createCategoryDto, Category.class);
    }

    public void updateCategoryFromDto(Category category, CreateCategoryDto createCategoryDto) {
        modelMapper.map(createCategoryDto, category);
    }

    public CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
