package project.community.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.community.entity.board.Board;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author jeonghwanlee
 * @date 2023-10-19
 */
@SpringBootTest
@Transactional
@Rollback(value = false)
class BoardRepositoryTest {

    @Autowired BoardRepository boardRepository;

    @Test
    @DisplayName("게시판 기본적인 CRUD 테스트")
    void basicCRUD() {
        Board board1 = Board.builder()
                .title("board1")
                .build();
        Board board2 = Board.builder()
                .title("board2")
                .build();
        boardRepository.save(board1);
        boardRepository.save(board2);

        Board findBoard1 = boardRepository.findById(board1.getId())
                .orElseThrow(IllegalArgumentException::new);
        Board findBoard2 = boardRepository.findById(board2.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findBoard1).isEqualTo(board1);
        assertThat(findBoard2).isEqualTo(board2);

        long count = boardRepository.count();

        assertThat(count).isEqualTo(2);

        boardRepository.delete(board1);
        long deleteCount = boardRepository.count();

        assertThat(deleteCount).isEqualTo(1);
    }

}