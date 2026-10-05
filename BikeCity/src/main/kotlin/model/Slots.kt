package org.example.model

import org.example.state.EstadoBicicleta

class Slots(
    val numero: Int
) {
    var estadoBicicleta: EstadoBicicleta = EstadoBicicleta.Libre

    var bicicletaAsignada: Bicicleta? = null

    val estaLibre: Boolean get() = estadoBicicleta is EstadoBicicleta.Libre

    val estaOcupado: Boolean get() = estadoBicicleta is EstadoBicicleta.Arrendada

    fun asignarBicicleta(bicicleta : Bicicleta) {
        bicicletaAsignada = bicicleta
    }
}