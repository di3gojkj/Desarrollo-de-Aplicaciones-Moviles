package org.example.model

import java.time.LocalDateTime

class BiciCiudad(
    codigo: String,
    marca: String,
    modelo: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente
) : Bicicleta(codigo, marca, modelo, horaIngreso, tipoCliente) {

    companion object {
        const val TARIFA_BASE = 800.0
    }

    override fun calcularCobro(minutosUso: Int): Double {
        val horas = minutosUso / 60.0
        val subTotal = TARIFA_BASE * horas

        return if (tipoCliente == TipoCliente.ABONADO) subTotal * (1 - DESCUENTO_ABONADO)
        else subTotal
    }

    override fun descripcionTipo(): String = "Bicicleta Ciudad"

}