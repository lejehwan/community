package project.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.community.entity.Board;

/**
 * @author jeonghwanlee
 * @date 2023-10-19
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
