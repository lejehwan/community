package project.community.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.community.entity.Reply;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jeonghwanlee
 * @date 2023-10-19
 */
@SpringBootTest
@Transactional
@Rollback(value = false)
class ReplyRepositoryTest {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @DisplayName("게시판 기본적인 CRUD 테스트")
    void basicCRUD() {
        Reply reply1 = Reply.builder()
                .contents("reply1")
                .build();
        Reply reply2 = Reply.builder()
                .contents("reply2")
                .build();
        replyRepository.save(reply1);
        replyRepository.save(reply2);

        Reply findReply1 = replyRepository.findById(reply1.getId())
                .orElseThrow(IllegalArgumentException::new);
        Reply findReply2 = replyRepository.findById(reply2.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findReply1).isEqualTo(reply1);
        assertThat(findReply2).isEqualTo(reply2);

        long count = replyRepository.count();

        assertThat(count).isEqualTo(2);

        replyRepository.delete(reply1);
        long deleteCount = replyRepository.count();

        assertThat(deleteCount).isEqualTo(1);
    }
}