package com.fmi.kentrack.services

import com.fmi.kentrack.jooq.tables.records.BoardRecord
import com.fmi.kentrack.jooq.tables.records.UserRecord
import com.fmi.kentrack.jooq.tables.references.BOARD
import com.fmi.kentrack.jooq.tables.references.USER
import com.fmi.kentrack.model.Board
import com.fmi.kentrack.users.UserService
import com.fmi.kentrack.users.mapToInternalModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class BoardsService : BaseService() {
    @Autowired
    private lateinit var userService: UserService

    fun getBoardById(id: BigDecimal): Board = db
        .select(BOARD.asterisk(), USER.asterisk())
        .from(BOARD)
        .leftJoin(USER)
        .on(BOARD.OWNER_USERNAME.eq(USER.USERNAME))
        .where(BOARD.ID.eq(id))
        .fetchOne()
        ?.map { mapToInternalModel(it) }!!

    fun getAllBoards(): List<Board> = db
        .select(BOARD.asterisk(), USER.asterisk())
        .from(BOARD)
        .leftJoin(USER)
        .on(BOARD.OWNER_USERNAME.eq(USER.USERNAME))
        .fetch()
        .map { mapToInternalModel(it) }

    fun updateBoard(board: Board) {
        db.fetchOne(BOARD, BOARD.ID.eq(board.id?.toBigDecimal()))?.apply {
            from(board)
            ownerUsername = board.owner?.username
            participantIds = Json.encodeToString(board.participants?.map { it.username })
            updatedDate = LocalDateTime.now()
        }?.update()
    }

    fun createBoard(newBoard: Board): Board {
        val newId = getBoardsSeqNextVal()
        db.newRecord(BOARD, newBoard).apply {
            id = newId
            createdDate = LocalDateTime.now()
            updatedDate = LocalDateTime.now()
            ownerUsername = newBoard.owner?.username
            participantIds = Json.encodeToString(newBoard.participants?.map { it.username })
        }.insert()
        return getBoardById(newId)
    }

    private fun getBoardsSeqNextVal(): BigDecimal =
        db.select(DSL.field("BOARDS_SEQUENCE.nextval")).from("DUAL")
            .fetchOne()!!.map { it.into(BigDecimal::class.java) }

    private fun mapToInternalModel(record: Record): Board {
        val boardRecord = record.into(BoardRecord::class.java)
        return boardRecord.into(Board::class.java).copy(
            owner = record.into(UserRecord::class.java).mapToInternalModel(),
            participants = userService.getUsersByUsernameList(Json.decodeFromString(boardRecord.participantIds!!))
        )
    }

}