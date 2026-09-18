package com.rogue.shopcontrol.domain.usecase

import android.net.Uri
import com.rogue.shopcontrol.data.repository.BackupRepository
import com.rogue.shopcontrol.domain.model.ImportResult

class ImportBackupUseCase(
    private val repository: BackupRepository
) {

    suspend operator fun invoke(uri: Uri): ImportResult =
        repository.importBackup(uri)

}
