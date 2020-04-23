package tsv_service

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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



    fun findBy(Contig: String, Left: Int,  Mutation: String, Right: Int)  =
         transaction(transactionIsolation = 8 ,
             repetitionAttempts = 0) {
            tsvTable.select {
                tsvTable.contig.eq(Contig) and
                        tsvTable.left.eq(Left) and
                        tsvTable.right.eq(Right) and
                        tsvTable.mutation.eq(Mutation)
           }
          .map { toTsv(it) }
                .first()
        }

    }











