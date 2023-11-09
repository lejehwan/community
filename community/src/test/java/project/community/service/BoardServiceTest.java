package project.community.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.community.dto.BoardReqDto;
import project.community.dto.BoardRespDto;
import project.community.entity.Board;
import project.community.entity.Member;
import project.community.repository.BoardRepository;
import project.community.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author jeonghwanlee
 * @date 2023-11-07
 */
@SpringBootTest
@Transactional
class BoardServiceTest {

    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("게시글 등록 시, 저장 확인")
    void checkDefaultValueBySave() throws InterruptedException {
        Member member = Member.builder().username("test user").build();
        String title = "test board title 1";
        String contents = "test board contents 1";
        Board board = Board.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();

        memberRepository.save(member);
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);
        Board savedBoard = boardService.save(board);

        assertThat(savedBoard).isEqualTo(board);
        assertThat(savedBoard.getTitle()).isEqualTo(title);
        assertThat(savedBoard.getContents()).isEqualTo(contents);
        assertThat(savedBoard.getMember()).isEqualTo(member);
        assertThat(savedBoard.getCreateBy()).isNotBlank();
        assertThat(savedBoard.getCreateDate()).isAfter(now);
        assertThat(savedBoard.getLastModifiedDate()).isAfter(now);
        assertThat(savedBoard.getLastModifiedBy()).isNotBlank();
        assertThat(savedBoard.getViewCount()).isEqualTo(0);
        assertThat(savedBoard.getThumbsDownCount()).isEqualTo(0);
        assertThat(savedBoard.getThumbsUpCount()).isEqualTo(0);
        assertThat(savedBoard.isDelete()).isFalse();
    }

    @Test
    @DisplayName("게시글 수정 시, 변경 확인")
    @Rollback(value = false)
    void checkDefaultValue() throws InterruptedException {
        Member member = Member.builder().username("test user").build();
        String title = "test board title 1";
        String contents = "test board contents 1";
        Board board = Board.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();

        memberRepository.save(member);
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);
        Board savedBoard = boardService.save(board);
        String updateTitle = "test update board title 1";
        String updateContents = "test update board contents 1";
        BoardReqDto boardReqDto = BoardReqDto.builder()
                .title(updateTitle)
                .contents(updateContents)
                .viewCount(3L)
                .thumbsUpCount(2L)
                .thumbsDownCount(0L)
                .member(member)
                .replies(new ArrayList<>())
                .build();
        Thread.sleep(1000);
        Board updateBoard = boardService.updateById(savedBoard.getId(), boardReqDto);

        assertThat(updateBoard).isEqualTo(savedBoard);
        assertThat(updateBoard.getTitle()).isEqualTo(savedBoard.getTitle());
        assertThat(updateBoard.getContents()).isEqualTo(savedBoard.getContents());
        assertThat(updateBoard.getCreateDate()).isAfter(now);
        assertThat(updateBoard.getLastModifiedDate()).isAfter(now);

        boardRepository.flush(); // 수정시간과 생성 시간 확인을 위한 DB와 동기화 작업
        assertThat(updateBoard.getLastModifiedDate()).isAfter(updateBoard.getCreateDate());

        assertThat(updateBoard.getViewCount()).isEqualTo(3L);
        assertThat(updateBoard.getThumbsUpCount()).isEqualTo(2L);
        assertThat(updateBoard.getThumbsDownCount()).isEqualTo(0L);
        assertThat(updateBoard.getMember()).isEqualTo(member);
        assertThat(updateBoard.getReplies().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 조회 시, 페이징 테스트")
    void paging_test() {
        Member member = Member.builder().username("test user").build();
        Board board1 = Board.builder()
                .title("test board title 1")
                .contents("test board contents 1")
                .member(member)
                .build();
        Board board2 = Board.builder()
                .title("test board title 2")
                .contents("test board contents 2")
                .member(member)
                .build();
        Board board3 = Board.builder()
                .title("test board title 3")
                .contents("test board contents 3")
                .member(member)
                .build();
        Pageable pageable = PageRequest.of(0,2, Sort.Direction.DESC, "id");

        boardService.save(board1);
        boardService.save(board2);
        boardService.save(board3);

        memberRepository.save(member);
        Page<BoardRespDto> boardList = boardService.findAll(pageable);

        assertThat(boardList.getNumber()).isEqualTo(0);
        assertThat(boardList.getSize()).isEqualTo(2);
        assertThat(boardList.getTotalPages()).isEqualTo(2);
        assertThat(boardList.hasNext()).isTrue();
    }

    @Test
    @DisplayName("없는 게시글 조회에 대한 예외 처리")
    void notExistsBoardId() {
        Long id = -1L;

        assertThatThrownBy(() -> boardService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The board can't be found");
    }


}