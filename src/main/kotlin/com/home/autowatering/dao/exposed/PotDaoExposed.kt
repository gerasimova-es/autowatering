package com.home.autowatering.dao.exposed

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotTable
import com.home.autowatering.model.database.converter.PotConverter
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PotDaoExposed : PotDao {
    companion object {
        //todo try use jooq
        //todo replace max(id) with max(date)
        val LIST = """select 
                        |pot.id, 
                        |pot.code, 
                        |pot.name, 
                        |pot.min_humidity, 
                        |pot.check_interval, 
                        |pot.watering_duration, 
                        |coalesce(st.humidity, -1)
                    |from 
                        |Pot pot left join
                        |(
                            |select s1.*
                            |from 
                                |pot_state s1, 
                                |(                    
                                    |select pot_id, max(id) max_id
                                    |from pot_state
                                    |group by pot_id
                                |) s2
                            |where s1.pot_id = s2.pot_id
                                |and s1.id = s2.max_id
                        |) st
                    |on pot.id = st.pot_id 
                    |order by pot.id""".trimMargin()
    }

    override fun findAll(): List<Pot> =
        transaction {
            val result = arrayListOf<Pot>()
            TransactionManager.current().exec(LIST) {
                while (it.next()) {
                    result += Pot(
                        id = it.getLong(1),
                        code = it.getString(2),
                        name = it.getString(3),
                        minHumidity = it.getInt(4),
                        checkInterval = it.getInt(5),
                        wateringDuration = it.getInt(6),
                        humidity = it.getInt(7)
                    )
                }
            }
            result
        }

    override fun findById(id: Long): Pot =
        transaction {
            PotTable.select {
                PotTable.id eq id
            }.singleOrNull()
        }?.let { PotConverter.fromDB(it) }
            ?: throw PotNotFoundException(id)

    override fun findByCode(code: String): Pot? =
        transaction {
            PotTable.select {
                PotTable.code eq code
            }.singleOrNull()
                ?.let { PotConverter.fromDB(it) }
        }

    override fun save(pot: Pot): Pot {
        transaction {
            val found = pot.id?.let { id ->
                PotTable.select { PotTable.id eq id }
                    .single()
            }
            if (found == null) {
                val id = PotTable.insert {
                    it[code] = pot.code
                    it[name] = pot.name!!
                    it[minHumidity] = pot.minHumidity!!
                    it[checkInterval] = pot.checkInterval!!
                    it[wateringDuration] = pot.wateringDuration!!
                }
                pot.id = id.generatedKey?.toLong()
            } else {
                PotTable.update({ PotTable.id eq pot.id!! }) {
                    it[code] = pot.code
                    it[name] = pot.name!!
                    it[minHumidity] = pot.minHumidity!!
                    it[checkInterval] = pot.checkInterval!!
                    it[wateringDuration] = pot.wateringDuration!!
                }
            }
        }
        //todo return converted from DB pot
        return pot
    }
}