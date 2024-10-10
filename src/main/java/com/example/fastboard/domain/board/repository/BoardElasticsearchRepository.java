package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.BoardDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BoardElasticsearchRepository extends ElasticsearchRepository<BoardDocument,Long> {
    List<BoardDocument> findByTitleContaining(String title);
}
