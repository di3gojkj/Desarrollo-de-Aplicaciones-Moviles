// Archivo: state/EstadoSala.kt
// Responsabilidad: define los posibles estados de una sala de cine

// Importamos la librería de corrutinas para usar delay() en el servicio
package state

// sealed class: solo las subclases definidas dentro de este archivo pueden extenderla
// Esto garantiza que el sistema solo maneje estados válidos y conocidos
sealed class EstadoSala {

    // Objeto singleton que representa sala libre (no tiene propiedades adicionales)
    object Libre : EstadoSala()

    // data class: representa sala ocupada; guarda qué sala (identificador de función o película)
    data class Ocupada(val descripcion: String) : EstadoSala()

    // data class: representa sala en proceso de registro; incluye motivo del proceso
    data class EnProceso(val motivo: String) : EstadoSala()

    // data class: representa sala fuera de servicio; incluye motivo del bloqueo
    data class FueraDeServicio(val motivo: String) : EstadoSala()
}
