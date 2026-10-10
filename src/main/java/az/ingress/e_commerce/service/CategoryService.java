package az.ingress.e_commerce.service;

import az.ingress.e_commerce.dto.CategoryRequest;
import az.ingress.e_commerce.dto.CategoryResponse;
import az.ingress.e_commerce.entity.Category;
import az.ingress.e_commerce.exception.ResourceNotFoundException;
import az.ingress.e_commerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request) {

        Category category = new Category();
        category.setName(request.getName());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName()
        );

    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(" Category not found with id " + id));

        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }

}
