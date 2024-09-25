package com.guicarneirodev.goniometro.domain.validator

import java.util.regex.Pattern

class RegisterValidator {
    private val emailPattern: Pattern = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" + "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )

    private val passwordPattern: Pattern = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+_(){}|~/<>.?,€£¥=])(?=\\S+$).{6,}$"
    )

    fun isEmailValid(email: String): Boolean = emailPattern.matcher(email).matches()

    fun isPasswordValid(password: String): Boolean = passwordPattern.matcher(password).matches()

    fun passwordMatchError(password: String, confirmPassword: String): String =
        if (password != confirmPassword) "As senhas não coincidem." else ""

    fun getPasswordError(password: String): String {
        val validators = listOf(
            { pass: String -> "A senha deve ter pelo menos 6 caracteres.".takeIf { pass.length < 6 } },
            { pass: String -> "A senha deve conter pelo menos uma letra minúscula.".takeIf { pass.none { it.isLowerCase() } } },
            { pass: String -> "A senha deve conter pelo menos uma letra maiúscula.".takeIf { pass.none { it.isUpperCase() } } },
            { pass: String -> "A senha deve conter pelo menos um número.".takeIf { pass.none { it.isDigit() } } },
            { pass: String ->
                "A senha deve conter pelo menos um caractere especial.".takeIf {
                    !pass.contains(Regex("[!@#$%^&+_(){}|~/<>.?,€£¥=]"))
                }
            }
        )

        return validators
            .asSequence()
            .mapNotNull { it(password) }
            .firstOrNull() ?: ""
    }
}