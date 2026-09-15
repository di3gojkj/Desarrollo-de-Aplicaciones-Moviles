// Archivo: model/SalaVIP.kt
// Responsabilidad: representa una sala VIP con butacas premium

package model

import java.time.LocalDateTime

// SalaVIP extiende Sala con su propia tarifa y regla especial de cobro mínimo
class SalaVIP(
    codigo: String,
    pelicula: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente
) : Sala(codigo, pelicula, horaIngreso, tipoCliente) {

    companion object {
        const val TARIFA_BASE = 6_000.0       // $6.000 por hora (tarifa premium)
        const val MINUTOS_MINIMO = 30L        // umbral de tiempo mínimo cobrable
    }

    // si el uso fue menos de 30 minutos, el cobro es $0 sin importar el tipo de cliente
    override fun calcularCobro(minutosUso: Long): Double {
        if (minutosUso < MINUTOS_MINIMO) return 0.0 // regla especial: funciones cortas son gratuitas
        val horas = minutosUso / 60.0               // conversión a horas para el cálculo normal
        return TARIFA_BASE * horas                  // SalaVIP no tiene descuento por tipo de cliente en el subtotal
    }

    override fun descripcionTipo(): String = "Sala VIP"
}
