package it.gasadvisor.gas_backend.job.app_update.service

import it.gasadvisor.gas_backend.util.logging.Log
import java.io.BufferedReader
import java.util.*

abstract class GasUpdateService<T> constructor(
    private val delimiter: String,
    private val maxColumnsPerRow: Int
) {
    companion object : Log()

    open fun handle(br: BufferedReader) {
        val timeInit = System.currentTimeMillis()
        beforeAll()
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
            if (currentIndex < 2) {
                currentIndex++
                currentLine = br.readLine()
                continue
            }
            val optionalEntity = handleNewLine(currentLine)
            if (optionalEntity.isEmpty) {
                totalSkipped++
                currentLine = br.readLine()
                if (currentLine == null && listToSave.size > 0) {
                    saveAll(listToSave)
                }
                continue
            }
            listToSave.add(optionalEntity.get())
            if (listToSave.size >= 500) {
                saveAll(listToSave)
                listToSave.clear()
            }
            currentLine = br.readLine()
            if (currentLine == null && listToSave.size > 0) {
                saveAll(listToSave)
            }
            totalSaved++
        }
        log.info("Saved $totalSaved rows. Skipped $totalSkipped")
        log.info("Operation completed in ${(System.currentTimeMillis() - timeInit) / 1000} seconds")
    }

    private fun handleNewLine(line: String): Optional<T> {
        val fields = line.split(delimiter)
        val hasDirtyField = fields.stream().anyMatch { s -> s.split(delimiter).size > maxColumnsPerRow }
        val isDirtyLine = fields.size != maxColumnsPerRow
        if (hasDirtyField || isDirtyLine) {
            handleDirtyLine(line)
            return Optional.empty()
        }
        return entityFromFields(fields)
    }

    abstract fun beforeAll()
    abstract fun handleDirtyLine(line: String)
    abstract fun entityFromFields(fields: List<String>): Optional<T>
    abstract fun saveAll(list: List<T>)
}
