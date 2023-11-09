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
public class BoardRespDto {

    private Long id;
    private String title;
    private String contents;
    private Long viewCount;
    private Long thumbsUpCount;
    private Long thumbsDownCount;
    private boolean isDelete;
    private String createBy;
    private String lastModifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private Member member;
    private List<Reply> replies = new ArrayList<>();

    public BoardRespDto(Board board) {
        BoardRespDto.builder()
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
