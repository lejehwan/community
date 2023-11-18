package project.community.dto.reply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.community.entity.Member;
import project.community.entity.reply.Reply;
import project.community.entity.board.Board;

/**
 * @author jeonghwanlee
 * @date 2023-11-18
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyReqDto {

    private String contents;
    private Long thumbsUpCount;
    private Long thumbsDownCount;
    private Reply parent;
    private Member member;
    private Board board;
}
