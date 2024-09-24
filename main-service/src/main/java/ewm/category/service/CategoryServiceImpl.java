package ewm.category.service;

import ewm.category.dto.CategoryDto;
import ewm.category.dto.CreateCategoryDto;
import ewm.category.mapper.CategoryMapper;
import ewm.category.model.Category;
import ewm.category.repository.CategoryRepository;
import ewm.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private static final String ERROR_MESSAGE = "Category not found";

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable).stream().map(CategoryMapper.INSTANCE::categoryToCategoryDto).toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE);
        }
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category.get());
    }

    @Override
    public CategoryDto add(CreateCategoryDto createCategoryDto) {
        Category category = categoryRepository.save(Category.builder().name(createCategoryDto.getName()).build());
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryDto createCategoryDto) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE);
        }
        Category categoryToUpdate = category.get();
        categoryToUpdate.setName(createCategoryDto.getName());
        Category updated = categoryRepository.save(categoryToUpdate);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(updated);
    }

    @Override
    public void delete(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE);
        }
        categoryRepository.deleteById(id);
    }
}
