package com.atech.ui_common.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.atech.ui_common.theme.ResearchHubTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    fullScreen: Boolean = true,
    onDismissRequest: () -> Unit = {},
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    content: @Composable () -> Unit = {}
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = containerColor
    ) {
        if (!fullScreen) {
            content.invoke()
            return@ModalBottomSheet
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content.invoke()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun UniversalBottomSheetPreview() {
    ResearchHubTheme {
        UniversalBottomSheet()
    }
}