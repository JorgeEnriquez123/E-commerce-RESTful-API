package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CategoryDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.repository.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    protected Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
    }

    @Transactional
    protected Category save(Category category){
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Integer page, Integer pageSize, String sortOrder, String sortBy) {
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = findById(id);
        return convertToDto(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        Category newCategory = createCategoryFromDto(createCategoryDto);
        Category savedCategory = save(newCategory);
        return convertToDto(savedCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto updateCategory(Long categoryId, CreateCategoryDto createCategoryDto) {
        Category toUpdateCategory = findById(categoryId);
        updateCategoryFromDto(toUpdateCategory, createCategoryDto);

        Category savedUpdatedCategory = save(toUpdateCategory);
        return convertToDto(savedUpdatedCategory);
    }

    private Category createCategoryFromDto(CreateCategoryDto createCategoryDto) {
        return modelMapper.map(createCategoryDto, Category.class);
    }

    private void updateCategoryFromDto(Category category, CreateCategoryDto createCategoryDto) {
        modelMapper.map(createCategoryDto, category);
    }

    private CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
}
