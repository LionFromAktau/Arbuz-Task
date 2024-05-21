package com.example.arbuztask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.arbuztask.data.Type
import com.example.arbuztask.ui.AppViewModelProvider
import com.example.arbuztask.ui.CartScreen
import com.example.arbuztask.ui.HomeScreen

enum class Screen(){
    Home,
    Cart
}

data class BottomNavigationItem(
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var badgeCount: Int? = null,
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketApp(modifier: Modifier = Modifier){
    val viewModel: AppViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val selectedItemsQuantity by viewModel.selectedItemsQuantity.collectAsState()

    val navigationItemList = listOf(
        BottomNavigationItem(
            screen = Screen.Home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            screen = Screen.Cart,
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            badgeCount = selectedItemsQuantity,
        )
    )
    var selectedScreen by remember {
        mutableStateOf(Screen.Home)
    }
    var bottomSheetSort by remember {
        mutableStateOf(false)
    }
    var bottomSheetFilter by remember {
        mutableStateOf(false)
    }
    var isSortedByPrice by remember {
        mutableStateOf(true)
    }
    var isType by remember {
        mutableStateOf(Type.All)
    }

    val sheetStateSort = rememberModalBottomSheetState()
    val sheetStateFilter = rememberModalBottomSheetState()
    Scaffold(
        modifier = modifier,
        topBar = {
            MarketTopAppBar(
                selectedScreen = selectedScreen,
                onFilterClick = {bottomSheetFilter = true},
                onSortClick = {bottomSheetSort = true}
            )
        },
        bottomBar = {
            BottomAppNaviagtion(
                items = navigationItemList,
                selectedScreen = selectedScreen,
                onClick = {
                    selectedScreen = it
                }
            )
        }
    ){
        if(selectedScreen == Screen.Home){

            HomeScreen(modifier = Modifier.padding(it), viewModel = viewModel)

            if(bottomSheetSort){
                BottomAppSheetSort(
                    sheetState = sheetStateSort,
                    onDismiss = { bottomSheetSort = false },
                    onClick = {
                        isSortedByPrice = it
                        viewModel.updateItems(isSortedByPrice, isType)
                        bottomSheetSort = false
                    }
                )
            }
            if(bottomSheetFilter){
                BottomAppSheetFilter(
                    sheetState = sheetStateFilter,
                    onDismiss = { bottomSheetFilter = false },
                    onClick = {
                        isType = it
                        viewModel.updateItems(isSortedByPrice, isType)
                        bottomSheetFilter = false
                    }
                )
            }

        }

        else{
            CartScreen(modifier = Modifier.padding(it), viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppSheetSort(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClick: (Boolean) -> Unit
){

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Sorting", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(true) }) {
            Text(text = "By Price")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(false) }) {
            Text(text = "By Title")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppSheetFilter(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onClick: (Type) -> Unit
){

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Filter", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(Type.All) }) {
            Text(text = "All")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(Type.Food) }) {
            Text(text = "Only Food")
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(Type.Drink) }) {
            Text(text = "Only Drink")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketTopAppBar(
    modifier: Modifier = Modifier,
    selectedScreen: Screen = Screen.Home,
    onSortClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
){
    if(selectedScreen == Screen.Home){
        TopAppBar(
            modifier = modifier,
            title = {
                Text(text = selectedScreen.name)
            },
            actions = {
                Icon(
                    modifier = Modifier.clickable { onSortClick() },
                    imageVector = Icons.Filled.Sort,
                    contentDescription = "Sort",
                )
                Spacer(modifier = modifier.size(16.dp))
                Icon(
                    modifier = Modifier.clickable { onFilterClick() },
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = "Filter"
                )
            }
        )
    }
    else{
        TopAppBar(
            modifier = modifier,
            title = {
                Text(text = selectedScreen.name)
            },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomAppNaviagtion(items: List<BottomNavigationItem>, selectedScreen: Screen, onClick: (Screen) -> Unit){

    NavigationBar {
        items.forEachIndexed{ index, item ->
            NavigationBarItem(
                selected = (item.screen == selectedScreen),
                onClick = { onClick(item.screen) },
                label = { Text(text = item.screen.name) },
                icon = {
                    BadgedBox(
                        badge = {
                            if(item.badgeCount != null){
                                Badge{
                                    Text(text = item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if(item.screen == selectedScreen) {
                                item.selectedIcon
                            }else{
                                item.unselectedIcon
                            },
                            contentDescription = item.screen.name
                        )
                    }
                }
            )
        }
    }

}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun previewMarketApp(){
    MarketTopAppBar()
}



