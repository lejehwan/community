package project.community.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The board can't be found"));
    }

    /**
     * 게시글 아이디로 조회한 데이터를 응답 데이터로 감싸 리턴
     * @date 2023-11-13
     * @author jeonghwanlee
     * @param id 게시글 아이디
     * @return 게시글 응답 데이터
     */
    public BoardRespDto findByIdToResp(Long id) {
        return new BoardRespDto(findById(id));
    }

    @Transactional(readOnly = true)
    public Page<BoardRespDto> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardRespDto::new);
    }

    public Board save(Board board) {
        if (StringUtils.isBlank(board.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }
        return boardRepository.save(board);
    }

    public Board updateById(Long id, BoardReqDto boardReqDto) {
        Board board = findById(id);
        board.update(boardReqDto);
        return boardRepository.save(board);
    }
}
