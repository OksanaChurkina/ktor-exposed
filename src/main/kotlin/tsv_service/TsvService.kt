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



    fun findBy(Contig: String, Left: Int,  Mutation: String, Right: Int): Tsv =
        transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE , repetitionAttempts = 0) {
            with(tsvTable) {
                slice(rs)
                    .select {
                        contig.eq(Contig) and
                                left.eq(Left) and
                                right.eq(Right) and
                                mutation.eq(Mutation)
                    }
                    .map { toTsv(it) }
                    .first()
            }

        }
    }









