package cl.duocuc.miregistro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.miregistro.model.FormularioUiState
import cl.duocuc.miregistro.model.NivelExperiencia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FormularioViewModel: ViewModel(){
    //Privada y mutable
    private val _uiState = MutableStateFlow(FormularioUiState())
    //Publica y solo Lectura
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    //Eventos de los campos
    fun onNombreCambia(valor: String){
        _uiState.update { it.copy(nombre = valor, errorNombre = null, registroExitoso = false) }
    }
    fun onCorreoCambia(valor: String){
        _uiState.update { it.copy(correo = valor, errorCorreo = null, registroExitoso = false)}
    }
    fun onContrasenaCambia(valor: String){
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, registroExitoso = false)}
    }
    fun onAlternarVisibilidad(){
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }
    //Evento para los controles de seleccion
    fun onNivelSeleccionado(nivel: NivelExperiencia){
        _uiState.update { it.copy(nivel = nivel) }
    }
    fun onNoticiasCambia(valor: Boolean){
        _uiState.update { it.copy(recibirNoticias = valor) }
    }
    fun onTerminosCambia(valor: Boolean){
        _uiState.update { it.copy(aceptaTerminos = valor, errorTerminos = null) }
    }
    //Botones
    //Boton Limpiar
    fun onLimpiar(){
        _uiState.value = FormularioUiState()
    }
    //Boton registrarse
    fun onRegistar(){
        val estado = _uiState.value
        //Validar los campos
        // ! = Significa --> Si no tiene
        val errorNombre = if(estado.nombre.isBlank()) "El nombre es obligatorio" else null
        val errorContrasena = if(estado.correo.length < 6) "Minimo 6 caracteres" else null
        val errorCorreo = if(!estado.correo.contains("@") || !estado.correo.contains(".")) "Ingrese un correo valido" else null
        val errorTerminos = if(!estado.aceptaTerminos) "Debes aceptar los terminos" else null
        //Verificar si eisten errores
        val hayErrores = listOf(errorNombre, errorContrasena, errorCorreo, errorTerminos).any { it !=null }
        _uiState.update { it.copy(
            errorNombre = errorNombre,
            errorContrasena = errorContrasena,
            errorCorreo = errorCorreo,
            errorTerminos = errorTerminos,
            registroExitoso = !hayErrores
        ) }
    }

}