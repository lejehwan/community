package project.community.service.reply;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.community.dto.board.BoardReqDto;
import project.community.dto.reply.ReplyReqDto;
import project.community.entity.Member;
import project.community.entity.board.Board;
import project.community.entity.reply.Reply;
import project.community.mapper.reply.ReplyMapper;
import project.community.repository.MemberRepository;
import project.community.repository.reply.ReplyRepository;
import project.community.service.board.BoardService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
/**
 * @author jeonghwanlee
 * @date 2023-11-18
 */
@SpringBootTest
@Transactional
class ReplyServiceTest {

    @Autowired BoardService boardService;
    @Autowired MemberRepository memberRepository;
    @Autowired ReplyService replyService;
    @Autowired ReplyRepository replyRepository;

    /**
     * member 정보 세팅
     * @date 2023-11-18
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
     * @date 2023-11-18
     * @author jeonghwanlee
     * @return Board
     */
    Board setUpBoardReqDtoBuild() {
        Member member = setUpMember();
        BoardReqDto boardDto = BoardReqDto.builder()
                .title("test board title 1")
                .contents("test board contents 1")
                .member(member)
                .build();
        return boardService.save(boardDto);
    }

    /**
     * 기본 댓글 정보 세팅
     * @date 2023-11-19
     * @author jeonghwanlee
     * @param contents
     * @return ReplyReqDto
     */
    ReplyReqDto setUpReplyReqDtoBuild(String contents) {
        Member member = setUpMember();
        Board board = setUpBoardReqDtoBuild();
        return ReplyReqDto.builder()
                .contents(contents)
                .member(member)
                .board(board)
                .build();
    }

    @Test
    @DisplayName("댓글 등록 시, 저장 확인")
    void checkDefaultValueBySave() throws InterruptedException {
        String contents = "test reply contents 1";
        ReplyReqDto replyReqDto = setUpReplyReqDtoBuild(contents);
        LocalDateTime now = LocalDateTime.now();

        Thread.sleep(1000);
        Reply savedReply = replyService.save(replyReqDto);

        assertThat(savedReply.getContents()).isEqualTo(contents);
        assertThat(savedReply.getMember()).isEqualTo(replyReqDto.getMember());
        assertThat(savedReply.getBoard()).isEqualTo(replyReqDto.getBoard());
        assertThat(savedReply.getCreateBy()).isNotBlank();
        assertThat(savedReply.getCreateDate()).isAfter(now);
        assertThat(savedReply.getLastModifiedDate()).isAfter(now);
        assertThat(savedReply.getLastModifiedBy()).isNotBlank();
        assertThat(savedReply.getThumbsDownCount()).isEqualTo(0);
        assertThat(savedReply.getThumbsUpCount()).isEqualTo(0);
        assertThat(savedReply.isDelete()).isFalse();
    }

    @Test
    @DisplayName("내용이 없는 댓글을 등록하는 경우 예외 처리 - (null & \"\" & \" \")")
    void checkContentsBySave() {
        String contents = null;
        ReplyReqDto replyReqDto = setUpReplyReqDtoBuild(contents);

        assertThatThrownBy(() -> replyService.save(replyReqDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Contents is required");
    }

    @Test
    @DisplayName("존재하지 않는 댓글을 조회하는 경우 예외처리")
    void notExistsReplyId() {
        Long id = -1L;

        assertThatThrownBy(() -> replyService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The reply can't be found");
    }

    @Test
    @DisplayName("")
    void checkParentAndChild() {

    }

    @Test
    @DisplayName("댓글 수정 시, 변경 확인")
    void checkDefaultValue() throws InterruptedException {
        String contents = "test reply contents 1";
        ReplyReqDto replyReqDto = setUpReplyReqDtoBuild(contents);
        LocalDateTime now = LocalDateTime.now();
        Thread.sleep(1000);
        Reply savedReply = replyService.save(replyReqDto);

        String updateContents = "test update reply contents 1";
        ReplyReqDto updateReplyReqDto = ReplyReqDto.builder()
                .contents(updateContents)
                .thumbsUpCount(2L)
                .thumbsDownCount(1L)
                .build();
        Thread.sleep(1000);

        Reply updateReply = replyService.updateById(savedReply.getId(), updateReplyReqDto);

        assertThat(updateReply).isEqualTo(savedReply);
        assertThat(updateReply.getContents()).isEqualTo(updateContents);
        assertThat(updateReply.getThumbsUpCount()).isEqualTo(2L);
        assertThat(updateReply.getThumbsDownCount()).isEqualTo(1L);
        replyRepository.flush(); // 수정시간과 생성 시간 확인을 위한 DB와 동기화 작업
        assertThat(updateReply.getCreateDate()).isAfter(now);
        assertThat(updateReply.getLastModifiedDate()).isAfter(now);
        assertThat(updateReply.getMember()).isEqualTo(savedReply.getMember());
        assertThat(updateReply.getBoard()).isEqualTo(savedReply.getBoard());
    }

    @Test
    @DisplayName("내용이 없는 댓글을 수정하는 경우 예외 처리 - (null & \"\" & \" \")")
    void checkTitleByUpdate() {
        String contents = "test reply contents 1";
        ReplyReqDto replyReqDto = setUpReplyReqDtoBuild(contents);
        Reply savedReply = replyService.save(replyReqDto);
        String updateContents = null;
        ReplyReqDto updateReplyReqDto = ReplyReqDto.builder()
                .contents(updateContents)
                .thumbsUpCount(2L)
                .thumbsDownCount(1L)
                .build();

        assertThatThrownBy(() -> replyService.updateById(savedReply.getId(), updateReplyReqDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Contents is required");
    }

    @Test
    @DisplayName("아이디가 없는 댓글을 수정하는 경우 예외처리")
    void checkIdByUpdate() {
        String updateContents = "test update board contents 1";
        ReplyReqDto replyDto = setUpReplyReqDtoBuild(updateContents);

        assertThatThrownBy(() -> replyService.updateById(-1L, replyDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The reply can't be found");
    }

    @Test
    @DisplayName("댓글 삭제 시, 삭제 확인")
    void checkDeleteById() {
        String contents = "test board contents 1";
        ReplyReqDto replyDto = setUpReplyReqDtoBuild(contents);
        Reply savedReply = replyService.save(replyDto);

        replyService.deleteById(savedReply.getId());
        Reply deletedReply = replyService.findById(savedReply.getId());

        assertThat(deletedReply).isEqualTo(savedReply);
        assertThat(deletedReply.isDelete()).isTrue();
    }
}