package cl.duocuc.miregistro.ui.styles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import cl.duocuc.miregistro.ui.theme.Blanco
import cl.duocuc.miregistro.ui.theme.Dimens
import cl.duocuc.miregistro.ui.theme.GrisDeshabilitado
import cl.duocuc.miregistro.ui.theme.Primario

object EstilosBoton {
    val forma: Shape = RoundedCornerShape(Dimens.radioBoton)
    val relleno = PaddingValues(horizontal = 20.dp, vertical = 12.dp)

    //boton para el formulario = morado con texto blanco
    @Composable
    fun coloresPrincipal(): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Primario, //background-color
        contentColor = Blanco, // color
        disabledContentColor = Blanco,
        disabledContainerColor = GrisDeshabilitado
    )
    //botones secundarios = transparente con texto del color del tema del tlf
    @Composable
    fun coloresSecundario(): ButtonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    )
    //sombra del boton (animación - el boton se hunde al presionar)
    @Composable
    fun elevacion(): ButtonElevation = ButtonDefaults.buttonElevation(
        defaultElevation = Dimens.elevacionBoton,
        pressedElevation = 1.dp
    )
    //botones con bordes
    @Composable
    fun bordeSecundario(): BorderStroke = BorderStroke(
        Dimens.bordeBoton, MaterialTheme.colorScheme.primary
    )
}
//Selector clase CSS
fun Modifier.estiloAltoBoton(): Modifier = this.height(Dimens.alturaBoton)