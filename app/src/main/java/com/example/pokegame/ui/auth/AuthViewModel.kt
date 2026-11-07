package com.example.pokegame.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokegame.data.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState(isLoggedIn = repository.isLoggedIn))
    val state: StateFlow<AuthState> = _state.asStateFlow()

    fun signIn(email: String, password: String) =
        authenticate { repository.signIn(email, password) }

    fun register(email: String, password: String) =
        authenticate { repository.register(email, password) }

    fun signOut() {
        repository.signOut()
        _state.value = AuthState()
    }

    private fun authenticate(action: suspend () -> Result<Unit>) {
        if (_state.value.isLoading) return
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            action()
                .onSuccess {
                    _state.update { it.copy(isLoggedIn = true, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = messageFor(e)) }
                }
        }
    }

    private fun messageFor(e: Throwable): String {
        // Deja el error real en Logcat para poder depurarlo
        Log.e("AuthViewModel", "Error de autenticación", e)

        return when {
            e is FirebaseAuthWeakPasswordException ->
                "La contraseña debe tener al menos 6 caracteres"
            e is FirebaseAuthUserCollisionException ->
                "Ese correo ya está registrado"
            e is FirebaseAuthInvalidUserException ||
                e is FirebaseAuthInvalidCredentialsException ->
                "Correo o contraseña incorrectos"
            e is FirebaseNetworkException ->
                "Sin conexión. Revisa tu internet."
            e is FirebaseTooManyRequestsException ->
                "Demasiados intentos. Espera un momento."
            e is FirebaseAuthException && e.errorCode == "ERROR_OPERATION_NOT_ALLOWED" ->
                "El registro con correo no está activado en Firebase"
            e.message?.contains("CONFIGURATION_NOT_FOUND") == true ->
                "Firebase Authentication no está configurado en el proyecto"
            // TEMPORAL: muestra el detalle mientras depuramos
            else -> "No se pudo completar la acción: ${e.javaClass.simpleName} - ${e.message}"
        }
    }
}
