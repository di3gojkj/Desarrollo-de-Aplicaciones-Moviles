// Archivo: model/Ticket.kt
// Responsabilidad: representa el comprobante de pago emitido al finalizar una función

package model

// data class: Kotlin genera automáticamente equals(), hashCode() y toString() útiles
data class Ticket(
    val numero: Int,            // número correlativo del ticket dentro del turno
    val sala: Sala,             // referencia a la sala/función que generó este ticket
    val minutosUso: Long,       // tiempo total que duró la función
    val montoTotal: Double      // monto final pagado (ya con IVA y descuentos aplicados)
) {
    // función que formatea el ticket como texto para mostrar en pantalla
    fun mostrar() {
        println("========== TICKET #$numero ==========")  // encabezado del ticket
        println("Tipo de sala : ${sala.descripcionTipo()}") // tipo de sala (Estándar, VIP, IMAX)
        println("Código       : ${sala.codigo}")            // código de la función
        println("Película     : ${sala.pelicula}")          // nombre de la película
        println("Cliente      : ${sala.tipoCliente}")       // tipo de cliente (REGULAR, SOCIO, etc.)
        println("Tiempo de uso: $minutosUso minutos")       // duración en minutos
        println("Monto pagado : $${"%.0f".format(montoTotal)}") // monto formateado sin decimales
        println("=====================================")
    }
}
