package com.atech.research.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.atech.core.use_cases.NoUserLogInException
import com.atech.core.use_cases.SetTokenWorkManagerUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateTokenWorker @AssistedInject constructor(
   private val setTokenUseCase: SetTokenWorkManagerUseCase,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result =
        setTokenUseCase.invoke { exception ->
            if (exception != null) {
                if (exception is NoUserLogInException)
                    Result.failure(Data.Builder().putString("error", exception.message).build())
                else
                    Result.retry()
            } else Result.success()
        }

}