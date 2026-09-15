package org.example

// Archivo: Main.kt
// Responsabilidad: punto de entrada del programa; demuestra el funcionamiento completo del sistema
// Este archivo crea los objetos, registra entradas/salidas y llama las consultas de negocio

import kotlinx.coroutines.runBlocking // runBlocking: permite llamar funciones suspend desde main()
import model.*                         // importamos todos los modelos del paquete model
import service.CineMaxService          // importamos el servicio principal del negocio
import java.time.LocalDateTime         // usamos LocalDateTime para registrar el momento de ingreso

// fun main: función de entrada principal del programa
// runBlocking crea un scope de corrutinas que bloquea el hilo principal hasta que todo termine
fun main() = runBlocking {

    // creamos el servicio con el nombre del cine y la capacidad total de puestos
    val cine = CineMaxService("CineMax Alameda", 10)

    println("==============================================")
    println("   Bienvenido al sistema CineMax Alameda     ")
    println("==============================================\n")

    // ─────────────────────────────────────────────
    // R6 — Prueba de código con formato inválido
    // El sistema debe rechazarlo con mensaje claro y continuar funcionando
    // ─────────────────────────────────────────────
    println("=== Prueba de código inválido ===")
    val salaInvalida = SalaEstandar(
        codigo = "123ABC",         // formato incorrecto: empieza con dígitos
        pelicula = "Código Roto",
        horaIngreso = LocalDateTime.now(),
        tipoCliente = TipoCliente.REGULAR
    )
    cine.registrarEntrada(salaInvalida) // debe mostrar [ERROR] y no registrar nada
    println()


    // R5 — Registro de entradas (operación asíncrona — tarda 3 seg cada una)

    println("=== Registrando entradas ===")

    // SalaEstandar con cliente SOCIO: tendrá 20% de descuento en el subtotal
    val s1 = SalaEstandar(
        codigo = "SA12ES",
        pelicula = "Interstellar",
        horaIngreso = LocalDateTime.now(),
        tipoCliente = TipoCliente.SOCIO
    )

    // SalaEstandar con cliente REGULAR: paga la tarifa completa sin descuento
    val s2 = SalaEstandar(
        codigo = "SA99RG",
        pelicula = "Oppenheimer",
        horaIngreso = LocalDateTime.now(),
        tipoCliente = TipoCliente.REGULAR
    )

    // SalaVIP con cliente REGULAR: si dura menos de 30 min, el cobro es $0
    val s3 = SalaVIP(
        codigo = "VP22VI",
        pelicula = "Dune II",
        horaIngreso = LocalDateTime.now(),
        tipoCliente = TipoCliente.REGULAR
    )

    // SalaIMAX con lentes 3D y cliente DISCAPACITADO: recargo 30% + descuento 50% sobre IVA
    val s4 = SalaIMAX(
        codigo = "IX44ID",
        pelicula = "Avatar 3",
        horaIngreso = LocalDateTime.now(),
        tipoCliente = TipoCliente.DISCAPACITADO,
        incluyeLentes3D = true
    )

    // SalaIMAX sin lentes 3D con cliente REGULAR: sin recargo adicional
    val s5 = SalaIMAX(
        codigo = "IX77IR",
        pelicula = "Alien Romulus",
        horaIngreso = LocalDateTime.now(),
        tipoCliente = TipoCliente.REGULAR,
        incluyeLentes3D = false
    )

    // registramos cada función de forma secuencial (cada una espera 3 seg al sensor)
    cine.registrarEntrada(s1)
    cine.registrarEntrada(s2)
    cine.registrarEntrada(s3)
    cine.registrarEntrada(s4)
    cine.registrarEntrada(s5)
    println()

    // ─────────────────────────────────────────────
    // R4 — Consulta: ¿cuántos puestos quedan libres?
    // ─────────────────────────────────────────────
    println("Puestos disponibles ahora: ${cine.puestosDisponibles()} de 10\n")

    // ─────────────────────────────────────────────
    // R5 — Registro de salidas (operación asíncrona — tarda 6,5 seg cada una)
    // Internamente: calcula tarifa, emite ticket, libera el puesto
    // ─────────────────────────────────────────────
    println("=== Registrando salidas ===")

    // s1: SalaEstandar, SOCIO, 75 min → subtotal con 20% desc + IVA 19%
    cine.registrarSalida("SA12ES", 75)

    // s2: SalaEstandar, REGULAR, 180 min → tarifa plena + IVA 19%
    cine.registrarSalida("SA99RG", 180)

    // s3: SalaVIP, REGULAR, 25 min → menos de 30 min → cobro $0, sin ticket
    cine.registrarSalida("VP22VI", 25)

    // s4: SalaIMAX, lentes 3D, DISCAPACITADO, 120 min → tarifa + recargo 30% + IVA - 50%
    cine.registrarSalida("IX44ID", 120)

    // s5: SalaIMAX, sin lentes 3D, REGULAR, 45 min → tarifa normal + IVA 19%
    cine.registrarSalida("IX77IR", 45)


    // R6 — Prueba: salida de función que no existe

    println("\n=== Prueba de función no encontrada ===")
    cine.registrarSalida("XX00XX", 60) // código que no está en ningún puesto ocupado
    println()


    // R4 — Consultas de negocio adicionales

    println("=== Consultas de negocio ===")

    // ¿qué funciones del historial son de clientes SOCIO?
    val socios = cine.funcionesDeSocios()
    println("Funciones de socios: ${socios.map { it.codigo }}") // map extrae solo el código

    // ¿cuál es el ingreso promedio por función?
    println("Ingreso promedio por función: $${"%.0f".format(cine.ingresoPorFuncion())}")

    // ¿cuáles son todos los códigos de funciones atendidas en el turno?
    println("Códigos atendidos: ${cine.codigosFuncionesAtendidas()}")

    // ¿qué función estuvo más tiempo?
    val masLarga = cine.funcionMasLarga()
    println("Función más larga: ${masLarga?.sala?.codigo ?: "N/A"} — ${masLarga?.minutosUso} min")
    println()

    // ─────────────────────────────────────────────
    // R4 — Reporte de cierre de turno
    // ─────────────────────────────────────────────
    cine.generarReporteCierre()
}
