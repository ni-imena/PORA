package feri.pora.volunteerhub

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtualrunner.Run

class SharedViewModel : ViewModel() {
    val _selectedRun = MutableLiveData<Run>()
    fun saveRun(run: Run) {
        _selectedRun.value = run
    }


}