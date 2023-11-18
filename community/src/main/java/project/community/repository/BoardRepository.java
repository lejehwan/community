package project.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.community.entity.board.Board;

/**
 * @author jeonghwanlee
 * @date 2023-10-19
 */
public interface BoardRepository extends JpaRepository<Board, Long> {

}
