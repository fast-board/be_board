package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewRedisRepository {
    private final RedisTemplate<String, Long> longRedisTemplate;
    private final String PRE_FIX = "boardView:";

    public Long get(Long id) {
        ValueOperations<String, Long> ops = longRedisTemplate.opsForValue();
        Long view = ops.get(PRE_FIX + id);

        if (view != null) {
            return view;
        }
        return null;
    }

    public Long saveWithIncrement(Board board) {
        ValueOperations<String, Long> ops = longRedisTemplate.opsForValue();
        ops.set(PRE_FIX + board.getId(), board.getView() + 1);

        return get(board.getId());
    }

    public Long updateWithIncrement(Long id, Long view) {
        ValueOperations<String, Long> ops = longRedisTemplate.opsForValue();
        ops.set(PRE_FIX + id, view + 1);
        return view + 1;
    }
}
