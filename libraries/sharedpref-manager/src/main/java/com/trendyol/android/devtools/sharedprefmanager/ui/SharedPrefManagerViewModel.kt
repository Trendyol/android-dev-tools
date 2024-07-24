package com.trendyol.android.devtools.sharedprefmanager.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trendyol.android.devtools.sharedprefmanager.data.SharedPrefModel
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefManagerUseCase
import com.trendyol.android.devtools.sharedprefmanager.domain.SharedPrefUpdateTypeValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedPrefManagerViewModel(
    private val sharedPrefManagerUseCase: SharedPrefManagerUseCase,
    private val sharedPrefUpdateTypeValidator: SharedPrefUpdateTypeValidator,
) : ViewModel() {

    var sharedPrefList = mutableStateListOf<SharedPrefModel>()

    private val _updateError = MutableStateFlow("")
    val updateError: StateFlow<String> = _updateError

    private val _selectedSharedPrefKey = MutableStateFlow("")

    private val _isUpdateCompleted = MutableStateFlow(false)
    val  isUpdateCompleted: StateFlow<Boolean> = _isUpdateCompleted

    private val _showDeleteConfirmationDialog = MutableStateFlow(false)
    val  showDeleteConfirmationDialog: StateFlow<Boolean> = _showDeleteConfirmationDialog

    fun getAllSharedPrefs() {
        viewModelScope.launch {
            val items = sharedPrefManagerUseCase.getAllSharedPref()
            updateSharedPrefList(items)
        }
    }

    fun updateSharedPrefItem(newValue: String) {
        viewModelScope.launch {
            runCatching {
                sharedPrefUpdateTypeValidator.validate(getSelectedSharedPrefItem(), newValue)
            }.onSuccess {
                runCatching { sharedPrefManagerUseCase.updateSharedPrefItem(getSelectedSharedPrefItem(), newValue) }
                    .onSuccess { _isUpdateCompleted.value = true }
                    .onFailure { _isUpdateCompleted.value = false }
            }.onFailure {
                _updateError.value = it.message ?: "Unknown error while updating shared pref item"
            }
        }
    }

    fun deleteSharedPrefItem() {
        viewModelScope.launch {
            runCatching {
                sharedPrefManagerUseCase.deleteSharedPrefItem(getSelectedSharedPrefItem())
            }.onSuccess {
                _isUpdateCompleted.value = true
            }.onFailure {
                _updateError.value = it.message ?: "Unknown error while deleting shared pref item"
            }
        }
    }

    fun deleteAllSharedPref() {
        viewModelScope.launch {
            runCatching {
                sharedPrefManagerUseCase.deleteAllSharedPref()
            }.onSuccess {
                _isUpdateCompleted.value = true
            }.onFailure {
                _updateError.value = it.message ?: "Unknown error while deleting all shared pref"
            }
        }
    }

    fun searchSharedPrefItemByKey(key: String) {
        if(key.isEmpty()) {
            getAllSharedPrefs()
        } else {
            val items = sharedPrefList.filter {
                it.key.uppercase().contains(key.uppercase())
            }
            updateSharedPrefList(items)
        }
    }

    fun clearUpdateError() {
        _updateError.value = ""
    }

    fun clearUpdateCompleted() {
        _isUpdateCompleted.value = false
    }

    fun getSelectedSharedPrefItem(): SharedPrefModel {
        return sharedPrefList.firstOrNull {
            it.key == _selectedSharedPrefKey.value
        } ?: SharedPrefModel("", "")
    }

    fun updateSelectedSharedPrefKey(key: String) {
        _selectedSharedPrefKey.value = key
    }

    fun updateShowDeleteConfirmationDialog(show: Boolean) {
        _showDeleteConfirmationDialog.value = show
    }

    private fun updateSharedPrefList(items: List<SharedPrefModel>) {
        sharedPrefList.clear()
        sharedPrefList.addAll(items)
    }
}
