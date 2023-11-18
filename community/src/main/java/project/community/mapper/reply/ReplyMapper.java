package project.community.mapper.reply;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.community.dto.reply.ReplyReqDto;
import project.community.dto.reply.ReplyRespDto;
import project.community.entity.reply.Reply;

/**
 * @author jeonghwanlee
 * @date 2023-11-18
 */
@Mapper(componentModel = "spring")
public interface ReplyMapper {

    ReplyMapper INSTANCE = Mappers.getMapper(ReplyMapper.class);

    /**
     * mapping to Entity from Request Data
     * @date 2023-11-18
     * @author jeonghwanlee
     * @param reqDto request data
     * @return entity
     */
    Reply toEntity(ReplyReqDto reqDto);
    /**
     * mapping to Response Data from Entity
     * @date 2023-11-18
     * @author jeonghwanlee
     * @param reply entity
     * @return response data
     */
    ReplyRespDto fromEntity(Reply reply);
}
