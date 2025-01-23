package net.scit.myboard.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.scit.myboard.dto.BoardDTO;
import net.scit.myboard.entity.BoardEntity;
import net.scit.myboard.repository.BoardRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
	private final BoardRepository boardRepository;
	/**
	 * 1) 단순 조회: 게시글 목록 전체 조회
	 * 2) 검색 조회: searchItem과 searchWord를 통해 Query Method 사용
	 * @param searchItem 
	 * @param searchWord 
	 * @return
	 */	
	public List<BoardDTO> selectAll(String searchItem, String searchWord) {
		// 1) 단순 조회
//		List<BoardEntity> temp = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));	

		// 2) 검색 조회
		List<BoardEntity> temp = null;
		
		switch (searchItem) {
		case "boardTitle": 
			temp = boardRepository.findByBoardTitleContains(searchWord, Sort.by(Sort.Direction.DESC, "createDate"));
			break;
		case "boardWriter":
			temp = boardRepository.findByBoardWriterContains(searchWord, Sort.by(Sort.Direction.DESC, "createDate"));
			break;
		case "boardContent":
			temp = boardRepository.findByBoardContentContains(searchWord, Sort.by(Sort.Direction.DESC, "createDate"));
			break;
		}		
				
		List<BoardDTO> list = null;
		
		// 2) Lambda 객체, Stream : List, Set, Map (등등 데이터 묶음을 처리하기 위한 용도!) 
		
		List<BoardDTO> dtoList = new ArrayList<>();
		
		temp.forEach((entity) -> BoardDTO.toDTO(entity));
		
		return list;
		
	}
	
	/**
	 * 전달받은 BoardDTO를 DB에 저장
	 * 
	 * @param boardDTO
	 */
	public void insertBoard(BoardDTO boardDTO) {
		BoardEntity entity = BoardEntity.toEntity(boardDTO);
		boardRepository.save(entity);
	}

	/**
	 * boardSeq에 해당하는 글을 조회
	 * 
	 * @param boardSeq
	 * @return
	 */
	public BoardDTO selectOne(Long boardSeq) {
		Optional<BoardEntity> temp = boardRepository.findById(boardSeq);

		if (!temp.isPresent())
			return null;

		return BoardDTO.toDTO(temp.get());
	}

	/**
	 * boardSeq에 대한 조회수 증가
	 * save() : 데이터를 다른 값으로 바꾸는 메서드 
	 * update from board set hit_count = hit_count + 1 where board_seq = ?;
	 * 
	 * jpa에는 update 메서드가 없다. 
	 * 1) 조회 : findById(boardSeq) 
	 * 2) hitCount를 getter로 가져온 후 + 1 
	 * 3) 변경된 값을 다시 setter를 통해 삽입 
	 * @param boardSeq
	 */
	@Transactional
	public void incrementHitCount(Long boardSeq) {
		Optional<BoardEntity> temp = boardRepository.findById(boardSeq);
		if(temp.isEmpty()) return;
		BoardEntity entity = temp.get();
		entity.setHitCount((entity.getHitCount() + 1));
	}
	
	/**
	 * DB에 게시글을 삭제 
	 * @param boardSeq
	 */
	@Transactional
	public void deleteOne(Long boardSeq) {
		Optional<BoardEntity> temp = boardRepository.findById(boardSeq);
		
		//이건 근데 글만 삭제하는 거고...
		if(temp.isEmpty()) return;
			
		boardRepository.deleteById(boardSeq);
		
	
	}

	/**
	 * DB에 수정 처리 
	 * @param boardDTO
	 */
	@Transactional
	public void updateBoard(BoardDTO boardDTO) {

				
		// 1. 수정하려는 데이터가 있는지 확인 
		Optional<BoardEntity> temp = boardRepository.findById(boardDTO.getBoardSeq());
		
		// 2. 없으면 return
		if(!temp.isPresent()) return;
		
		// 3. 있으면 dto -> entity로 변환 
		BoardEntity entity = temp.get();

		// 4. 저장(update) 
		entity.setBoardTitle(boardDTO.getBoardTitle());
		entity.setBoardContent(boardDTO.getBoardContent());
		entity.setUpdateDate(LocalDateTime.now());

	}
	

}
