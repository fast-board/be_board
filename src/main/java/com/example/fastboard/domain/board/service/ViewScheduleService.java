package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.board.repository.ViewRedisRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewScheduleService {
    private final ViewRedisRepository viewRedisRepository;
    private final BoardRepository boardRepository;

    @Scheduled(fixedDelay = 5000L)
    @Async("viewCountExecutor")
    @Transactional
    public void viewSchedule() {
        Set<String> keys = viewRedisRepository.getKeysWithScan();
        keys.forEach(key -> {

            Long id = Long.parseLong(key.substring(key.lastIndexOf(":") + 1));
            Long viewCount = viewRedisRepository.getViewByKey(key);
            if (viewCount != null) {
                boardRepository.updateViewById(id, viewCount);
                viewRedisRepository.deleteByKey(key);
            }
        });
    }
}
