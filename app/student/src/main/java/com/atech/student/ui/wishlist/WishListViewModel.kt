package com.atech.student.ui.wishlist

import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.WishListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val wishListUseCases: WishListUseCases
) : ViewModel() {
    val getAllResearch = wishListUseCases.getAll()
}