package cl.duocuc.miregistro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.miregistro.R
import cl.duocuc.miregistro.model.FormularioUiState
import cl.duocuc.miregistro.model.NivelExperiencia
import cl.duocuc.miregistro.ui.components.BotonPrincipal
import cl.duocuc.miregistro.ui.components.BotonSecundario
import cl.duocuc.miregistro.ui.components.CampoContrasena
import cl.duocuc.miregistro.ui.components.CampoTexto
import cl.duocuc.miregistro.ui.components.CasillaConTexto
import cl.duocuc.miregistro.ui.components.GrupoRadio
import cl.duocuc.miregistro.ui.components.ImagenEncabezado
import cl.duocuc.miregistro.ui.components.InterruptorConTexto
import cl.duocuc.miregistro.ui.components.TarjetaSeccion
import cl.duocuc.miregistro.ui.theme.Dimens
import cl.duocuc.miregistro.ui.theme.Exito
import cl.duocuc.miregistro.ui.theme.ExitoFondo
import cl.duocuc.miregistro.viewmodel.FormularioViewModel

@Composable
fun FormularioScreen(
    viewModel: FormularioViewModel = viewModel() //Crea una sola vez y sobrevive a las instancias
){
    //cada vez que el stateflow mute / redibujar la pantalla
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FormularioContent(
        uiState = uiState,
        onNombreCambia = viewModel::onNombreCambia,
        onCorreoCambia = viewModel::onCorreoCambia,
        onContrasenaCambia = viewModel::onContrasenaCambia,
        onAlternarVisibilidad = viewModel::onAlternarVisibilidad,
        onNivelSeleccionado = viewModel::onNivelSeleccionado,
        onNoticiasCambia = viewModel::onNoticiasCambia,
        onTerminosCambia = viewModel::onTerminosCambia,
        onLimpiar = viewModel::onLimpiar,
        onRegistar = viewModel::onRegistar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioContent(
    uiState: FormularioUiState,
    onNombreCambia: (String) -> Unit,
    onCorreoCambia: (String) -> Unit,
    onContrasenaCambia: (String) -> Unit,
    onAlternarVisibilidad: () -> Unit,
    onNivelSeleccionado: (NivelExperiencia) -> Unit,
    onNoticiasCambia: (Boolean) -> Unit,
    onTerminosCambia: (Boolean) -> Unit,
    onLimpiar: () -> Unit,
    onRegistar: () -> Unit
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_formulario)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ImagenEncabezado(
                imagen = R.drawable.banner_registro,
                descripcion = stringResource(R.string.cd_banner),
                titulo = stringResource(R.string.banner_titulo),
                subtitulo = stringResource(R.string.banner_subtitulo)
            )
            Column(
                modifier = Modifier.padding(Dimens.espacioPantalla),
                verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)
            ) {
                //CARD 1 - Datos Personales
                TarjetaSeccion(titulo = stringResource(R.string.seccion_datos)) {
                    CampoTexto(
                        valor = uiState.nombre,
                        onValorCambia = onNombreCambia,
                        etiqueta = stringResource(R.string.campo_nombre),
                        icono = R.drawable.ic_persona,
                        error = uiState.errorNombre
                    )
                    CampoTexto(
                        valor = uiState.correo,
                        onValorCambia = onCorreoCambia,
                        etiqueta = stringResource(R.string.campo_correo),
                        icono = R.drawable.ic_correo,
                        error = uiState.errorCorreo,
                        tipoTeclado = KeyboardType.Email
                    )
                    CampoContrasena(
                        valor = uiState.contrasena,
                        onValorCambia = onContrasenaCambia,
                        etiqueta = stringResource(R.string.campo_contrasena),
                        visible = uiState.contrasenaVisible,
                        onAlternarVisible = onAlternarVisibilidad,
                        error = uiState.errorContrasena
                    )
                }
                //CARD 2 - Preferencias
                TarjetaSeccion(titulo = stringResource(R.string.seccion_preferencias)) {
                    Text(
                        text = stringResource(R.string.etiqueta_nivel),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    GrupoRadio(
                        opciones = NivelExperiencia.entries,
                        seleccionada = uiState.nivel,
                        onSeleccionar = onNivelSeleccionado,
                        textoDe =  { it.etiqueta }
                    )
                    InterruptorConTexto(
                        texto = stringResource(R.string.opcion_noticias),
                        activado = uiState.recibirNoticias,
                        onCambio = onNoticiasCambia
                    )
                }
                //Terminos
                Column{
                    CasillaConTexto(
                        texto = stringResource(R.string.opcion_terminos),
                        marcado = uiState.aceptaTerminos,
                        onCambio = onTerminosCambia
                    )
                    uiState.errorTerminos?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = Dimens.espacioChico)
                        )
                    }
                }
                //Botones
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)) {
                    BotonSecundario(
                        texto = stringResource(R.string.btn_limpiar),
                        onClick = onLimpiar,
                        modifier = Modifier.weight(1f)
                    )
                    BotonPrincipal(
                        texto = stringResource(R.string.btn_registro),
                        onClick = onRegistar,
                        icono = R.drawable.ic_check,
                        modifier = Modifier.weight(1f)
                    )
                }
                //Mensaje de exito
                if (uiState.registroExitoso){
                    MensajeExito(uiState.nombre)
                }
                Spacer(Modifier.height(Dimens.espacioGrande))

            }

        }


    }

}
//Diseño para el mensaje de exito
@Composable
private fun MensajeExito(nombre: String){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ExitoFondo, contentColor = Exito)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.espacioChico),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Exito,
                modifier = Modifier.size(Dimens.tamanoIcono)
            )
            Text(
                text = stringResource(R.string.registro_exitoso, nombre),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = Dimens.espacioChico)
            )
        }
    }
}