package project.community.service.board;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.community.dto.board.BoardReqDto;
import project.community.dto.board.BoardRespDto;
import project.community.entity.board.Board;
import project.community.mapper.board.BoardMapper;
import project.community.repository.board.BoardRepository;

/**
 * @author jeonghwanlee
 * @date 2023-11-07
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public Board save(BoardReqDto boardDto) {
        if (StringUtils.isBlank(boardDto.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }
        return boardRepository.save(boardMapper.toEntity(boardDto));
    }

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The board can't be found"));
        board.increaseViewCount();
        return board;
    }

    @Transactional(readOnly = true)
    public Page<BoardRespDto> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable).map(boardMapper::fromEntity);
    }

    public Board updateById(Long id, BoardReqDto boardReqDto) {
        if (StringUtils.isBlank(boardReqDto.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }
        Board findBoard = findById(id);
        findBoard.update(boardReqDto);
        return findBoard;
    }

    public void deleteById(Long id) {
        Board findBoard = findById(id);
        findBoard.delete();
    }

}
