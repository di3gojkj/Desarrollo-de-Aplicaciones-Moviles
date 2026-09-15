// Archivo: model/Sala.kt
// Responsabilidad: clase abstracta que representa una sala de cine genérica
// Esta clase define los datos y comportamientos comunes a todos los tipos de sala

package model

import java.time.LocalDateTime  // importamos para manejar fecha y hora de ingreso

// abstract class: no se puede instanciar directamente; obliga a las subclases a implementar calcularCobro()
abstract class Sala(
    val codigo: String,           // código único de la función (formato: dos letras, dos dígitos, dos letras)
    val pelicula: String,         // nombre de la película o función
    val horaIngreso: LocalDateTime, // momento exacto en que inicia la función; no cambia una vez registrado
    val tipoCliente: TipoCliente  // tipo de cliente que reservó: REGULAR, SOCIO o DISCAPACITADO
) {

    // companion object: agrupa constantes y funciones de clase (similares a static en Java)
    companion object {
        const val IVA = 0.19             // porcentaje de IVA aplicado sobre el subtotal
        const val DESCUENTO_SOCIO = 0.20 // descuento del 20% aplicado si el cliente es SOCIO
        const val DESCUENTO_DISCAPACITADO = 0.50 // descuento del 50% aplicado sobre monto con IVA

        // Regex para validar el formato del código: exactamente 2 letras + 2 dígitos + 2 letras
        val FORMATO_CODIGO = Regex("^[A-Za-z]{2}[0-9]{2}[A-Za-z]{2}$")

        // función de clase que valida si un código tiene el formato correcto
        fun codigoValido(codigo: String): Boolean = FORMATO_CODIGO.matches(codigo)
    }

    // abstract fun: obliga a cada subclase a definir su propia lógica de cobro
    // recibe los minutos que la función estuvo en uso
    abstract fun calcularCobro(minutosUso: Long): Double

    // función final que aplica el flujo completo de cálculo: subtotal → IVA → descuento discapacidad
    fun calcularTotal(minutosUso: Long): Double {
        val subtotal = calcularCobro(minutosUso) // paso 1: cálculo base según tipo de sala
        val conIva = subtotal * (1 + IVA)        // paso 2: se aplica IVA del 19% sobre el subtotal
        // paso 3: si el cliente es DISCAPACITADO, se aplica un 50% de descuento sobre el monto con IVA
        return if (tipoCliente == TipoCliente.DISCAPACITADO) conIva * (1 - DESCUENTO_DISCAPACITADO) else conIva
    }

    // función que retorna una descripción legible del tipo de sala (usada en reportes)
    // override se implementa en cada subclase
    abstract fun descripcionTipo(): String
}
