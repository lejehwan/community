package project.community.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.community.entity.Member;

/**
 * @author jeonghwanlee
 * @date 2023-11-08
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardReqDto {

    private String title;
    private String contents;
    private Long viewCount;
    private Long thumbsUpCount;
    private Long thumbsDownCount;
    private Member member;
}
