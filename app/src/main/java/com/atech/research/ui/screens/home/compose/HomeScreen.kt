package com.atech.research.ui.screens.home.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.atech.research.R
import com.atech.research.ui.comman.MainContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    MainContainer(
        title = stringResource(id = R.string.home),
        modifier = modifier
    ) { paddingValues ->
        Text(
            modifier = Modifier.padding(paddingValues),
            text = "Home Screen"
        )
    }
}