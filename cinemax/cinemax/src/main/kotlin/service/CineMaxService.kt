// Archivo: service/CineMaxService.kt
// Responsabilidad: lógica central del sistema; gestiona puestos, registros y reportes

package service

import kotlinx.coroutines.delay         // delay() suspende la corrutina sin bloquear el hilo
import model.*                           // importamos todos los modelos del paquete model
import state.EstadoSala                  // importamos los estados del puesto

// class: clase concreta que contiene toda la lógica de negocio de CineMax
class CineMaxService(
    private val nombreCine: String, // nombre del cine, se muestra en reportes y mensajes
    private val capacidad: Int      // número total de puestos disponibles en el cine
) {
    // MutableList de puestos; se inicializa con 'capacidad' puestos numerados del 1 al N
    private val puestos: MutableList<Puesto> = MutableList(capacidad) { Puesto(it + 1) }

    // historial de tickets emitidos durante el turno (uno por cada salida completada)
    private val historial: MutableList<Ticket> = mutableListOf()

    // contador correlativo de tickets; incrementa con cada salida completada exitosamente
    private var contadorTickets = 0

    // mapa que acumula la recaudación por tipo de sala (ej: "Sala Estándar" → 45000.0)
    private val recaudacionPorTipo: MutableMap<String, Double> = mutableMapOf()

    // recaudación total acumulada en todo el turno
    private var recaudacionTotal = 0.0


    // R3 — Validación de formato del código de función


    // delega la validación al companion object de Sala; retorna true si el formato es correcto
    private fun validarCodigo(codigo: String): Boolean = Sala.codigoValido(codigo)


    // R5 — Registro asíncrono de entrada (suspend = solo puede llamarse desde una corrutina)


    suspend fun registrarEntrada(sala: Sala) { // suspend: la función puede pausarse sin bloquear

        // VALIDACIÓN R3/R6: el código debe tener el formato correcto antes de continuar
        if (!validarCodigo(sala.codigo)) {
            println("[ERROR] Código inválido '${sala.codigo}'. Formato requerido: AA00AA.") // mensaje claro
            return // salimos sin registrar; el sistema sigue funcionando normalmente
        }

        // buscamos el primer puesto en estado Libre disponible en la lista
        val puesto = puestos.firstOrNull { it.estaLibre } // firstOrNull retorna null si no hay ninguno

        // R6: si no hay puestos libres, avisamos y no realizamos el registro
        if (puesto == null) {
            println("[ERROR] Sin capacidad: el cine está lleno. No se puede registrar la entrada.")
            return // salimos sin bloquear el sistema
        }

        // paso 1: marcamos el puesto como EnProceso mientras esperamos al sensor
        puesto.cambiarEstado(EstadoSala.EnProceso("Registrando entrada: ${sala.pelicula}"))
        println("[INFO] Puesto #${puesto.numero} → procesando entrada de '${sala.pelicula}'...")

        delay(3_000) // simulamos 3 segundos de comunicación con el sensor físico del cine

        // paso 2: confirmada la operación, asignamos la sala y el puesto queda Ocupado
        puesto.asignarSala(sala) // cambia estado a Ocupado y guarda la referencia a la sala
        println("[OK]   Puesto #${puesto.numero} → entrada registrada: ${sala.pelicula} (${sala.codigo})")
    }

    // ─────────────────────────────────────────────────────────────────────
    // R5 — Registro asíncrono de salida
    // ─────────────────────────────────────────────────────────────────────

    suspend fun registrarSalida(codigo: String, minutosUso: Long) {

        // buscamos el puesto que tiene la sala con el código indicado
        val puesto = puestos.firstOrNull {
            it.estaOcupado && it.salaAsignada?.codigo == codigo // el puesto debe estar Ocupado con ese código
        }

        // R6: si no encontramos la función, avisamos y continuamos
        if (puesto == null) {
            println("[ERROR] Función '$codigo' no encontrada en ningún puesto ocupado.")
            return
        }

        val sala = puesto.salaAsignada!! // !! es seguro aquí porque ya verificamos que no es null

        // paso 1: marcamos el puesto como EnProceso mientras calculamos la tarifa
        puesto.cambiarEstado(EstadoSala.EnProceso("Calculando tarifa para ${sala.pelicula}"))
        println("[INFO] Puesto #${puesto.numero} → procesando salida de '${sala.pelicula}'...")

        delay(6_500) // simulamos 6,5 segundos de procesamiento del sensor de salida

        // paso 2: procesamos el pago y emitimos el ticket
        val ticket = procesarPago(sala, minutosUso)

        // paso 3: liberamos el puesto independientemente del resultado del pago
        puesto.liberar() // estado vuelve a Libre y salaAsignada queda null

        if (ticket != null) { // solo confirmamos si el ticket se generó correctamente
            println("[OK]   Puesto #${puesto.numero} → salida registrada. Ticket #${ticket.numero} emitido.")
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // R3 — Calcular pago y emitir ticket (llamado internamente desde registrarSalida)
    // ─────────────────────────────────────────────────────────────────────

    private fun procesarPago(sala: Sala, minutosUso: Long): Ticket? {
        val total = sala.calcularTotal(minutosUso) // cálculo completo: subtotal + IVA + descuento

        // R3/R6: si el resultado es 0 o negativo donde no debería serlo, reportamos error
        // Excepción: SalaVIP con menos de 30 min legítimamente cobra $0
        val esMenorMinimoVIP = sala is SalaVIP && minutosUso < SalaVIP.MINUTOS_MINIMO
        if (total <= 0.0 && !esMenorMinimoVIP) {
            println("[ERROR] Tarifa inválida (≤ $0) para el código '${sala.codigo}'. No se emite ticket.")
            return null // retornamos null para indicar fallo; el programa no se detiene
        }

        if (total == 0.0) { // caso válido: SalaVIP con función corta (menos de 30 min)
            println("[AVISO] Función '${sala.codigo}' duró $minutosUso min (< 30 min en Sala VIP). Cobro: $0.")
            return null // informamos, pero tampoco emitimos ticket (sin recaudación)
        }

        contadorTickets++                          // incrementamos el número correlativo
        val ticket = Ticket(contadorTickets, sala, minutosUso, total) // creamos el ticket con todos los datos

        historial.add(ticket) // agregamos el ticket al historial del turno

        // actualizamos la recaudación por tipo de sala
        val tipo = sala.descripcionTipo() // ej: "Sala Estándar", "Sala VIP", "Sala IMAX (con lentes 3D)"
        recaudacionPorTipo[tipo] = (recaudacionPorTipo[tipo] ?: 0.0) + total // acumulamos

        recaudacionTotal += total // sumamos al total general del turno

        ticket.mostrar() // mostramos el ticket en pantalla
        return ticket     // retornamos el ticket para que el llamador sepa que fue exitoso
    }

    // ─────────────────────────────────────────────────────────────────────
    // R4 — Consultas de negocio
    // ─────────────────────────────────────────────────────────────────────

    // ¿Cuántos puestos están disponibles ahora mismo?
    fun puestosDisponibles(): Int = puestos.count { it.estaLibre }

    // ¿Qué funciones del historial pertenecen a clientes SOCIO?
    fun funcionesDeSocios(): List<Sala> = historial
        .map { it.sala }                                 // extraemos la sala de cada ticket
        .filter { it.tipoCliente == TipoCliente.SOCIO } // filtramos solo los de tipo SOCIO

    // ¿Cuál es el ingreso promedio por función atendida en el turno?
    fun ingresoPorFuncion(): Double =
        if (historial.isEmpty()) 0.0 else recaudacionTotal / historial.size // evitamos división por cero

    // ¿Cuáles son los códigos de todas las funciones que han terminado en el turno?
    fun codigosFuncionesAtendidas(): List<String> = historial.map { it.sala.codigo }

    // ¿Qué función tuvo más tiempo de uso en el turno?
    fun funcionMasLarga(): Ticket? = historial.maxByOrNull { it.minutosUso } // null si historial vacío

    // ─────────────────────────────────────────────────────────────────────
    // R4 — Reporte de cierre de turno
    // ─────────────────────────────────────────────────────────────────────

    fun generarReporteCierre() {
        println("\n******************************************")
        println("*     REPORTE DE CIERRE DE TURNO           *")
        println("*     $nombreCine")
        println("********************************************")

        if (historial.isEmpty()) {           // si no hubo funciones en el turno, lo informamos
            println("No se atendió ninguna función en este turno.")
            return
        }

        println("\n── Detalle por función atendida ──")
        historial.forEach { ticket ->        // forEach itera sobre cada ticket del historial
            println(
                "Ticket #${ticket.numero} | ${ticket.sala.descripcionTipo()} | " +
                "Código: ${ticket.sala.codigo} | ${ticket.minutosUso} min | " +
                "$${"%.0f".format(ticket.montoTotal)}"
            )
        }

        // tipo de sala que más ingresos generó: buscamos la clave con mayor valor en el mapa
        val topTipo = recaudacionPorTipo.maxByOrNull { it.value }

        println("\n── Resumen del turno ──")
        println("Total recaudado      : $${"%.0f".format(recaudacionTotal)}")
        println("Funciones atendidas  : ${historial.size}")
        println("Ingreso promedio     : $${"%.0f".format(ingresoPorFuncion())}")
        println("Tipo más rentable    : ${topTipo?.key ?: "N/A"} ($${"%.0f".format(topTipo?.value ?: 0.0)})")
        println("Puestos libres cierre: ${puestosDisponibles()} de $capacidad")
        println("****************************************\n")
    }
}
