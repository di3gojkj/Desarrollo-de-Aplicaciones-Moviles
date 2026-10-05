package org.example.model

import java.time.LocalDateTime

class BiciMontana(
    codigo: String,
    marca: String,
    modelo: String,
    horaIngreso: LocalDateTime,
    tipoCliente: TipoCliente
) : Bicicleta (codigo, marca, modelo, horaIngreso, tipoCliente) {

    companion object {
        const val TARIFA_BASE =  1_500.0
        const val MINUTOS_MIN = 20
    }

    override fun calcularCobro(minutosUso: Int): Double {
        if (minutosUso < MINUTOS_MIN) return 0.0

        val horas = minutosUso / 60.0

        return TARIFA_BASE * horas
    }

    override fun descripcionTipo(): String = "Bicicleta Montaña"
}