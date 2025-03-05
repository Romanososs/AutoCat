package pro.aliencat.autocat.ui.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.aliencat.autocat.R
import pro.aliencat.autocat.ui.search.components.SearchScreenScopeBox
import pro.aliencat.autocat.ui.theme.AutoCatTheme

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
    CenterAlignedTopAppBar(
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
                        modifier = modifier,
                        windowInsets = WindowInsets(0,0,0,0)
                    ) { }
            }
        },
//        expandedHeight = 76.dp,
        windowInsets = WindowInsets(0,0,0,0),
        scrollBehavior = scrollBehavior,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = true
)
@Composable
fun VehicleSearchBarPreview() {
    var text by remember { mutableStateOf("123") }
    AutoCatTheme {
        Column {
            CenterAlignedTopAppBar(title = { Text("App bar") })
            VehicleSearchBar(
                text,
                onQueryChange = { text = it },
                {},
                { text = "" },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 8.dp)
            )
            SearchScreenScopeBox(
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 8.dp)
            ) {}
        }
    }
}