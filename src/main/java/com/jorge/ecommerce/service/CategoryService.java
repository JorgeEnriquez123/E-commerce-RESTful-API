package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CategoryDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.dto.update.UpdateCategoryDto;
import com.jorge.ecommerce.handler.exception.ResourceNotFoundException;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.repository.CategoryRepository;
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
public class CategoryService {
    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    protected Category findById(Long id) {
        log.debug("Finding category by id: {} using repository", id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
    }

    @Transactional(rollbackFor = Exception.class)
    protected Category save(Category category){
        log.debug("Saving category: {} using repository", category);
        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Integer page, Integer pageSize, String sortOrder, String sortBy) {
        log.debug("Finding all categories");
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page - 1, pageSize, sort);

        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        log.debug("Getting category by id: {}", id);
        Category category = findById(id);
        return convertToDto(category);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        log.debug("Creating category: {}", createCategoryDto);
        Category newCategory = createCategoryFromDto(createCategoryDto);
        Category savedCategory = save(newCategory);
        return convertToDto(savedCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    public CategoryDto updateCategory(Long categoryId, UpdateCategoryDto updateCategoryDto) {
        log.debug("Updating category by id: {}, dto: {}", categoryId, updateCategoryDto);
        Category toUpdateCategory = findById(categoryId);
        updateCategoryFromDto(toUpdateCategory, updateCategoryDto);

        Category savedUpdatedCategory = save(toUpdateCategory);
        return convertToDto(savedUpdatedCategory);
    }

    private Category createCategoryFromDto(CreateCategoryDto createCategoryDto) {
        log.debug("Creating category from Dto: {}", createCategoryDto);
        return modelMapper.map(createCategoryDto, Category.class);
    }

    private void updateCategoryFromDto(Category category, UpdateCategoryDto updateCategoryDto) {
        log.debug("Updating category from Dto: {}", updateCategoryDto);
        modelMapper.map(updateCategoryDto, category);
    }

    private CategoryDto convertToDto(Category category) {
        log.debug("Mapping category: {} to Dto", category);
        return modelMapper.map(category, CategoryDto.class);
    }
}
