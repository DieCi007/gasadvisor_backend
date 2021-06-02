package it.gasadvisor.gas_backend.job.priceupdate.service

import it.gasadvisor.gas_backend.util.Log
import java.io.BufferedReader
import java.util.*

abstract class GasStationPriceUpdateService<T> constructor(
    private val delimiter: String,
    private val maxColumnsPerRow: Int
) {
    companion object : Log()

    fun handle(br: BufferedReader) {
        var totalSkipped = 0
        var totalSaved = 0
        var currentIndex = 0
        val listToSave = mutableListOf<T>()
        var currentLine = try {
            br.readLine()
        } catch (e: Exception) {
            null
        }
        while (currentLine != null) {
            if (currentIndex < 3) {
                currentIndex++
                currentLine = br.readLine()
                continue
            }
            val optionalEntity = handleNewLine(currentLine)
            if (optionalEntity.isEmpty) {
                totalSkipped++
                currentLine = br.readLine()
                continue
            }
            listToSave.add(optionalEntity.get())
            if (listToSave.size >= 500) {
                saveAll(listToSave)
                listToSave.clear()
            }
            currentLine = br.readLine()
            if (currentLine == null) {
                saveAll(listToSave)
                listToSave.clear()
            }
            totalSaved++
        }
        log.info("Saved $totalSaved rows. Skipped $totalSkipped")
    }

    private fun handleNewLine(line: String): Optional<T> {
        val fields = line.split(delimiter)
        val hasDirtyField = fields.stream().anyMatch { s -> s.split(delimiter).size > maxColumnsPerRow }
        val isDirtyLine = fields.size != maxColumnsPerRow
        if (hasDirtyField || isDirtyLine) {
            return Optional.empty()
        }
        return entityFromFields(fields)
    }

    abstract fun entityFromFields(fields: List<String>): Optional<T>
    abstract fun saveAll(list: List<T>)
}
