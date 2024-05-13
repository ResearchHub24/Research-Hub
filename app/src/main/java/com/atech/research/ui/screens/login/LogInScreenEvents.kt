/*
 *  Created by aiyu
 *  Copyright (c) 2021 . All rights reserved.
 *  BIT App
 *
 */

package com.atech.research.ui.screens.login

sealed interface LogInScreenEvents {

    data class OnSignInResult(val state: LogInState) : LogInScreenEvents
    data object SetUID : LogInScreenEvents
}