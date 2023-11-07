package project.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.community.entity.Board;
import project.community.entity.Member;
import project.community.entity.Reply;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeonghwanlee
 * @date 2023-11-06
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private Long id;// 번호
    private String title;// 제목
    private String contents;// 내용
    private Long viewCount;// 조회 수
    private Long thumbsUpCount;// 좋아요
    private Long thumbsDownCount;// 싫어요
    private boolean isDelete;// 삭제 여부
    private String createBy;// 등록자
    private String lastModifiedBy;// 수정자
    private LocalDateTime createDate;// 등록 일시
    private LocalDateTime lastModifiedDate;// 수정 일시
    private Member member;
    private List<Reply> replies = new ArrayList<>();

    public BoardDto(Board board) {
        BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .contents(board.getContents())
                .viewCount(board.getViewCount())
                .thumbsUpCount(board.getThumbsUpCount())
                .thumbsDownCount(board.getThumbsDownCount())
                .isDelete(board.isDelete())
                .createBy(board.getCreateBy())
                .createDate(board.getCreateDate())
                .lastModifiedBy(board.getLastModifiedBy())
                .lastModifiedDate(board.getLastModifiedDate())
                .member(board.getMember())
                .replies(board.getReplies())
                .build();
    }
}
