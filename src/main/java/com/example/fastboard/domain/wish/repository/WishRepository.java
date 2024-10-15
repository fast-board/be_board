package com.example.fastboard.domain.wish.repository;

import com.example.fastboard.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish,Long> {
}
