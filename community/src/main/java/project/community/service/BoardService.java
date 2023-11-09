package project.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.community.dto.BoardReqDto;
import project.community.dto.BoardRespDto;
import project.community.entity.Board;
import project.community.repository.BoardRepository;

/**
 * @author jeonghwanlee
 * @date 2023-11-07
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public BoardRespDto findById(Long id) {
        return new BoardRespDto(boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The board can't be found")));
    }

    public Page<BoardRespDto> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardRespDto::new);
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public Board updateById(Long id, BoardReqDto boardReqDto) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The board can't be found"));
        board.update(boardReqDto);
        return boardRepository.save(board);
    }
}
