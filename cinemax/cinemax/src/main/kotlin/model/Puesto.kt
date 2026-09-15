// Archivo: model/Puesto.kt
// Responsabilidad: representa un puesto físico dentro del cine
// Cada puesto tiene un número único, un estado actual y puede guardar la sala asignada

package model

import state.EstadoSala // importamos la sealed class que define los estados posibles

// class mutable: el estado del puesto cambia durante la operación del sistema
class Puesto(
    val numero: Int // número identificador del puesto; no cambia una vez asignado
) {
    // var: el estado puede cambiar en cualquier momento (Libre → EnProceso → Ocupado, etc.)
    var estado: EstadoSala = EstadoSala.Libre

    // referencia a la sala actualmente asignada; null cuando el puesto está libre
    var salaAsignada: Sala? = null

    // propiedad calculada: retorna true solo si el puesto está en estado Libre
    val estaLibre: Boolean get() = estado is EstadoSala.Libre

    // propiedad calculada: retorna true si el puesto tiene una sala asignada y está Ocupado
    val estaOcupado: Boolean get() = estado is EstadoSala.Ocupada

    // función que asigna una sala al puesto y cambia su estado a Ocupado
    fun asignarSala(sala: Sala) {
        salaAsignada = sala                             // guardamos la referencia a la sala
        estado = EstadoSala.Ocupada(sala.pelicula)      // el estado pasa a Ocupado con el nombre de la película
    }

    // función que libera el puesto: elimina la sala asignada y vuelve al estado Libre
    fun liberar() {
        salaAsignada = null        // eliminamos la referencia a la sala anterior
        estado = EstadoSala.Libre  // el puesto queda disponible para una nueva función
    }

    // función que cambia el estado sin modificar la sala asignada (útil para EnProceso)
    fun cambiarEstado(nuevoEstado: EstadoSala) {
        estado = nuevoEstado // asignamos directamente el nuevo estado al puesto
    }

    // función que muestra en pantalla el estado actual del puesto (usada en reportes)
    fun mostrarEstado() {
        val descripcionEstado = when (estado) { // when evalúa el tipo de estado con smart cast
            is EstadoSala.Libre -> "LIBRE"
            is EstadoSala.Ocupada -> "OCUPADO — ${(estado as EstadoSala.Ocupada).descripcion}"
            is EstadoSala.EnProceso -> "EN PROCESO — ${(estado as EstadoSala.EnProceso).motivo}"
            is EstadoSala.FueraDeServicio -> "FUERA DE SERVICIO — ${(estado as EstadoSala.FueraDeServicio).motivo}"
        }
        println("Puesto #$numero: $descripcionEstado") // imprime número y estado en una línea
    }
}
