package project.community.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.community.dto.BoardReqDto;
import project.community.dto.BoardRespDto;
import project.community.entity.Board;

/**
 * MapStruct 를 통한 Object Mapping Interface<br>
 * 구현체 생성 경로 > src.main.generated.project.community.mapper.BoardMapperImpl
 * @author jeonghwanlee
 * @date 2023-11-13
 */
@Mapper(componentModel = "spring")// Mapper Bean 등록
public interface BoardMapper {

    /**
     * access to Mapper
     */
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);
    /**
     * mapping to Entity from Request Data
     * @date 2023-11-13
     * @author jeonghwanlee
     * @param reqDto request data
     * @return entity
     */
    Board toEntity(BoardReqDto reqDto);
    /**
     * mapping to Response Data from Entity
     * @date 2023-11-13
     * @author jeonghwanlee
     * @param board entity
     * @return response data
     */
    BoardRespDto fromEntity(Board board);

}
