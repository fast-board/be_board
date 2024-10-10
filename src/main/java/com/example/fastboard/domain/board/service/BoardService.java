package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.dto.request.BoardUpdateRequest;
import com.example.fastboard.domain.board.dto.response.BoardDetailResponse;
import com.example.fastboard.domain.board.dto.response.BoardResponse;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.domain.member.service.MemberService;
import com.example.fastboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageService boardImageService;
    private final MemberService memberService;

    @Transactional
    public Long save(BoardCreateRequest request, Long memberId) {
        Member member = memberService.findActiveMemberById(memberId);
        Board board = request.toEntity(member);

        Board newBoard = boardRepository.save(board);

        List<Long> imageIds = extractImageIdFromContent(request.content());
        imageIds.forEach(id -> boardImageService.updateImageBoardId(id, board));

        return newBoard.getId();
    }

    public List<BoardResponse> searchList(String title) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<BoardResponse> collect = boardRepository.findByTitleContainingOrderByCreatedAtDesc(title).stream()
                .map(BoardResponse::fromEntities)
                .collect(Collectors.toList());

        stopWatch.stop();
        log.info("Execution time: " + stopWatch.getTotalTimeMillis() + " ms");

        return collect;
    }

    public Page<BoardResponse> getAllBoards(Pageable pageable) {
        return boardRepository.findAllByDeletedAtIsNull(pageable)
                .map(BoardResponse::fromEntities);
    }

    private List<Long> extractImageIdFromContent(String content) {
        // content에서 <img src="..."> 태그의 경로를 추출하는 로직
        List<Long> imageIds = new ArrayList<>();
        Pattern pattern = Pattern.compile("src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String src = matcher.group(1);
            imageIds.add(Long.parseLong(src.substring(src.lastIndexOf("/") + 1)));
        }
        return imageIds;
    }


    @Transactional
    public Long update(BoardUpdateRequest request, Long memberId, Long boardId) {
        Member member = memberService.findActiveMemberById(memberId);
        Board existingBoard = findActiveBoardById(boardId);

        if (!member.equals(existingBoard.getMember())) {
            throw new AuthException(ErrorCode.AUTHOR_MISMATCH_EXCEPTION);
        }

        existingBoard.update(request.title(), request.content(), request.category());
        return existingBoard.getId();
    }

    public Board findActiveBoardById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardException(ErrorCode.BOARD_NOT_FOUND_EXCEPTION));
        if (board.isDelete()) {
            throw new BoardException(ErrorCode.BOARD_DELETED_EXCEPTION);
        }
        return board;
    }

    @Transactional
    public BoardDetailResponse loadBoardDetail(Long boardId, Long memberId) {
        memberService.findActiveMemberById(memberId);
        Board board = findActiveBoardById(boardId);
        if (!board.getMember().getId().equals(memberId)) {
            board.plusViewCount();
        }
        return BoardDetailResponse.fromEntities(board);
    }

    @Transactional
    public void delete(Long boardId, Long memberId) {
        Member member = memberService.findActiveMemberById(memberId);
        Board board = findActiveBoardById(boardId);
        if (!board.getMember().equals(member)) {
            throw new AuthException(ErrorCode.AUTHOR_MISMATCH_EXCEPTION);
        }
        board.delete();
    }
}
