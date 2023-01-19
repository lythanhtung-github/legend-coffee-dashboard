package com.cg.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "database_check")
public class DatabaseCheck {
    @Id
    private Long id;

    @Column(name = "product_check")
    private int productCheck;

    @Column(name = "table_check")
    private int tableCheck;
}
