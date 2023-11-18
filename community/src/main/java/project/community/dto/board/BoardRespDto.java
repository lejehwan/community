package project.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.community.entity.Member;
import project.community.entity.reply.Reply;

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
    private LocalDateTime deleteDate;
    private String createBy;
    private String lastModifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
    private Member member;
    private List<Reply> replies = new ArrayList<>();
}
