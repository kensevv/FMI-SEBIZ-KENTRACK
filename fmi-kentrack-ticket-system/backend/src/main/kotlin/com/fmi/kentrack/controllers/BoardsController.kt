package com.fmi.kentrack.controllers

import com.fmi.kentrack.model.Board
import com.fmi.kentrack.services.BoardsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/api")
class BoardsController {

    @Autowired
    private lateinit var boardsService: BoardsService

    @GetMapping("/get-all-boards")
    fun getAllBoards() = boardsService.getAllBoards()

    @PostMapping("/update-board")
    fun updateBoard(@RequestBody board: Board) = boardsService.updateBoard(board)

    @PostMapping("/create-new-board")
    fun createBoard(@RequestBody newBoard: Board) = boardsService.createBoard(newBoard)
}