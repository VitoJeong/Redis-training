package com.redis.training.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long save(Board board) {
        boardRepository.save(board);

        return board.getId();
    }

    // 키값이 'methodName::id' 형식인 캐시를 생성
    @Cacheable(key = "#id", value = "findOne")
    public Board findOne(Long id) {

        // 캐싱을 확인하기 위해 시간지연 (520ms -> 7ms)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return boardRepository.findById(id).get();
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    // 캐시 삭제(지우고싶은 캐시의 value로 지정)
    @CacheEvict(value = "findOne")
    @Transactional
    public Long delete(Long id) {
//        boardRepository.deleteById(id);
        log.info("delete user id:{}", id);

        return id;
    }

    @Transactional
    public Long update(Long id, Board board) {
        Board findBoard = boardRepository.findById(id).get();
        findBoard.update(board);

        return id;
    }
}