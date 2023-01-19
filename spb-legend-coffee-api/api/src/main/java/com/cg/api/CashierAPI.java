package com.cg.api;


import com.cg.domain.dto.product.ProductCashierDTO;
import com.cg.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cashier")
public class CashierAPI {

    @Autowired
    private IProductService productService;

    @GetMapping("/products")
    @PreAuthorize("hasAnyAuthority('ADMIN','CASHIER')")
    public ResponseEntity<?> getAllByDeletedIsFalseForCashier() {
        List<ProductCashierDTO> productCashierDTOS = productService.getAllProductCashierDTOWhereDeletedIsFalse();

        if (productCashierDTOS.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(productCashierDTOS, HttpStatus.OK);
    }
}
