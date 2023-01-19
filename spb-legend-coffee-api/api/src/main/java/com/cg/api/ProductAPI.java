package com.cg.api;

import com.cg.domain.dto.product.*;
import com.cg.domain.entity.Category;
import com.cg.domain.entity.Product;
import com.cg.exception.DataInputException;
import com.cg.service.category.ICategoryService;
import com.cg.service.databaseCheck.IDatabaseCheckService;
import com.cg.service.product.IProductService;
import com.cg.utils.AppUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController
@RequestMapping("/api/products")
public class ProductAPI {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IDatabaseCheckService databaseCheckService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> getAllByDeletedIsFalse() {
        List<ProductDTO> productDTOS = productService.getAllProductDTOWhereDeletedIsFalse();

        if (productDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<?> getById(@PathVariable String productId) {
        long pid;

        try {
            pid = Long.parseLong(productId);
        } catch (NumberFormatException e) {
            throw new DataInputException("ID sản phẩm không hợp lệ.");
        }

        Optional<Product> productOptional = productService.findById(pid);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không hợp lệ.");
        }

        return new ResponseEntity<>(productOptional.get().toProductDTO(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> createProduct(MultipartFile file, @Validated ProductCreateDTO productCreateDTO, BindingResult bindingResult) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new SizeDTODeserializer());
        mapper.registerModule(module);

        if (productCreateDTO.getSizes().equals("") || productCreateDTO.getSizes().equals("{}")) {
            throw new DataInputException("Giá kích thước không được để trống.");
        }
        List<SizeDTO> sizeDTOList = mapper.readValue(productCreateDTO.getSizes(), new TypeReference<List<SizeDTO>>() {
        });

        for (SizeDTO item : sizeDTOList) {
            if (item.getPrice().equals("")) throw new DataInputException("Giá sản phẩm không được để trống.");
            try {

                int price = Integer.parseInt(item.getPrice());
                if (!appUtils.checkPrice(price).equals(""))
                    throw new DataInputException(appUtils.checkPrice(price));

            } catch (NumberFormatException e) {
                throw new DataInputException("Giá sản phẩm phải là số.");
            }
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long categoryId = Long.parseLong(productCreateDTO.getCategoryId());
        Optional<Category> categoryOptional = categoryService.findById(categoryId);

        if (!categoryOptional.isPresent()) {
            throw new DataInputException("Loại sản phẩm không hợp lệ.");
        }

        if (productService.existsByTitle(productCreateDTO.getTitle())) {
            throw new DataInputException("Sản phẩm đã tồn tại trong hệ thống.");
        }

        if (productCreateDTO.getSummary().trim().equals("")) {
            productCreateDTO.setSummary(productCreateDTO.getDescription());
        }

        productCreateDTO.setId(null);

        Product newProduct;

        if (file != null) {
            newProduct = productService.createWithImage(productCreateDTO, file);
        } else {
            newProduct = productService.createNoImage(productCreateDTO);
        }

        databaseCheckService.updateWithProductCheck();

        return new ResponseEntity<>(newProduct.toProductDTO(), HttpStatus.CREATED);
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long productId, MultipartFile file, @Validated ProductUpdateDTO productUpdateDTO, BindingResult bindingResult) throws JsonProcessingException {

        Optional<Product> productOptional = productService.findById(productId);
        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không tồn tại.");
        }

        Product product = productOptional.get();

        Long categoryId = Long.parseLong(productUpdateDTO.getCategoryId());
        Optional<Category> categoryOptional = categoryService.findById(categoryId);

        if (!categoryOptional.isPresent()) {
            throw new DataInputException("Loại sản phẩm không hợp lệ.");
        }

        if (productService.existsByProductNameAndIdNot(productUpdateDTO.getTitle(), productId)) {
            throw new DataInputException("Sản phẩm đã tồn tại trong hệ thống.");
        }

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(List.class, new SizeDTODeserializer());
        mapper.registerModule(module);

        if (productUpdateDTO.getSizes().equals("") || productUpdateDTO.getSizes().equals("{}")) {
            throw new DataInputException("Giá kích thước không được để trống.");
        }

        List<SizeDTO> sizeDTOList = mapper.readValue(productUpdateDTO.getSizes(), new TypeReference<List<SizeDTO>>() {
        });

        for (SizeDTO item : sizeDTOList) {
            if (item.getPrice().equals("")) throw new DataInputException("Giá sản phẩm không được để trống.");
            try {

                int price = Integer.parseInt(item.getPrice());
                if (!appUtils.checkPrice(price).equals(""))
                    throw new DataInputException(appUtils.checkPrice(price));

            } catch (NumberFormatException e) {
                throw new DataInputException("Giá sản phẩm phải là số.");
            }
        }

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (productUpdateDTO.getSummary().trim().equals("")) {
            productUpdateDTO.setSummary(productUpdateDTO.getDescription());
        }

        String title = productUpdateDTO.getTitle();
        String description = productUpdateDTO.getDescription();
        String summary = productUpdateDTO.getSummary();
        Category category = categoryOptional.get();
        String sizes = productUpdateDTO.getSizes();

        product.setTitle(title)
                .setCategory(category)
                .setSizes(sizes)
                .setDescription(description)
                .setSummary(summary);

        product = productService.save(product);

        if (file != null) {
            product = productService.saveWithImage(product, file);
        }

        databaseCheckService.updateWithProductCheck();

        return new ResponseEntity<>(product.toProductDTO(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{productId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long productId) {

        Optional<Product> productOptional = productService.findById(productId);

        if (!productOptional.isPresent()) {
            throw new DataInputException("ID sản phẩm không hợp lệ.");
        }

        try {

            productService.softDelete(productId);

            databaseCheckService.updateWithProductCheck();

            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        } catch (Exception e) {

            throw new DataInputException("Vui lòng liên hệ Administrator.");
        }
    }

}
