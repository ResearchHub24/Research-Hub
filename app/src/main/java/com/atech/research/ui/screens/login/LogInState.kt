/*
 *  Created by aiyu
 *  Copyright (c) 2021 . All rights reserved.
 *  BIT App
 *
 */

package com.atech.research.ui.screens.login

data class LogInState(
    val isNewUser: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null,
    val uId : String? = null
)