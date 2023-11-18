package project.community.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.community.entity.Member;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jeonghwanlee
 * @date 2023-11-06
 */
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("사용자 기본적인 CRUD 테스트")
    void basicCRUD() {
        Member member1 = Member.builder()
                .username("member1")
                .build();
        Member member2 = Member.builder()
                .username("member2")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId())
                .orElseThrow(IllegalArgumentException::new);
        Member findMember2 = memberRepository.findById(member2.getId())
                .orElseThrow(IllegalArgumentException::new);

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        long count = memberRepository.count();

        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        long deleteCount = memberRepository.count();

        assertThat(deleteCount).isEqualTo(1);
    }
}