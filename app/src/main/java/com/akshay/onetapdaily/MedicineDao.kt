package com.akshay.onetapdaily

import androidx.room.*

@Dao
interface MedicineDao {

    @Query("SELECT * FROM medicines")
    suspend fun getAllMedicines(): List<MedicineEntity>

    @Insert
    suspend fun insertMedicine(
        medicine: MedicineEntity
    ): Long

    @Delete
    suspend fun deleteMedicine(
        medicine: MedicineEntity
    )

    @Insert
    suspend fun insertLog(
        log: MedicineLogEntity
    )

    @Query(
        "SELECT * FROM medicine_logs WHERE medicineId = :medicineId ORDER BY takenAt DESC"
    )
    suspend fun getLogsForMedicine(
        medicineId: Long
    ): List<MedicineLogEntity>
}