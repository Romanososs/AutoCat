package pro.aliencat.autocat.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pro.aliencat.autocat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        title = {
            ProvideTextStyle(value = MaterialTheme.typography.bodyLarge) {
                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = query,
                            onQueryChange = onQueryChange,
                            onSearch = { onSearchClick() },
                            expanded = false,
                            onExpandedChange = { },
                            placeholder = { Text("Search") },//TODO
                            leadingIcon = {
                                Icon(
                                    painterResource(R.drawable.search_24dp),
                                    contentDescription = null
                                )
                            },
                            //TODO add animation
                            trailingIcon = {
                                AnimatedVisibility(
                                    query.isNotBlank(),
                                    enter = fadeIn(), //+ expandHorizontally(expandFrom = Alignment.Start),
                                    exit = fadeOut(), //+ shrinkHorizontally(shrinkTowards = Alignment.Start)
                                ) {
                                    IconButton(onCancelClick) {
                                        Icon(
                                            painterResource(R.drawable.cancel_24dp),
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                        )
                    },
                    expanded = false,
                    onExpandedChange = { },
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 16.dp)
                ) { }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun VehicleSearchBarPreview() {
    var text by remember { mutableStateOf("123") }
    VehicleSearchBar(text, onQueryChange = { text = it }, {}, { text = "" })
}