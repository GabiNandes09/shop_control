package com.rogue.shopcontrol.domain.usecase

import android.net.Uri
import com.rogue.shopcontrol.data.repository.BackupRepository

class ExportBackupUseCase(
    private val repository: BackupRepository
) {

    suspend operator fun invoke(): Uri =
        repository.exportBackup()

}
