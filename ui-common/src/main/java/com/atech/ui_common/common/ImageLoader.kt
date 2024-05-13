/*
 *  Created by aiyu
 *  Copyright (c) 2021 . All rights reserved.
 *  BIT App
 *
 */

package com.atech.ui_common.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.atech.ui_common.R


@Composable
fun ImageLoader(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    @DrawableRes errorImage: Int = R.drawable.ic_error,
    contentScale: ContentScale = ContentScale.Fit,
    isRounderCorner: Boolean = false,
    onError: () -> Unit = {}
) {

    AsyncImage(modifier = modifier.let { if (isRounderCorner) it.clip(shape = CircleShape) else it },
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
            .decoderFactory(SvgDecoder.Factory()).crossfade(true).build(),
        contentDescription = stringResource(id = R.string.loaded_image),
        contentScale = contentScale,
        error = painterResource(id = errorImage),
        onError = {
            onError()
        })
}

@Composable
fun ImageLoaderRounderCorner(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    @DrawableRes errorImage: Int = R.drawable.ic_error,
    contentScale: ContentScale = ContentScale.Fit,
    isRounderCorner: Dp = 0.dp,
    onError: () -> Unit = {}
) {

    AsyncImage(modifier = modifier.clip(shape = RoundedCornerShape(isRounderCorner)),
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
            .decoderFactory(SvgDecoder.Factory()).crossfade(true).build(),
        contentDescription = stringResource(id = R.string.loaded_image),
        contentScale = contentScale,
        error = painterResource(id = errorImage),
        onError = {
            onError()
        })
}