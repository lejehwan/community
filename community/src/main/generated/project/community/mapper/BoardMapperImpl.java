package project.community.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.community.dto.BoardReqDto;
import project.community.dto.BoardRespDto;
import project.community.entity.Board;
import project.community.entity.Reply;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-13T23:49:52+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public Board toEntity(BoardReqDto reqDto) {
        if ( reqDto == null ) {
            return null;
        }

        Board.BoardBuilder board = Board.builder();

        board.title( reqDto.getTitle() );
        board.contents( reqDto.getContents() );
        board.viewCount( reqDto.getViewCount() );
        board.thumbsUpCount( reqDto.getThumbsUpCount() );
        board.thumbsDownCount( reqDto.getThumbsDownCount() );
        board.member( reqDto.getMember() );

        return board.build();
    }

    @Override
    public BoardRespDto fromEntity(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardRespDto.BoardRespDtoBuilder boardRespDto = BoardRespDto.builder();

        boardRespDto.id( board.getId() );
        boardRespDto.title( board.getTitle() );
        boardRespDto.contents( board.getContents() );
        boardRespDto.viewCount( board.getViewCount() );
        boardRespDto.thumbsUpCount( board.getThumbsUpCount() );
        boardRespDto.thumbsDownCount( board.getThumbsDownCount() );
        boardRespDto.createBy( board.getCreateBy() );
        boardRespDto.lastModifiedBy( board.getLastModifiedBy() );
        boardRespDto.createDate( board.getCreateDate() );
        boardRespDto.lastModifiedDate( board.getLastModifiedDate() );
        boardRespDto.member( board.getMember() );
        List<Reply> list = board.getReplies();
        if ( list != null ) {
            boardRespDto.replies( new ArrayList<Reply>( list ) );
        }

        return boardRespDto.build();
    }
}
