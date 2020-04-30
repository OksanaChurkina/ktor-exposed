package tsv_model

import org.jetbrains.exposed.sql.Table

object tsvTable: Table(){
    val contig = varchar("contig", 50)
    val left = integer("left")
    val mutation = varchar("mutation", 20)
    val right = integer("right")
    val rs = varchar("rs", 100)
}

data class Tsv(val contig: String,
               val left: Int,
               val mutation: String,
               val right: Int,
               val rs: String )