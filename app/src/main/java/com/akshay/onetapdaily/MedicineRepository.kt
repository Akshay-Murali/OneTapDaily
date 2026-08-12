package com.akshay.onetapdaily

class MedicineRepository(
    private val medicineDao: MedicineDao
) {

    suspend fun getAllMedicines(): List<MedicineEntity> {
        return medicineDao.getAllMedicines()
    }

    suspend fun addMedicine(
        name: String
    ) {
        medicineDao.insertMedicine(
            MedicineEntity(
                name = name
            )
        )
    }

    suspend fun deleteMedicine(
        medicine: MedicineEntity
    ) {
        medicineDao.deleteMedicine(
            medicine
        )
    }

    suspend fun logMedicineTaken(
        medicineId: Long
    ) {
        medicineDao.insertLog(
            MedicineLogEntity(
                medicineId = medicineId,
                takenAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun getMedicineHistory(
        medicineId: Long
    ): List<MedicineLogEntity> {

        return medicineDao.getLogsForMedicine(
            medicineId
        )
    }
}