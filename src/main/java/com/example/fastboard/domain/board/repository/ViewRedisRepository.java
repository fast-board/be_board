package com.example.fastboard.domain.board.repository;

import com.example.fastboard.domain.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ViewRedisRepository {
    private final RedisTemplate<String, Long> longRedisTemplate;
    private final String PRE_FIX = "boardView:";
    private final RedissonClient redissonClient;


    public Long get(Board board) {
        RLock lock = redissonClient.getLock("ViewRLock" + board.getId());

        try {
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!available) {
                return board.getView();
            }

            Long view = longRedisTemplate.opsForValue().get(PRE_FIX + board.getId());

            if (view == null) {
                longRedisTemplate.opsForValue().set(PRE_FIX + board.getId(), board.getView() + 1);
                return board.getView() + 1;
            }

            else {
                longRedisTemplate.opsForValue().set(PRE_FIX + board.getId(), view + 1);
                return view + 1;
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public Set<String> getKeysWithScan() {
        Set<String> keys = new HashSet<>();
        Cursor<byte[]> cursor = longRedisTemplate.executeWithStickyConnection(redisConnection ->
                redisConnection.scan(ScanOptions.scanOptions().match(PRE_FIX + "*").count(1000).build())
        );

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }
        cursor.close();
        return keys;
    }

    public Long getViewByKey(String key) {
        return longRedisTemplate.opsForValue().get(key);
    }

    public void deleteByKey(String key) {
        longRedisTemplate.delete(key);
    }

}
