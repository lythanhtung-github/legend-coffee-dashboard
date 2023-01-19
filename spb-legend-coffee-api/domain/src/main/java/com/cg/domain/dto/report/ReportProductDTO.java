package com.cg.domain.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ReportProductDTO {

    private String productName;

    private String fileFolder;

    private String fileName;

    private String size;

    private Long quantity;

    private BigDecimal amount;

}
