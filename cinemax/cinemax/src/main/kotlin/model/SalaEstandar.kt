// Archivo: model/SalaEstandar.kt
// Responsabilidad: representa una sala de cine estándar (butacas normales)

package model

import java.time.LocalDateTime

// SalaEstandar hereda de Sala y es una subclase concreta (se puede instanciar)
class SalaEstandar(
    codigo: String,             // código de función, se pasa al constructor de Sala
    pelicula: String,           // nombre de la película
    horaIngreso: LocalDateTime, // hora de inicio
    tipoCliente: TipoCliente    // tipo de cliente
) : Sala(codigo, pelicula, horaIngreso, tipoCliente) { // llamada al constructor de la clase padre

    // companion object: contiene la tarifa base propia de este tipo de sala
    companion object {
        const val TARIFA_BASE = 3_000.0 // $3.000 por hora; guion bajo en número mejora la legibilidad
    }

    // override: implementación concreta del método abstracto de la clase padre
    override fun calcularCobro(minutosUso: Long): Double {
        val horas = minutosUso / 60.0 // convertimos minutos a horas (división en Double)
        val subtotal = TARIFA_BASE * horas // costo = tarifa por hora × cantidad de horas
        // si el cliente es SOCIO, aplicamos el 20% de descuento sobre el subtotal
        return if (tipoCliente == TipoCliente.SOCIO) subtotal * (1 - DESCUENTO_SOCIO) else subtotal
    }

    // retorna texto descriptivo del tipo para usar en pantalla y reportes
    override fun descripcionTipo(): String = "Sala Estándar"
}
