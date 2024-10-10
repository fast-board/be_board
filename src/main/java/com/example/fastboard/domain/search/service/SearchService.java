package com.example.fastboard.domain.search.service;

import com.example.fastboard.domain.board.dto.BoardMapper;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.repository.BoardElasticsearchRepository;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.board.entity.BoardDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final BoardRepository boardRepository;
    private final BoardElasticsearchRepository boardElasticsearchRepository;


    public List<BoardDocument> searchByTitle(String title, Pageable pageable){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        List<BoardDocument> byTitle = boardElasticsearchRepository.findByTitleContaining(title);

        stopWatch.stop();
        log.info("Execution time: " + stopWatch.getTotalTimeMillis() + " ms");

        System.out.println(byTitle.size());

        return null;
    }

    public void migrateDataToElasticsearch() {
        List<Board> boards = boardRepository.findAll();

        System.out.println(boards.size());
        List<BoardDocument> boardDocuments = boards.stream()
                .map(BoardMapper::toDocument)
                .collect(Collectors.toList());


        boardElasticsearchRepository.saveAll(boardDocuments); // Elasticsearch에 저장
    }
}
