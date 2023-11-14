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

    /**
     * member 정보 세팅
     * @date 2023-11-15
     * @author jeonghwanlee
     * @return Member
     */
    Member setUpMember() {
        Member member = Member.builder().username("test user").build();
        return memberRepository.save(member);
    }

    /**
     * 기본 게시글 정보 세팅 <br>
     * params[0]: title <br>
     * params[1]: contents <br>
     * @date 2023-11-15
     * @author jeonghwanlee
     * @param params
     * @return BoardReqDto
     */
    BoardReqDto setUpBoardReqDtoBuild(String ...params) {
        Member member = setUpMember();
        return BoardReqDto.builder()
                .title(params[0])
                .contents(params[1])
                .member(member)
                .build();
    }

    @Test
    @DisplayName("게시글 등록 시, 저장 확인")
    void checkDefaultValueBySave() throws InterruptedException {
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = setUpBoardReqDtoBuild(title, contents);
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);

        Board savedBoard = boardService.save(boardDto);

        assertThat(savedBoard.getTitle()).isEqualTo(title);
        assertThat(savedBoard.getContents()).isEqualTo(contents);
        assertThat(savedBoard.getMember()).isEqualTo(boardDto.getMember());
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
        String title = null;
        String contents = "test board contents 1";
        BoardReqDto boardDto = setUpBoardReqDtoBuild(title, contents);

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
        String title1 = "test board title 1";
        String contents1 = "test board contents 1";
        String title2 = "test board title 2";
        String contents2 = "test board contents 2";
        String title3 = "test board title 3";
        String contents3 = "test board contents 3";
        BoardReqDto boardDto1 = setUpBoardReqDtoBuild(title1, contents1);
        BoardReqDto boardDto2 = setUpBoardReqDtoBuild(title2, contents2);
        BoardReqDto boardDto3 = setUpBoardReqDtoBuild(title3, contents3);
        Pageable pageable = PageRequest.of(0,2, Sort.Direction.DESC, "id");
        boardService.save(boardDto1);
        boardService.save(boardDto2);
        boardService.save(boardDto3);

        Page<BoardRespDto> boardList = boardService.findAll(pageable);

        assertThat(boardList.getNumber()).isEqualTo(0);
        assertThat(boardList.getSize()).isEqualTo(2);
        assertThat(boardList.getTotalPages()).isEqualTo(2);
        assertThat(boardList.hasNext()).isTrue();
    }

    @Test
    @DisplayName("게시글 수정 시, 변경 확인")
    void checkDefaultValue() throws InterruptedException {
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = setUpBoardReqDtoBuild(title, contents);
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
                .member(boardDto.getMember())
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
        assertThat(updateBoard.getMember()).isEqualTo(boardDto.getMember());
    }

    @Test
    @DisplayName("제목이 없는 게시글을 수정하는 경우 예외 처리 - (null & \"\" & \" \")")
    void checkTitleByUpdate() {
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = setUpBoardReqDtoBuild(title, contents);
        Board savedBoard = boardService.save(boardDto);
        String updateTitle = null;
        BoardReqDto boardReqDto = BoardReqDto.builder()
                .title(updateTitle)
                .contents("")
                .member(boardDto.getMember())
                .build();

        assertThatThrownBy(() -> boardService.updateById(savedBoard.getId(), boardReqDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title is required");
    }

    @Test
    @DisplayName("아이디가 없는 게시글을 수정하는 경우 예외처리")
    void checkIdByUpdate() {
        String updateTitle = "test update board title 1";
        String updateContents = "test update board contents 1";
        BoardReqDto boardDto = setUpBoardReqDtoBuild(updateTitle, updateContents);

        assertThatThrownBy(() -> boardService.updateById(-1L, boardDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The board can't be found");
    }

    @Test
    @DisplayName("게시글 삭제 시, 삭제 확인")
    void checkDeleteById() {
        String title = "test board title 1";
        String contents = "test board contents 1";
        BoardReqDto boardDto = setUpBoardReqDtoBuild(title, contents);
        Board savedBoard = boardService.save(boardDto);

        boardService.deleteById(savedBoard.getId());
        Board deletedBoard = boardService.findById(savedBoard.getId());

        assertThat(deletedBoard).isEqualTo(savedBoard);
        assertThat(deletedBoard.isDelete()).isTrue();
    }


}