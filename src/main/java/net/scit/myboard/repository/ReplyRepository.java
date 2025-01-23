package net.scit.myboard.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import net.scit.myboard.entity.BoardEntity;
import net.scit.myboard.entity.ReplyEntity;


public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
	
	// 댓글 조회
	List<ReplyEntity> findByBoardEntity(Optional<BoardEntity> temp, Sort by);

}
