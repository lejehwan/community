package project.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.community.entity.Member;

/**
 * @author jeonghwanlee
 * @date 2023-11-06
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
