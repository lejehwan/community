package project.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.community.entity.Member;
import project.community.entity.Reply;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isDelete;
    private Member member;
    private List<Reply> replies = new ArrayList<>();
}
