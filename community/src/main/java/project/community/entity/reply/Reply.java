package project.community.entity.reply;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import project.community.dto.board.BoardReqDto;
import project.community.dto.reply.ReplyReqDto;
import project.community.entity.Member;
import project.community.entity.board.Board;
import project.community.entity.common.BaseEntity;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author jeonghwanlee
 * @date 2023-10-15
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reply_id")
    private Long id;// 번호

    @Column(nullable = false)
    private String contents;

    @ColumnDefault(value = "0")
    private Long thumbsUpCount;// 좋아요

    @ColumnDefault(value = "0")
    private Long thumbsDownCount;// 싫어요

    @ColumnDefault(value = "false")
    private boolean isDelete;// 삭제 여부

    private LocalDateTime deleteDate;// 삭제 일시

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent;

    @OneToMany(mappedBy = "parent")
    private List<Reply> child = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @PrePersist
    public void prePersist() {
        thumbsUpCount = 0L;
        thumbsDownCount = 0L;
        isDelete = false;
        deleteDate = null;
    }

    /**
     * 댓글 삭제 메소드
     * @date 2023-11-18
     * @author jeonghwanlee
     */
    public void delete() {
        this.isDelete = true;
        this.deleteDate = LocalDateTime.now();
    }

    /**
     * 댓글 수정 메소드
     * @date 2023-11-18
     * @author jeonghwanlee
     * @param replyReqDto
     */
    public void update(ReplyReqDto replyReqDto) {
        this.contents = replyReqDto.getContents();
        this.thumbsUpCount = replyReqDto.getThumbsUpCount();
        this.thumbsDownCount = replyReqDto.getThumbsDownCount();
        this.member = replyReqDto.getMember();
    }
}
