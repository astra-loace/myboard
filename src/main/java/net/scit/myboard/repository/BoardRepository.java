package net.scit.myboard.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import net.scit.myboard.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	// 검색 기능을 위한 JPA 쿼리 메서드 선언
	List<BoardEntity> findByBoardTitleContains(String searchWord, Sort by);

	List<BoardEntity> findByBoardWriterContains(String searchWord, Sort by);

	List<BoardEntity> findByBoardContentContains(String searchWord, Sort by);


}
