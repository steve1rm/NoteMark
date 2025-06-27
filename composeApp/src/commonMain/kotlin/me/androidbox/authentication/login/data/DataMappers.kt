package me.androidbox.authentication.login.data

import me.androidbox.authentication.login.domain.model.LoginRequest
import me.androidbox.authentication.login.domain.model.LoginResponse

fun LoginResponseDto.toLoginResponse(): LoginResponse {
    return LoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        username = username
    )
}

fun LoginResponse.toLoginResponseDto(): LoginResponseDto {
    return LoginResponseDto(
        accessToken = accessToken,
        refreshToken = refreshToken,
        username = username
    )
}

fun LoginRequest.toLoginRequestDto(): LoginRequestDto {
    return LoginRequestDto(
        email = email,
        password = password
    )
}

fun LoginRequestDto.toLoginRequest(): LoginRequest {
    return LoginRequest(
        email = email,
        password = password
    )
}



