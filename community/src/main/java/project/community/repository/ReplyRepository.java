package project.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.community.entity.Reply;

/**
 * @author jeonghwanlee
 * @date 2023-10-19
 */
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
