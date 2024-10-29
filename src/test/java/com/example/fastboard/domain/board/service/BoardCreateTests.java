package com.example.fastboard.domain.board.service;

import com.example.fastboard.domain.board.dto.parameter.BoardPostParam;
import com.example.fastboard.domain.board.entity.Board;
import com.example.fastboard.domain.board.entity.Category;
import com.example.fastboard.domain.board.repository.BoardRepository;
import com.example.fastboard.domain.member.entity.Member;
import com.example.fastboard.domain.member.entity.Role;
import com.example.fastboard.domain.member.service.MemberFindService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardCreateTests {
    @Mock
    BoardRepository boardRepository;

    @Mock
    MemberFindService memberFindService;

    @InjectMocks
    BoardPostService boardPostService;

    @Test
    @DisplayName("게시글 저장 - 성공 케이스")
    public void saveBoard() {

        // Given
        Member member = Member.builder()
                .id(1L)
                .name("TestName")
                .email("TestEmail@email.com")
                .nickname("TestNickname")
                .phoneNumber("TestPhoneNumber")
                .role(Role.USER)
                .encryptedPassword("TestPassword")
                .build();


        Board board = Board.builder()
                .title("TestTitle")
                .content("TestContent")
                .category(Category.FIRST)
                .member(member)
                .build();

        BoardPostParam boardPostParam = BoardPostParam.builder()
                .title("TestTitle")
                .content("TestContent")
                .category(Category.FIRST)
                .authorId(member.getId())
                .build();

        when(boardRepository.save(any(Board.class))).thenReturn(board);
        when(memberFindService.findMemberById(anyLong())).thenReturn(member);

        // When
        Board postedBoard = boardPostService.create(boardPostParam);


        // then
        Assertions.assertEquals(member.getId(), postedBoard.getMember().getId());
        Assertions.assertEquals(board.getTitle(), postedBoard.getTitle());
        Assertions.assertEquals(board.getContent(), postedBoard.getContent());
        Assertions.assertEquals(board.getCategory(), postedBoard.getCategory());
    }
}
