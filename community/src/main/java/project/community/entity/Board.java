package project.community.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import project.community.dto.BoardReqDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

/**
 * @author jeonghwanlee
 * @date 2023-10-15
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board extends BaseEntity{

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "borad_id")
    private Long id;// 번호

    @Column(nullable = false, length = 100)
    private String title;// 제목

    @Lob
    private String contents;// 내용

    @ColumnDefault(value = "0")
    private Long viewCount;// 조회 수

    @ColumnDefault(value = "0")
    private Long thumbsUpCount;// 좋아요

    @ColumnDefault(value = "0")
    private Long thumbsDownCount;// 싫어요

    @ColumnDefault(value = "false")
    private boolean isDelete;// 삭제 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Reply> replies = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        viewCount = 0L;
        thumbsUpCount = 0L;
        thumbsDownCount = 0L;
        isDelete = false;
    }

    @Builder
    public Board(String title, String contents, Member member) {
        this.title = title;
        this.contents = contents;
        this.member = member;
    }

    public void delete() {
        this.isDelete = true;
    }

    public void update(BoardReqDto boardReqDto) {
        this.title = boardReqDto.getTitle();
        this.contents = boardReqDto.getContents();
        this.viewCount = boardReqDto.getViewCount();
        this.thumbsUpCount = boardReqDto.getThumbsUpCount();
        this.thumbsDownCount = boardReqDto.getThumbsDownCount();
        this.isDelete = boardReqDto.isDelete();
        this.member = boardReqDto.getMember();
        this.replies = boardReqDto.getReplies();
    }
}
