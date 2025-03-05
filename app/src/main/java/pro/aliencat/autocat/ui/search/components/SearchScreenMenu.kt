package pro.aliencat.autocat.ui.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import pro.aliencat.autocat.R


@Composable
fun SearchScreenMenu(onFilterClick: () -> Unit, onMapClick: () -> Unit, onExportClick: () -> Unit) {
    var isDropdownMenuExpended by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { isDropdownMenuExpended = true }) {
            Icon(
                painterResource(R.drawable.more_horiz_24dp),
                contentDescription = "Open menu"
            )//TODO
        }
        DropdownMenu(
            expanded = isDropdownMenuExpended,
            onDismissRequest = { isDropdownMenuExpended = false }) {
            DropdownMenuItem(
                text = { Text("Filter") },
                onClick = onFilterClick,
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.filter_list_24dp),
                        contentDescription = null
                    )
                } //TODO
            )
            DropdownMenuItem(
                text = { Text("Map") },
                onClick = onMapClick,
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.map_24dp),
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("Export") },
                onClick = onExportClick,
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.share_24dp),
                        contentDescription = null
                    )
                },
            )
        }
    }
}
