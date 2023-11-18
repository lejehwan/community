package project.community.service.reply;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.community.dto.board.BoardReqDto;
import project.community.dto.board.BoardRespDto;
import project.community.dto.reply.ReplyReqDto;
import project.community.dto.reply.ReplyRespDto;
import project.community.entity.board.Board;
import project.community.entity.reply.Reply;
import project.community.mapper.reply.ReplyMapper;
import project.community.repository.reply.ReplyRepository;

/**
 * @author jeonghwanlee
 * @date 2023-11-18
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ReplyMapper replyMapper;

    public Reply save(ReplyReqDto reqDto) {
        if (StringUtils.isBlank(reqDto.getContents())) {
            throw new IllegalArgumentException("Contents is required");
        }
        return replyRepository.save(replyMapper.toEntity(reqDto));
    }

    @Transactional(readOnly = true)
    public Reply findById(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("The reply can't be found"));
    }

    @Transactional(readOnly = true)
    public Page<ReplyRespDto> findAll(Pageable pageable) {
        return replyRepository.findAll(pageable).map(replyMapper::fromEntity);
    }

    public Reply updateById(Long id, ReplyReqDto replyReqDto) {
        if (StringUtils.isBlank(replyReqDto.getContents())) {
            throw new IllegalArgumentException("Contents is required");
        }
        Reply findReply = findById(id);
        findReply.update(replyReqDto);
        return findReply;
    }

    public void deleteById(Long id) {
        Reply findReply = findById(id);
        findReply.delete();
    }
}
