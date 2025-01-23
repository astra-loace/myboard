package net.scit.myboard.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.scit.myboard.dto.BoardDTO;
import net.scit.myboard.service.BoardService;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
	private final BoardService boardService;

	/**
	 * 게시글 목록 요청
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/boardList")
	public String boardList(
			@RequestParam(name="searchItem", defaultValue = "boardTitle") String searchItem,
			@RequestParam(name="searchWord", defaultValue = "") String searchWord,
			Model model) {
//		log.info("== searchWord: {}", searchWord);
//		log.info("== searchItem: {}", searchItem);
		
		List<BoardDTO> list = boardService.selectAll(searchItem, searchWord);
		model.addAttribute("list", list);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		
		return "board/boardList";
	}

	/**
	 * 게시글 쓰기 화면 요청
	 * 
	 * @return
	 */
	@GetMapping("/boardWrite")
	public String boardWrite() {
		return "board/boardWrite";
	}

	/**
	 * 게시글 등록 요청
	 * 
	 * @param boardDTO
	 * @return
	 */
	@PostMapping("/boardWrite")
	public String boardWrite(@ModelAttribute BoardDTO boardDTO) {
		// DB 등록
//		log.info("========= {}", boardDTO.toString());
		boardService.insertBoard(boardDTO);
		return "redirect:/board/boardList";
	}

	/**
	 * 게시글 상세 보기 화면 요청 & 조회수 증가
	 * 
	 * @param boardSeq
	 * @param model
	 * @return
	 */
	@GetMapping("/boardDetail")
	public String boardDetail(
			@RequestParam(name="searchItem", defaultValue = "boardTitle") String searchItem,
			@RequestParam(name="searchWord", defaultValue = "") String searchWord,
			@RequestParam(name = "boardSeq") Long boardSeq, Model model) {
		// DB에 boardSeq에 해당하는 하나의 게시글을 조회
		BoardDTO boardDTO = boardService.selectOne(boardSeq);
		boardService.incrementHitCount(boardSeq);

		model.addAttribute("board", boardDTO);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		
		return "board/boardDetail";
	}

	/**
	 * 게시글 삭제
	 * 
	 * @param boardSeq
	 * @return
	 */
	@GetMapping("/boardDelete")
	public String boardDelete(
			@RequestParam(name="searchItem", defaultValue = "boardTitle") String searchItem,
			@RequestParam(name="searchWord", defaultValue = "") String searchWord,
			@RequestParam(name = "boardSeq") Long boardSeq,
			RedirectAttributes reat) {
		boardService.deleteOne(boardSeq);
		reat.addAttribute("searchItem", searchItem);
		reat.addAttribute("searchWord", searchWord);
		return "redirect:/board/boardList";
	}

	/**
	 * 수정을 위한 화면 요청
	 * 
	 * @param boardSeq
	 * @param model
	 * @return
	 */
	@GetMapping("/boardUpdate")
	public String boardUpdate(
			@RequestParam(name="searchItem", defaultValue = "boardTitle") String searchItem,
			@RequestParam(name="searchWord", defaultValue = "") String searchWord,
			@RequestParam(name = "boardSeq") Long boardSeq, Model model) {
		BoardDTO boardDTO = boardService.selectOne(boardSeq);
		model.addAttribute("board", boardDTO);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		return "board/boardUpdate";
	}

	/**
	 * 게시글 수정 처리 요청 
	 * 
	 * @param boardDTO
	 * @return
	 */
	@PostMapping("/boardUpdate")
	public String boardUpdate(
			@RequestParam(name="searchItem", defaultValue = "boardTitle") String searchItem,
			@RequestParam(name="searchWord", defaultValue = "") String searchWord,
			@ModelAttribute BoardDTO boardDTO, 
			RedirectAttributes reat) {
		log.info("==== 수정데이터: {}", boardDTO.toString());
		boardService.updateBoard(boardDTO);
		reat.addAttribute("searchItem", searchItem);
		reat.addAttribute("searchWord", searchWord);
		return "redirect:/board/boardList";
	}
}
