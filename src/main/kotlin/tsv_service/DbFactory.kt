package tsv_service

import com.typesafe.config.ConfigException
import com.univocity.parsers.tsv.TsvParser
import com.univocity.parsers.tsv.TsvParserSettings
import tsv_model.tsvTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import tsv_model.Tsv
import java.io.FileReader

object DbFactory {
    fun init() {
        val settings = TsvParserSettings()
        val parser = TsvParser(settings)
        val allRows: List<Array<String>> = parser.parseAll(FileReader("42.tsv"))

        transaction {
            create(tsvTable)
            with(tsvTable){
            batchInsert(allRows){
                this[contig] = it[0]
                this[left] = it[1].toInt()
                this[right] = it[2].toInt()
                if(it[3].isNullOrEmpty()){
                    this[mutation] = it[3].let { " " }
                } else
                {this[mutation] = it[3]}
                this[rs] = it[4]
            }

        }
    }}
}



