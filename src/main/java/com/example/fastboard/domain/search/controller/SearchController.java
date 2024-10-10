package com.example.fastboard.domain.search.controller;

import com.example.fastboard.domain.board.entity.BoardDocument;
import com.example.fastboard.domain.search.service.SearchService;
import com.example.fastboard.global.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/migrate")
    public ResponseEntity<String> migrate() {
        searchService.migrateDataToElasticsearch();
        return ResponseEntity.ok("Data migration completed successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<BoardDocument>>> searchList(
            @RequestParam(value = "title", required = false) String title,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ResponseDTO.okWithData(searchService.searchByTitle(title,pageable))
        );
    }
}
