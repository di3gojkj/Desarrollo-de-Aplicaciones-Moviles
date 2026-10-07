package cl.duocuc.miregistro.model

enum class NivelExperiencia(val etiqueta: String){
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado")
}

//Estructura de datos principal
data class FormularioUiState (
    //Valores de los campos
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val nivel: NivelExperiencia = NivelExperiencia.PRINCIPIANTE,
    val recibirNoticias: Boolean = false,
    val aceptaTerminos: Boolean = false,
    //Estado visual de la clave
    val contrasenaVisible: Boolean = false,
    //Errores de los campos
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorTerminos: String? = null,
    //Resultado del formulario
    val registroExitoso: Boolean = false
)