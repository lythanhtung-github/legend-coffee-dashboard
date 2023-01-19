package com.cg.repository;

import com.cg.domain.dto.category.CategoryDTO;
import com.cg.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Modifying
    @Query("UPDATE Category AS ctgr SET ctgr.deleted = true WHERE ctgr.id = :categoryId")
    void softDelete(@Param("categoryId") Long categoryId);


    @Query("SELECT NEW com.cg.domain.dto.category.CategoryDTO (" +
                "ctgr.id, " +
                "ctgr.title " +
            ")" +
            "FROM Category ctgr WHERE ctgr.deleted = false")
    List<CategoryDTO> findAllCategoryDTO();


    @Query("SELECT NEW com.cg.domain.dto.category.CategoryDTO (" +
                "ctgr.id, " +
                "ctgr.title " +
            ")" +
            "FROM Category  ctgr WHERE ctgr.id = ?1 ")
    Optional<CategoryDTO> findCategoryDTOById(Long id);


    Boolean existsCategoryByTitle(String title);

}
