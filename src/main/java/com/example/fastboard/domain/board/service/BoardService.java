package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.dto.request.BoardUpdateRequest;
import com.example.fastboard.domain.board.dto.response.BoardDetailResponse;
import com.example.fastboard.domain.board.dto.response.BoardResponse;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.BoardImage;
import com.example.fastboard.domain.board.exception.BoardException;
import com.example.fastboard.domain.board.repository.BoardImageRepository;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.exception.AuthException;
import com.example.fastboard.domain.member.service.MemberService;
import com.example.fastboard.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final MemberService memberService;

    @Transactional
    public Long save(BoardCreateRequest request, Long memberId) {
        Member member = memberService.findActiveMemberById(memberId);
        Board board = request.toEntity(member);

        Board newBoard = boardRepository.save(board);

        List<String> imagePaths = extractImageSrcFromContent(request.content());
        imagePaths.forEach(path -> updateImageBoardId(path, board));

        return newBoard.getId();
    }

    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAllByDeletedAtIsNull().stream()
                .map(board -> BoardResponse.fromEntities(board, board.getMember()))
                .collect(Collectors.toList());
    }

    private List<String> extractImageSrcFromContent(String content) {
        // content에서 <img src="..."> 태그의 경로를 추출하는 로직
        List<String> imagePaths = new ArrayList<>();
        Pattern pattern = Pattern.compile("src=\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            String src = matcher.group(1);
            imagePaths.add(src);
        }
        return imagePaths;
    }

    @Transactional
    public void updateImageBoardId(String imagePath, Board board) {
        // 파일 경로에서 uniqueFileName 추출
        String uniqueFileName = Paths.get(imagePath).getFileName().toString();

        Optional<BoardImage> boardImageOptional = boardImageRepository.findBySaveName(uniqueFileName);

        if (boardImageOptional.isPresent()) {
            BoardImage boardImage = boardImageOptional.get();
            boardImage.setBoard(board);
            boardImageRepository.save(boardImage);
        } else {
            log.warn("이미지를 찾을 수 없습니다: uniqueFileName = {}", uniqueFileName);
        }
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
