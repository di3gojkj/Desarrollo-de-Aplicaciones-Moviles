// Archivo: model/SalaIMAX.kt
// Responsabilidad: representa una sala IMAX con pantalla gigante y sonido envolvente

package model

import java.time.LocalDateTime

// SalaIMAX tiene un campo adicional: si incluye lentes 3D (recargo del 30%)
class SalaIMAX(
    codigo: String,
    pelicula: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente,
    val incluyeLentes3D: Boolean  // propiedad adicional exclusiva de SalaIMAX; no cambia al registrar
) : Sala(codigo, pelicula, horaIngreso, tipoCliente) {

    companion object {
        const val TARIFA_BASE = 10_000.0 // $10.000 por hora (la más cara)
        const val RECARGO_3D = 0.30      // recargo del 30% si la función incluye lentes 3D
    }

    override fun calcularCobro(minutosUso: Long): Double {
        val horas = minutosUso / 60.0           // convertimos los minutos a horas
        val subtotal = TARIFA_BASE * horas      // cálculo base sin recargo
        // si la función incluye lentes 3D, añadimos el recargo del 30% sobre el subtotal
        return if (incluyeLentes3D) subtotal * (1 + RECARGO_3D) else subtotal
    }

    // el texto descriptivo incluye si hay lentes 3D o no, tal como lo requiere el sistema
    override fun descripcionTipo(): String =
        if (incluyeLentes3D) "Sala IMAX (con lentes 3D)" else "Sala IMAX (sin lentes 3D)"
}
