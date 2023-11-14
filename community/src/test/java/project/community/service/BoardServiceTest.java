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
import project.community.mapper.BoardMapper;
import project.community.repository.BoardRepository;
import project.community.repository.MemberRepository;

import java.time.LocalDateTime;

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
    @Autowired BoardMapper boardMapper;

    @Test
    @DisplayName("게시글 등록 시, 저장 확인")
    @Rollback(value = false)
    void checkDefaultValueBySave() throws InterruptedException {
        Member member = Member.builder().username("test user").build();
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = BoardReqDto.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();
        memberRepository.save(member);
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);

        Board savedBoard = boardService.save(boardDto);

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
    @DisplayName("제목이 없는 게시글을 등록하는 경우 예외 처리 - (null & \"\" & \" \")")
    void checkTitleBySave() {
        Member member = Member.builder().username("test user").build();
        String title = null;
        String contents = "test board contents 1";
        BoardReqDto boardDto = BoardReqDto.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();
        memberRepository.save(member);

        assertThatThrownBy(() -> boardService.save(boardDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title is required");
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하는 경우 예외처리")
    void notExistsBoardId() {
        Long id = -1L;

        assertThatThrownBy(() -> boardService.findByIdToResp(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The board can't be found");
    }

    @Test
    @DisplayName("게시글 조회 시, 페이징 테스트")
    void pagingTest() {
        Member member = Member.builder().username("test user").build();
        BoardReqDto board1 = BoardReqDto.builder()
                .title("test board title 1")
                .contents("test board contents 1")
                .member(member)
                .build();
        BoardReqDto board2 = BoardReqDto.builder()
                .title("test board title 2")
                .contents("test board contents 2")
                .member(member)
                .build();
        BoardReqDto board3 = BoardReqDto.builder()
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
    @DisplayName("게시글 수정 시, 변경 확인")
    void checkDefaultValue() throws InterruptedException {
        Member member = Member.builder().username("test user").build();
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = BoardReqDto.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();
        memberRepository.save(member);
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);
        Board savedBoard = boardService.save(boardDto);
        String updateTitle = "test update board title 1";
        String updateContents = "test update board contents 1";
        BoardReqDto boardReqDto = BoardReqDto.builder()
                .title(updateTitle)
                .contents(updateContents)
                .viewCount(3L)
                .thumbsUpCount(2L)
                .thumbsDownCount(0L)
                .member(member)
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
    }

    @Test
    @DisplayName("제목이 없는 게시글을 수정하는 경우 예외 처리 - (null & \"\" & \" \")")
    void checkTitleByUpdate() {
        Member member = Member.builder().username("test user").build();
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = BoardReqDto.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();
        memberRepository.save(member);
        Board savedBoard = boardService.save(boardDto);
        String updateTitle = null;
        BoardReqDto boardReqDto = BoardReqDto.builder()
                .title(updateTitle)
                .contents("")
                .viewCount(3L)
                .thumbsUpCount(2L)
                .thumbsDownCount(0L)
                .member(member)
                .build();

        assertThatThrownBy(() -> boardService.updateById(savedBoard.getId(), boardReqDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title is required");
    }

    @Test
    @DisplayName("아이디가 없는 게시글을 수정하는 경우 예외처리")
    void checkIdByUpdate() {
        Member member = Member.builder().username("test user").build();
        String updateTitle = "test update board title 1";
        String updateContents = "test update board contents 1";
        BoardReqDto boardReqDto = BoardReqDto.builder()
                .title(updateTitle)
                .contents(updateContents)
                .viewCount(3L)
                .thumbsUpCount(2L)
                .thumbsDownCount(0L)
                .member(member)
                .build();

        assertThatThrownBy(() -> boardService.updateById(-1L, boardReqDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The board can't be found");
    }

    @Test
    @DisplayName("게시글 삭제 시, 삭제 확인")
    void checkDeleteById() {
        Member member = Member.builder().username("test user").build();
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = BoardReqDto.builder()
                .title(title)
                .contents(contents)
                .member(member)
                .build();
        memberRepository.save(member);
        Board savedBoard = boardService.save(boardDto);

        boardService.deleteById(savedBoard.getId());
        Board deletedBoard = boardService.findById(savedBoard.getId());

        assertThat(deletedBoard).isEqualTo(savedBoard);
        assertThat(deletedBoard.isDelete()).isTrue();
    }


}