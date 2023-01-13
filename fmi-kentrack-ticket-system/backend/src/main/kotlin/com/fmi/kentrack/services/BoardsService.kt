package com.fmi.kentrack.services

import com.fmi.kentrack.jooq.tables.references.BOARD
import com.fmi.kentrack.model.Board
import org.springframework.stereotype.Service

@Service
class BoardsService : Base() {
    fun getAllBoards(): List<Board> = db.selectFrom(BOARD).fetchInto(Board::class.java)

    fun updateBoard(board: Board) {
        db.fetchOne(BOARD, BOARD.ID.eq(board.id.toBigDecimal()))?.apply {
            from(board)
        }?.update()
    }

    fun createBoard(newBoard: Board) {
        db.newRecord(BOARD).apply {
            from(newBoard)
        }.insert()

    }
}