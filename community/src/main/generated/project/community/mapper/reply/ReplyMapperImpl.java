package project.community.mapper.reply;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import project.community.dto.reply.ReplyReqDto;
import project.community.dto.reply.ReplyRespDto;
import project.community.entity.reply.Reply;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-18T21:55:32+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class ReplyMapperImpl implements ReplyMapper {

    @Override
    public Reply toEntity(ReplyReqDto reqDto) {
        if ( reqDto == null ) {
            return null;
        }

        Reply.ReplyBuilder reply = Reply.builder();

        reply.contents( reqDto.getContents() );
        reply.thumbsUpCount( reqDto.getThumbsUpCount() );
        reply.thumbsDownCount( reqDto.getThumbsDownCount() );
        reply.parent( reqDto.getParent() );
        reply.member( reqDto.getMember() );
        reply.board( reqDto.getBoard() );

        return reply.build();
    }

    @Override
    public ReplyRespDto fromEntity(Reply reply) {
        if ( reply == null ) {
            return null;
        }

        ReplyRespDto.ReplyRespDtoBuilder replyRespDto = ReplyRespDto.builder();

        replyRespDto.id( reply.getId() );
        replyRespDto.contents( reply.getContents() );
        replyRespDto.thumbsUpCount( reply.getThumbsUpCount() );
        replyRespDto.thumbsDownCount( reply.getThumbsDownCount() );
        replyRespDto.deleteDate( reply.getDeleteDate() );
        replyRespDto.parent( reply.getParent() );
        List<Reply> list = reply.getChild();
        if ( list != null ) {
            replyRespDto.child( new ArrayList<Reply>( list ) );
        }
        replyRespDto.createBy( reply.getCreateBy() );
        replyRespDto.lastModifiedBy( reply.getLastModifiedBy() );
        replyRespDto.createDate( reply.getCreateDate() );
        replyRespDto.lastModifiedDate( reply.getLastModifiedDate() );
        replyRespDto.member( reply.getMember() );
        replyRespDto.board( reply.getBoard() );

        return replyRespDto.build();
    }
}
