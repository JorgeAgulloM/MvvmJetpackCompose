package com.softyorch.mvvmjetpackcompose.ui.screen.userDetail.details

sealed class EventDetails {
    data object Read: EventDetails()
    data object Edit: EventDetails()
    data object Delete: EventDetails()
}
