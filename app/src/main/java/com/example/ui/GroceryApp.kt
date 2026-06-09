package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import com.example.data.GroceryItem

@Composable
fun GlassBackground() {
    Box(modifier = Modifier.fillMaxSize().background(com.example.ui.theme.BackgroundColor)) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(com.example.ui.theme.Emerald100, androidx.compose.ui.graphics.Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(size.width + 100.dp.toPx(), -50.dp.toPx()),
                    radius = 350.dp.toPx()
                ),
                radius = 350.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(size.width + 100.dp.toPx(), -50.dp.toPx())
            )
            drawCircle(
                brush = androidx.compose.ui.graphics.Brush.radialGradient(
                    colors = listOf(com.example.ui.theme.Orange100, androidx.compose.ui.graphics.Color.Transparent),
                    center = androidx.compose.ui.geometry.Offset(-50.dp.toPx(), size.height + 50.dp.toPx()),
                    radius = 400.dp.toPx()
                ),
                radius = 400.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(-50.dp.toPx(), size.height + 50.dp.toPx())
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroceryApp(viewModel: GroceryViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "shop"
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        GlassBackground()
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                    placeholder = { Text("Search items...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = com.example.ui.theme.GlassWhite80,
                        unfocusedContainerColor = com.example.ui.theme.GlassWhite60,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = com.example.ui.theme.BorderWhite40,
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = com.example.ui.theme.GlassWhite70,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Shop") },
                        label = { Text("Shop") },
                        selected = currentRoute == "shop",
                        colors = NavigationBarItemDefaults.colors(
                             indicatorColor = com.example.ui.theme.Emerald50,
                             selectedIconColor = com.example.ui.theme.Emerald600,
                             selectedTextColor = com.example.ui.theme.Emerald600,
                             unselectedIconColor = com.example.ui.theme.Slate400,
                             unselectedTextColor = com.example.ui.theme.Slate400
                        ),
                    onClick = {
                        if (currentRoute != "shop") navController.navigate("shop") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
                        label = { Text("Cart") },
                        selected = currentRoute == "cart",
                        colors = NavigationBarItemDefaults.colors(
                             indicatorColor = com.example.ui.theme.Emerald50,
                             selectedIconColor = com.example.ui.theme.Emerald600,
                             selectedTextColor = com.example.ui.theme.Emerald600,
                             unselectedIconColor = com.example.ui.theme.Slate400,
                             unselectedTextColor = com.example.ui.theme.Slate400
                        ),
                    onClick = {
                        if (currentRoute != "cart") navController.navigate("cart") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Order Again") },
                    label = { Text("Order Again") },
                    selected = currentRoute == "history",
                    colors = NavigationBarItemDefaults.colors(
                         indicatorColor = com.example.ui.theme.Emerald50,
                         selectedIconColor = com.example.ui.theme.Emerald600,
                         selectedTextColor = com.example.ui.theme.Emerald600,
                         unselectedIconColor = com.example.ui.theme.Slate400,
                         unselectedTextColor = com.example.ui.theme.Slate400
                    ),
                    onClick = {
                        if (currentRoute != "history") navController.navigate("history") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "shop",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("shop") {
                ShopScreen(viewModel)
            }
            composable("cart") {
                CartScreen(viewModel)
            }
            composable("history") {
                HistoryScreen(viewModel)
            }
        }
    }
}
}

val categoryMapping = mapOf(
    "Produce" to listOf("apple", "banana", "broccoli", "carrot", "tomato", "spinach", "lettuce", "mango", "strawberry", "avocado", "onion", "garlic", "potato", "pepper", "fruit", "veg"),
    "Dairy & Eggs" to listOf("milk", "cheese", "yogurt", "butter", "cream", "egg", "eggs", "paneer"),
    "Meat & Seafood" to listOf("chicken", "beef", "pork", "fish", "salmon", "bacon", "sausage", "meat", "shrimp"),
    "Bakery" to listOf("bread", "bagel", "croissant", "muffin", "bun", "cake", "pastry", "pie"),
    "Pantry" to listOf("rice", "pasta", "beans", "cereal", "flour", "sugar", "salt", "oil", "sauce", "soup", "spice", "spices", "jam", "honey", "nut", "seed", "oat"),
    "Beverages" to listOf("water", "juice", "soda", "coffee", "tea", "beer", "wine"),
    "Snacks" to listOf("chip", "cookie", "cracker", "popcorn", "candy", "chocolate"),
    "Household" to listOf("paper", "soap", "detergent", "clean", "trash", "bag", "foil", "wrap", "tissue", "napkin")
)

fun getItemCategory(name: String): String {
    val lowerName = name.lowercase()
    for ((category, keywords) in categoryMapping) {
        if (keywords.any { lowerName.contains(it) }) {
            return category
        }
    }
    return "Other"
}

@Composable
fun CartScreen(viewModel: GroceryViewModel) {
    var newItemName by remember { mutableStateOf("") }
    val items by viewModel.activeItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your Cart",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = com.example.ui.theme.Slate800
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newItemName,
                onValueChange = { newItemName = it },
                modifier = Modifier.weight(1f),
                label = { Text("Search and Add Item") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = com.example.ui.theme.GlassWhite80,
                    unfocusedContainerColor = com.example.ui.theme.GlassWhite60,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = com.example.ui.theme.BorderWhite40,
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            FloatingActionButton(
                onClick = { 
                    viewModel.addItem(newItemName.trim())
                    newItemName = ""
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = androidx.compose.foundation.shape.CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "Your cart is empty. Add items above!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = com.example.ui.theme.Slate500
                )
            }
        } else {
            Box(modifier = Modifier.weight(1f).fillMaxWidth().background(
                com.example.ui.theme.Slate900Alpha5, RoundedCornerShape(32.dp)
            ).border(1.dp, com.example.ui.theme.BorderSlate100, RoundedCornerShape(32.dp)).padding(16.dp)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val activeItemsList = items.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    val groupedActiveItems = activeItemsList.groupBy { getItemCategory(it.name) }
                    val sortedCategories = groupedActiveItems.keys.sorted()

                    sortedCategories.forEach { category ->
                        item {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = com.example.ui.theme.Slate800,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }
                        items(groupedActiveItems[category] ?: emptyList(), key = { it.id }) { item ->
                            ItemCard(
                                item = item,
                                onDelete = { viewModel.deleteItem(item) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val context = androidx.compose.ui.platform.LocalContext.current
                            Button(
                                onClick = {
                                    val total = items.size * 50
                                    val uri = android.net.Uri.parse("upi://pay?pa=store@upi&pn=Grocery%20Store&am=${total}.00&cu=INR")
                                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
                                    val chooser = android.content.Intent.createChooser(intent, "Pay with UPI")
                                    context.startActivity(chooser)
                                    viewModel.checkoutCart()
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Checkout and Pay ₹${items.size * 50} (UPI)")
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(item: GroceryItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = com.example.ui.theme.GlassWhite80,
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderWhite20),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(32.dp).background(Color.White, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = com.example.ui.theme.Slate600, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = com.example.ui.theme.Slate800
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = com.example.ui.theme.Slate400
                )
            }
        }
    }
}


@Composable
fun HistoryScreen(viewModel: GroceryViewModel) {
    val historyItems by viewModel.historyItems.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Order Again",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = com.example.ui.theme.Slate800
        )
        Text(
            text = "Previously bought items used for habit building.",
            style = MaterialTheme.typography.bodyMedium,
            color = com.example.ui.theme.Slate500
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (historyItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "No previous orders yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = com.example.ui.theme.Slate500
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(com.example.ui.theme.GlassWhite40, RoundedCornerShape(32.dp))
                    .border(1.dp, com.example.ui.theme.BorderWhite40, RoundedCornerShape(32.dp))
                    .padding(16.dp)
            ) {
                val filteredHistoryItems = historyItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredHistoryItems, key = { it.id }) { item ->
                        HistoryItemCard(
                            item = item,
                            onAdd = { viewModel.addItem(item.name) },
                            onDelete = { viewModel.deleteItem(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: GroceryItem, onAdd: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onAdd() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.GlassWhite60),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderWhite20),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(32.dp).background(Color.White, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, tint = com.example.ui.theme.Slate600, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = com.example.ui.theme.Slate800
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete from previous orders", tint = com.example.ui.theme.Slate400)
            }
        }
    }
}

data class ShopItemInfo(val name: String, val weight: String, val price: Double, val imageUrl: String)

val shopItems = listOf(
    ShopItemInfo("Fresh Apple", "1 kg", 3.50, "https://images.unsplash.com/photo-1560806887-1e4cd0b6faa6?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Banana Robusta", "500 g", 1.20, "https://images.unsplash.com/photo-1603833665858-e61d17a86224?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Broccoli", "1 pc", 2.00, "https://images.unsplash.com/photo-1459411621453-7b03977f4bfc?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Carrot", "500 g", 1.80, "https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Tomato", "1 kg", 2.50, "https://images.unsplash.com/photo-1592924357228-91a4daadcfea?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Spinach Lettuce", "250 g", 1.50, "https://images.unsplash.com/photo-1576045057995-568f588f82fb?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Mango", "1 kg", 4.00, "https://images.unsplash.com/photo-1553279768-865429fa0078?auto=format&fit=crop&w=300&q=80"),
    ShopItemInfo("Strawberry", "250 g", 3.20, "https://images.unsplash.com/photo-1464965911861-746a04b4bca6?auto=format&fit=crop&w=300&q=80")
)

@Composable
fun ShopScreen(viewModel: GroceryViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Explore Categories",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = com.example.ui.theme.Slate800,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = "Fresh fruits and vegetables",
            style = MaterialTheme.typography.bodyMedium,
            color = com.example.ui.theme.Slate500,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val filteredShopItems = shopItems.filter { it.name.contains(searchQuery, ignoreCase = true) }
            items(filteredShopItems) { item ->
                ShopItemCard(item = item, onAdd = { viewModel.addItem(item.name) })
            }
        }
    }
}

@Composable
fun ShopItemCard(item: ShopItemInfo, onAdd: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(260.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = com.example.ui.theme.GlassWhite80),
        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.BorderWhite40),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = com.example.ui.theme.Slate800,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.weight,
                        style = MaterialTheme.typography.bodySmall,
                        color = com.example.ui.theme.Slate500
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${String.format(java.util.Locale.US, "%.2f", item.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = com.example.ui.theme.Slate800
                    )
                    
                    Button(
                        onClick = onAdd,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = com.example.ui.theme.Emerald50,
                            contentColor = com.example.ui.theme.Emerald600
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, com.example.ui.theme.Emerald600),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "ADD",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
