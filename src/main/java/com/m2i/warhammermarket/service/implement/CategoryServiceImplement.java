package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DTO.CategoryDTO;
import com.m2i.warhammermarket.repository.CategoryRepository;
import com.m2i.warhammermarket.service.CategoryService;
import com.m2i.warhammermarket.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImplement implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryServiceImplement() {

    }

    @Override
    public CategoryDTO save(CategoryDTO category) {
        return null;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return this.categoryRepository.findAll().stream().map(categoryMapper::categoryToCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDTO> findOne(Long id) {
        return Optional.ofNullable(this.categoryMapper.categoryToCategoryDTO(this.categoryRepository.findById(id).orElse(null)));
    }

    @Override
    public void delete(Long id) {

    }
}
