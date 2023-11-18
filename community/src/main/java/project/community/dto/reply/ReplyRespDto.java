package project.community.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.community.entity.Member;
import project.community.entity.reply.Reply;
import project.community.entity.board.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeonghwanlee
 * @date 2023-11-18
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRespDto {

    private Long id;
    private String contents;
    private Long thumbsUpCount;
    private Long thumbsDownCount;
    private boolean isDelete;
    private LocalDateTime deleteDate;
    private Reply parent;
    private List<Reply> child = new ArrayList<>();
    private String createBy;
    private String lastModifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private Member member;
    private Board board;
}
