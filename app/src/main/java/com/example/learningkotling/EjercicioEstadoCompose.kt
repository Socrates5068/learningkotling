package com.example.learningkotling

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningkotling.ui.theme.LearningKotlingTheme

@Composable
fun InteractiveScreen(onBack: () -> Unit) {
    // 1. ESTADO DEL CONTADOR
    var contador by remember { mutableStateOf(0) }

    // 2. ESTADO DEL CAMPO DE TEXTO
    var productoInput by remember { mutableStateOf("") }

    // 3. ESTADO DE LA LISTA QUE CRECE
    val listaDeseos = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Button(onClick = onBack) {
                Text("Volver")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Estado en Compose",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECCIÓN 1: EL CONTADOR ---
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Artículos en el carrito: $contador", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { contador++ }) {
                    Text("Añadir al carrito")
                }
            }
        }

        HorizontalDivider()

        // --- SECCIÓN 2: CAMPO DE TEXTO Y LISTA ---
        Spacer(modifier = Modifier.height(24.dp))
        Text("Añadir a lista de deseos:", fontWeight = FontWeight.SemiBold)

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = productoInput,
                onValueChange = { nuevoTexto -> productoInput = nuevoTexto },
                label = { Text("Nombre del producto") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (productoInput.isNotBlank()) {
                        listaDeseos.add(productoInput)
                        productoInput = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Agregar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Tu lista:", fontWeight = FontWeight.SemiBold)
        listaDeseos.forEach { producto ->
            Text(
                text = "• $producto",
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InteractiveScreenPreview() {
    LearningKotlingTheme {
        InteractiveScreen(onBack = {})
    }
}
