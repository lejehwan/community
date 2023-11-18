package project.community.repository.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import project.community.entity.reply.Reply;

/**
 * @author jeonghwanlee
 * @date 2023-10-19
 */
public interface ReplyRepository extends JpaRepository<Reply, Long> {

}
