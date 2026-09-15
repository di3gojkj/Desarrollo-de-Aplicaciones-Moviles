// Archivo: model/TipoCliente.kt
// Responsabilidad: define los tipos de clientes del sistema CineMax

package model

// enum class: tipo enumerado que limita los valores posibles a exactamente tres opciones
enum class TipoCliente {
    REGULAR,       // cliente sin beneficios, paga tarifa completa
    SOCIO,         // cliente con membresía mensual, obtiene 20% de descuento sobre el subtotal
    DISCAPACITADO  // cliente con credencial vigente, obtiene 50% de descuento sobre el monto con IVA
}
