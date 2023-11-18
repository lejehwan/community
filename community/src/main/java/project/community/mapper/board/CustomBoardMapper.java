package project.community.mapper.board;

import org.mapstruct.Mapper;
import project.community.dto.board.BoardReqDto;
import project.community.dto.board.BoardRespDto;
import project.community.entity.board.Board;

/**
 * MapStruct 의 구현체 custom version
 * @author jeonghwanlee
 * @date 2023-11-13
 */
@Mapper(componentModel = "spring")
@Deprecated
public interface CustomBoardMapper {

    /**
     * mapping to Entity from Request Data
     * @date 2023-11-13
     * @author jeonghwanlee
     * @param reqDto request data
     * @return entity
     */
    default Board toEntity(BoardReqDto reqDto) {
        return Board.builder()
                .title(reqDto.getTitle())
                .contents(reqDto.getContents())
//                .viewCount(0L)
//                .thumbsUpCount(0L)
//                .thumbsDownCount(0L)
//                .isDelete(false)
                .member(reqDto.getMember())
                .build();
    }

    /**
     * mapping to Response Data from Entity
     * @date 2023-11-13
     * @author jeonghwanlee
     * @param board entity
     * @return response data
     */
    default BoardRespDto fromEntity(Board board) {
        return BoardRespDto.builder()
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
