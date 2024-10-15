package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.request.BoardCreateRequest;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.Category;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    private MemberService memberService;
    @Mock
    private BoardRepository boardRepository;
    @InjectMocks
    private BoardService boardService;


    @Test
    @DisplayName("게시글 작성하기")
    public void 게시글_작성(){
        // given (데이터 준비)
        Long memberId = 1L;
        String title = "테스트 게시글";
        String content = "<P>테스트 내용</p>";
        Category category= Category.HUMOR;

        BoardCreateRequest request = new BoardCreateRequest(title, content, category);

        Member mockMember = Member.builder()
                .role(Role.USER)
                .build();

        Board board = mock(Board.class);
        when(memberService.findActiveMemberById(memberId)).thenReturn(mockMember);
        when(boardRepository.save(any(Board.class))).thenReturn(board);
        when(board.getId()).thenReturn(1L);

        // when
        Long boardId = boardService.create(request, memberId);

        // then
        assertDoesNotThrow(() -> boardService.create(request,memberId));
        assertEquals(1L, boardId);
    }
}
