package org.example.model

import java.time.LocalDateTime

class BiciElectrica(
    codigo: String,
    marca: String,
    modelo: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente,
    val largaAutonomia: Boolean
) : Bicicleta(codigo, marca, modelo, horaIngreso, tipoCliente){

    companion object{
        const val TARIFA_BASE = 2_200.0
        const val RECARGO_AUTONOMIA = 0.30
    }

    override fun calcularCobro(minutosUso: Int): Double {
        val horas = minutosUso / 60.0
        val subTotal = TARIFA_BASE * horas

        return if (largaAutonomia) subTotal * (1 + RECARGO_AUTONOMIA)
        else subTotal
    }

    override fun descripcionTipo(): String =
        if (largaAutonomia) "Bicicleta Electrica (Larga Autonomia)"
        else "Bicicleta Electrica (Normal)"
}