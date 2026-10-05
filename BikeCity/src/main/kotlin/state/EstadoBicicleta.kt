package org.example.state

sealed class EstadoBicicleta {
    object Libre : EstadoBicicleta()

    data class Arrendada(val descripcion: String) : EstadoBicicleta()

    data class EnProceso(val motivo: String) : EstadoBicicleta()

    data class EnMantencion(val motivo: String) : EstadoBicicleta()
}