package com.akshay.onetapdaily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicineViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao =
        (application as OneTapDailyApp)
            .database
            .medicineDao()

    private val repository =
        MedicineRepository(dao)

    private val _medicines =
        MutableStateFlow<List<MedicineEntity>>(emptyList())

    val medicines: StateFlow<List<MedicineEntity>>
        get() = _medicines

    init {
        loadMedicines()
    }

    fun loadMedicines() {

        viewModelScope.launch {

            _medicines.value =
                repository.getAllMedicines()
        }
    }

    fun addMedicine(
        name: String
    ) {

        viewModelScope.launch {

            repository.addMedicine(name)

            loadMedicines()
        }
    }

    fun deleteMedicine(
        medicine: MedicineEntity
    ) {

        viewModelScope.launch {

            repository.deleteMedicine(medicine)

            loadMedicines()
        }
    }

    fun markMedicineTaken(
        medicine: MedicineEntity
    ) {

        viewModelScope.launch {

            repository.logMedicineTaken(
                medicine.id
            )
        }
    }

    suspend fun getMedicineHistory(
        medicineId: Long
    ): List<MedicineLogEntity> {

        return repository.getMedicineHistory(
            medicineId
        )
    }

    suspend fun getLastTakenText(
        medicineId: Long
    ): String {

        val history =
            repository.getMedicineHistory(
                medicineId
            )

        val lastLog =
            history.firstOrNull()

        return if (lastLog == null) {

            "Never"

        } else {

            SimpleDateFormat(
                "MMM dd • h:mm a",
                Locale.getDefault()
            ).format(
                Date(lastLog.takenAt)
            )
        }
    }
}