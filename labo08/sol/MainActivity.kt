package es.uva.inf5g.psm.labo08

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.uva.inf5g.psm.labo08.ui.theme.ShoppinglistappMVVMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ShoppingRepository()
        val factory = ShoppingViewModelFactory(repository)

        setContent {
            val shoppingViewModel: ShoppingViewModel by viewModels { factory }

            ShoppinglistappMVVMTheme {
                ShoppingListScreen(viewModel = shoppingViewModel)
            }
        }
    }
}

@Composable
fun ShoppingListScreen(viewModel: ShoppingViewModel) {
    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(24.dp)
    ) {
        Text(
            text = "🛒 Lista de Compras",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        AddItemButton { showAddDialog = true }

        if (showAddDialog) {
            ShoppingItemDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, quantity ->
                    viewModel.addItem(name, quantity)
                    showAddDialog = false
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.items.value) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { name, quantity ->
                            viewModel.editItem(item, name, quantity)
                        }
                    )
                } else {
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            viewModel.editItem(item)
                        },
                        onDeleteClick = { viewModel.removeItem(item)}
                    )
                }
            }
        }
    }
}

@Composable
fun AddItemButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3), contentColor = Color.White),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(50.dp)
            .width(200.dp)
            .shadow(4.dp, RoundedCornerShape(50))
    ) {
        Text("➕ Añadir Item", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ShoppingItemDialog(onDismiss: () -> Unit, onConfirm: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir un nuevo artículo") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = "" },
                    label = { Text("Nombre del artículo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it; error = "" },
                    label = { Text("Cantidad") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
                if (error.isNotEmpty()) {
                    Text(error, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(start = 8.dp, top = 4.dp))
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val q = quantity.toIntOrNull()
                when {
                    name.isBlank() -> error = "El nombre no puede estar vacío"
                    q == null || q <= 0 -> error = "La cantidad debe ser mayor que 0"
                    else -> onConfirm(name, q)
                }
            }) { Text("Aceptar") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun ShoppingListItem(item: ShoppingItemModel, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF212121))
                Text("Cantidad: ${item.quantity}", fontSize = 14.sp, color = Color(0xFF757575))
            }
            IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF0288D1)) }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFD32F2F)) }
        }
    }
}

@Composable
fun ShoppingItemEditor(item: ShoppingItemModel, onEditComplete: (String, Int) -> Unit) {
    var name by remember { mutableStateOf(item.name) }
    var quantity by remember { mutableStateOf(item.quantity.toString()) }
    var error by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it; error = "" }, label = { Text("Nombre del artículo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = quantity, onValueChange = { quantity = it; error = "" }, label = { Text("Cantidad") }, modifier = Modifier.fillMaxWidth())
            if (error.isNotEmpty()) Text(error, color = Color.Red, fontSize = 13.sp)
            Button(onClick = {
                val q = quantity.toIntOrNull()
                when {
                    name.isBlank() -> error = "El nombre no puede estar vacío"
                    q == null || q <= 0 -> error = "La cantidad debe ser mayor que 0"
                    else -> onEditComplete(name, q)
                }
            }, modifier = Modifier.align(Alignment.End)) { Text("Guardar") }
        }
    }
}