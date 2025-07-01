package me.androidbox.authentication.login.data

import me.androidbox.authentication.login.domain.model.LogoutRequest

fun LogoutRequest.toLogoutRequestDto(): LogoutRequestDto {
    return LogoutRequestDto(
        refreshToken = this.refreshToken
    )
}

fun LogoutRequestDto.toLogoutRequest(): LogoutRequest {
    return LogoutRequest(
        refreshToken = this.refreshToken
    )
}
