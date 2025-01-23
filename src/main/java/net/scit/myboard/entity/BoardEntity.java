package net.scit.myboard.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.myboard.dto.BoardDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Builder

@Entity
@Table(name="myboard")
@EntityListeners(AuditingEntityListener.class) // @LastModifiedDate --> 2
public class BoardEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="board_seq")
	private Long boardSeq;
	
	@Column(name="board_writer", nullable = false)
	private String boardWriter;
	
	@Column(name="board_title")
	private String boardTitle;
	
	@Column(name="board_content")
	private String boardContent;
	
	@Column(name="hit_count")
	private int hitCount;
	
	@Column(name="create_date")
	@CreationTimestamp
	private LocalDateTime createDate;
	
	@Column(name="update_date")
	@LastModifiedDate
	private LocalDateTime updateDate;	
	
	@Formula("(SELECT count(1) FROM myboard_reply r WHERE board_seq = r.board_seq)")
	private int replyCount;
	
	public static BoardEntity toEntity(BoardDTO boardDTO) {
		return BoardEntity.builder()
				.boardSeq(boardDTO.getBoardSeq())
				.boardWriter(boardDTO.getBoardWriter())
				.boardTitle(boardDTO.getBoardTitle())
				.boardContent(boardDTO.getBoardContent())
				.hitCount(boardDTO.getHitCount())
				.createDate(boardDTO.getCreateDate())
				.updateDate(boardDTO.getUpdateDate())
				.build();
		
	}

}
