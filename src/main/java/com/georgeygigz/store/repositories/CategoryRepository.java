package com.georgeygigz.store.repositories;

import com.georgeygigz.store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
