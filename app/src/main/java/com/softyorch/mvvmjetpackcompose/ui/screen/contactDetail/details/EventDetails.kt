package com.softyorch.mvvmjetpackcompose.ui.screen.contactDetail.details

sealed class EventDetails {
    data object Read: EventDetails()
    data object Edit: EventDetails()
    data object Deleting: EventDetails()
    data object Delete: EventDetails()
}
