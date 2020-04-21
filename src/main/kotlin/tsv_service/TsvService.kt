package tsv_service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import tsv_model.Tsv
import tsv_model.tsvTable
import java.sql.Connection


class TsvService {

     fun toTsv(row: ResultRow): Tsv = Tsv(
        contig = row[tsvTable.contig] ,
        left = row[tsvTable.left],
        right = row[tsvTable.right],
        mutation = row[tsvTable.mutation],
        rs = row[tsvTable.rs] )


    //метод slice для множественного селекта
//    suspend fun getRS(s: String, i: Int, s1: String, i1: Int) {
//      val str = tsvTable.slice( tsvTable.contig, tsvTable.left, tsvTable.right, tsvTable.mutation)
//            tsvTable.select { tsvTable.left eq  i }.map { toTsv(it) }
//            }

//     fun findBy(contig: String, left: Int, right: Int, mutation: String): ResultRow {
//         val row = transaction {
//             addLogger(StdOutSqlLogger)
//             tsvTable.select {
//                 tsvTable.contig eq contig
//                 tsvTable.left eq left
//                 tsvTable.right eq right
//                 tsvTable.mutation eq mutation
//
//             }.first()
//         }
//        return row
//     }

    fun findBy(Contig: String, Left: Int,  Mutation: String, Right: Int): Tsv =
        transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE , repetitionAttempts = 0) {
            with(tsvTable) {
                slice(rs)
                    .select {
                        contig eq Contig
                        left eq Left
                        right eq Right
                        mutation eq Mutation
                    }
                    .map { toTsv(it) }
                    .first()
            }

        }
    }









