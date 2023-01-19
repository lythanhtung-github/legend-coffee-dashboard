package com.cg.api;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.Category;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.service.category.CategoryServiceImpl;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryAPI {

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping
    private ResponseEntity<?> findAll() {

        try {
            List<CategoryDTO> categoryDTOList = categoryService.findAllCategoryDTO();

            return new ResponseEntity<>(categoryDTOList, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {

        Optional<CategoryDTO> categoryDTOOptional = categoryService.findCategoryDTOById(id);

        if (!categoryDTOOptional.isPresent()) {
            throw new ResourceNotFoundException("Thể loại không tồn tại !");
        }

        return new ResponseEntity<>(categoryDTOOptional.get().toCategory(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitByCategory = categoryService.existsCategoryByTitle(categoryDTO.getTitle());

        if (exitByCategory) {
            throw new DataInputException("Đã tồn tại!!!");
        }

        categoryDTO.setTitle(categoryDTO.getTitle());
        Category category = categoryService.save(categoryDTO.toCategory());

        return new ResponseEntity<>(category.toCategoryDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {

        categoryService.softDelete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> editCategory(@PathVariable Long id, @Validated @RequestBody CategoryDTO categoryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean exitByCategory = categoryService.existsCategoryByTitle(categoryDTO.getTitle());

        if (exitByCategory) {
            throw new DataInputException("Loại sản phẩm đã tồn tại!");
        }

        Optional<Category> category = categoryService.findById(id);

        if (!category.isPresent()) {
            return new ResponseEntity<>("Loại sản phẩm không tồn tại !", HttpStatus.NOT_FOUND);
        }

        try {

            category.get().setUpdatedAt(new Date());
            category.get().setTitle(categoryDTO.getTitle());

            Category newCategory = categoryService.save(category.get());

            return new ResponseEntity<>(newCategory.toCategoryDTO(), HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>("Vui lòng liên hệ Administrator", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
