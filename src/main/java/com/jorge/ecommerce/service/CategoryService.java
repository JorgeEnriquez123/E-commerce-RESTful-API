package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CategoryDto;
import com.jorge.ecommerce.dto.create.CreateCategoryDto;
import com.jorge.ecommerce.model.Category;
import com.jorge.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
    }

    public CategoryDto findById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> modelMapper.map(category, CategoryDto.class)).orElseThrow();
    }

    public CategoryDto save(CreateCategoryDto createCategoryDto) {
        Category savedCategory = categoryRepository.save(
                modelMapper.map(createCategoryDto, Category.class)
        );
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    public CategoryDto update(Long categoryId, CreateCategoryDto createCategoryDto) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow();
        modelMapper.map(createCategoryDto, existingCategory);
        categoryRepository.save(existingCategory);
        return modelMapper.map(existingCategory, CategoryDto.class);
    }
}
