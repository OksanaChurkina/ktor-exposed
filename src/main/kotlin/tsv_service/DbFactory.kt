package tsv_service

import com.univocity.parsers.tsv.TsvParser
import com.univocity.parsers.tsv.TsvParserSettings
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import tsv_model.tsvTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import tsv_model.Tsv
import java.io.FileReader

object DbFactory {
    fun init() {
        // Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        val settings = TsvParserSettings()
        val parser = TsvParser(settings)
        val allRows: List<Array<String>> = parser.parseAll(FileReader("42.tsv")).take(5)

        transaction {
            create(tsvTable)
            with(tsvTable){
            batchInsert(allRows){
                this[contig] = it[0]
                this[left] = it[1].toInt()
                this[right] = it[2].toInt()
                this[mutation] = it[3]
                this[rs] = it[4]
            }

        }
    }}
}



