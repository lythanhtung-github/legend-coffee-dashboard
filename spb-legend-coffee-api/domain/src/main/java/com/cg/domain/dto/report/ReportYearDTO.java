package com.cg.domain.dto.report;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportYearDTO {

    private int month;

    private BigDecimal totalAmount;

}
