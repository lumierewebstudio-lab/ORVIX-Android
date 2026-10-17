package com.orvix.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Bg = Color(0xFF080B10)
private val Panel = Color(0xFF151B24)
private val Panel2 = Color(0xFF202936)
private val Muted = Color(0xFF9BA7B7)
private val Green = Color(0xFF36C76A)
private val Blue = Color(0xFF4B8DFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { OrvixApp() } }
}

private val languages = listOf("English" to "EN", "Español" to "ES", "Français" to "FR", "Deutsch" to "DE", "Português" to "PT", "Italiano" to "IT", "العربية" to "AR", "हिन्दी" to "HI", "中文" to "ZH", "日本語" to "JA")
private fun tr(key: String, lang: String): String {
    val es = mapOf("Welcome back" to "Bienvenido de nuevo", "Create your account" to "Crea tu cuenta", "Log in" to "Iniciar sesión", "Create account" to "Crear cuenta", "Home" to "Inicio", "Messages" to "Mensajes", "Create" to "Crear", "Notifications" to "Notificaciones", "Profile" to "Perfil", "Today's Stories" to "Historias de hoy", "Your feed" to "Tu feed", "Settings" to "Ajustes", "Language" to "Idioma", "Search" to "Buscar", "What's on your mind?" to "¿Qué estás pensando?", "Share post" to "Publicar")
    val fr = mapOf("Welcome back" to "Bon retour", "Create your account" to "Créer votre compte", "Log in" to "Connexion", "Create account" to "Créer un compte", "Home" to "Accueil", "Messages" to "Messages", "Create" to "Créer", "Notifications" to "Notifications", "Profile" to "Profil", "Today's Stories" to "Stories du jour", "Your feed" to "Votre fil", "Settings" to "Paramètres", "Language" to "Langue", "Search" to "Rechercher", "What's on your mind?" to "À quoi pensez-vous ?", "Share post" to "Publier")
    val de = mapOf("Welcome back" to "Willkommen zurück", "Create your account" to "Konto erstellen", "Log in" to "Anmelden", "Create account" to "Konto erstellen", "Home" to "Startseite", "Messages" to "Nachrichten", "Create" to "Erstellen", "Notifications" to "Benachrichtigungen", "Profile" to "Profil", "Today's Stories" to "Stories heute", "Your feed" to "Dein Feed", "Settings" to "Einstellungen", "Language" to "Sprache", "Search" to "Suchen", "What's on your mind?" to "Was denkst du?", "Share post" to "Beitrag teilen")
    return when(lang) { "Español" -> es[key] ?: key; "Français" -> fr[key] ?: key; "Deutsch" -> de[key] ?: key; else -> key }
}

@Composable fun OrvixApp() {
    var lang by remember { mutableStateOf("English") }; var loggedIn by remember { mutableStateOf(false) }; var registering by remember { mutableStateOf(false) }; var tab by remember { mutableStateOf("Home") }; var settings by remember { mutableStateOf(false) }
    MaterialTheme(colorScheme = darkColorScheme(primary = Blue, background = Bg, surface = Panel)) {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            AnimatedContent(loggedIn, transitionSpec = { fadeIn(tween(280)) togetherWith fadeOut(tween(160)) }, label = "auth") { isLogged ->
                if (!isLogged) AuthScreen(lang, { lang = it }, registering, { registering = it }) { loggedIn = true }
                else AppShell(lang, { lang = it }, tab, { tab = it }, settings, { settings = it }) { loggedIn = false; registering = false; tab = "Home"; settings = false }
            }
        }
    }
}

@Composable fun LanguagePicker(lang: String, onLang: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) {
            Icon(Icons.Default.Language, null, Modifier.size(17.dp)); Spacer(Modifier.width(6.dp)); Text(languages.first { it.first == lang }.second, fontSize = 12.sp); Icon(Icons.Default.ExpandMore, null, Modifier.size(16.dp))
        }
        DropdownMenu(expanded, { expanded = false }, modifier = Modifier.background(Panel)) {
            languages.forEach { (name, code) -> DropdownMenuItem(text = { Text("$name ($code)", color = Color.White) }, onClick = { onLang(name); expanded = false }) }
        }
    }
}

@Composable fun AuthScreen(lang: String, onLang: (String) -> Unit, register: Boolean, onRegister: (Boolean) -> Unit, onSuccess: () -> Unit) {
    var fullName by remember { mutableStateOf("") }; var username by remember { mutableStateOf("") }; var email by remember { mutableStateOf("") }; var password by remember { mutableStateOf("") }; var confirm by remember { mutableStateOf("") }; var error by remember { mutableStateOf("") }; var showPassword by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().background(Bg).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(Modifier.fillMaxWidth().padding(top = 14.dp), horizontalArrangement = Arrangement.End) { LanguagePicker(lang, onLang) }
        Spacer(Modifier.height(22.dp))
        Box(Modifier.size(70.dp).clip(RoundedCornerShape(22.dp)).background(Panel2).border(1.dp, Color(0xFF3B4656), RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) { Text("O", color = Color.White, fontSize = 42.sp, fontWeight = FontWeight.Black) }
        Text("ORVIX", color = Color.White, fontSize = 29.sp, fontWeight = FontWeight.Black, letterSpacing = 5.sp, modifier = Modifier.padding(top = 8.dp))
        Text("Your world. Your people.", color = Muted, fontSize = 13.sp)
        Spacer(Modifier.height(22.dp))
        Text(tr(if (register) "Create your account" else "Welcome back", lang), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(14.dp))
        AnimatedVisibility(register, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
            Column {
                Field("Full name", fullName, { fullName = it }, Icons.Default.Person)
                Field("Username", username, { username = it }, Icons.Default.AlternateEmail)
            }
        }
        Field(if (register) "Email address" else "Email, username or phone", email, { email = it }, Icons.Default.Email)
        OutlinedTextField(password, { password = it }, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), singleLine = true, label = { Text("Password") }, leadingIcon = { Icon(Icons.Default.Lock, null, tint = Muted) }, trailingIcon = { IconButton(onClick = { showPassword = !showPassword }) { Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Muted) } }, visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(), colors = fieldColors())
        AnimatedVisibility(register, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) { Field("Confirm password", confirm, { confirm = it }, Icons.Default.Lock, true) }
        if (error.isNotBlank()) Text(error, color = Color(0xFFFF7777), fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
        Spacer(Modifier.height(12.dp))
        Button(onClick = {
            error = when {
                email.isBlank() || password.isBlank() -> "Enter your login details."
                register && (fullName.isBlank() || username.isBlank()) -> "Enter your full name and username."
                register && password != confirm -> "Passwords do not match."
                register && password.length < 8 -> "Password must be at least 8 characters."
                else -> ""
            }
            if (error.isEmpty()) onSuccess()
        }, modifier = Modifier.fillMaxWidth().height(51.dp), shape = RoundedCornerShape(15.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue)) { Text(tr(if (register) "Create account" else "Log in", lang), fontWeight = FontWeight.Bold) }
        TextButton(onClick = { onRegister(!register); error = "" }) { Text(if (register) "Already have an account? Log in" else "New to ORVIX? Create account", color = Color.White) }
        if (!register) TextButton(onClick = { error = "Password recovery requires account service setup." }) { Text("Forgot password?", color = Muted) }
        Spacer(Modifier.weight(1f))
        Text("Terms of Service  •  Privacy Policy", color = Muted, fontSize = 10.sp, modifier = Modifier.padding(bottom = 14.dp))
    }
}

@Composable fun fieldColors() = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = Blue, unfocusedBorderColor = Panel2, focusedLabelColor = Blue, unfocusedLabelColor = Muted, cursorColor = Blue)
@Composable fun Field(label: String, value: String, change: (String) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, password: Boolean = false) {
    OutlinedTextField(value, change, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), singleLine = true, label = { Text(label) }, leadingIcon = { Icon(icon, null, tint = Muted) }, visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None, keyboardOptions = if(label.contains("Email")) KeyboardOptions(keyboardType = KeyboardType.Email) else KeyboardOptions.Default, colors = fieldColors())
}

@Composable fun AppShell(lang: String, onLang: (String) -> Unit, tab: String, onTab: (String) -> Unit, settings: Boolean, onSettings: (Boolean) -> Unit, onLogout: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Bg)) {
        if (settings) SettingsScreen(lang, onLang, { onSettings(false) }, onLogout)
        else {
            Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) { Text("ORVIX", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp); Text(when(tab) { "Home" -> "Your world, in one place."; "Messages" -> "Stay close to your people."; "Create" -> "Share a moment."; "Notifications" -> "What's happening?"; else -> "Your space." }, color = Muted, fontSize = 11.sp) }
                IconButton(onClick = { onSettings(true) }) { Icon(Icons.Default.Settings, null, tint = Color.White) }
            }
            AnimatedContent(tab, transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(140)) }, label = "tab", modifier = Modifier.weight(1f)) { screen ->
                when(screen) { "Home" -> HomeFeed(lang); "Messages" -> MessagesScreen(lang); "Create" -> CreateScreen(lang); "Notifications" -> NotificationsScreen(lang); else -> ProfileScreen(lang) { onSettings(true) } }
            }
            NavigationBar(containerColor = Color(0xFF0D1118)) {
                listOf(Triple("Home", Icons.Default.Home, "Home"), Triple("Messages", Icons.Default.ChatBubbleOutline, "Messages"), Triple("Create", Icons.Default.AddBox, "Create"), Triple("Notifications", Icons.Default.NotificationsNone, "Notifications"), Triple("Profile", Icons.Default.PersonOutline, "Profile")).forEach { item ->
                    NavigationBarItem(selected = tab == item.first, onClick = { onTab(item.first) }, icon = { Icon(item.second, null) }, label = { Text(tr(item.third, lang), fontSize = 9.sp) }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Green, selectedTextColor = Green, indicatorColor = Panel2, unselectedIconColor = Muted, unselectedTextColor = Muted))
                }
            }
        }
    }
}

data class Post(val name: String, val handle: String, val text: String, val likes: Int, val color: Color)
@Composable fun HomeFeed(lang: String) {
    val stories = listOf("Your story", "Jerome", "Jane", "Dianne", "Eleanor", "Marvin")
    val posts = listOf(Post("Jerome Bell", "@jeromebell", "A little reminder to enjoy the small things. ✨", 128, Color(0xFF705B52)), Post("Jane Cooper", "@janecooper", "Weekend mood. Who else is ready to explore? 🌍", 84, Color(0xFF3E6A72)), Post("Dianne Russell", "@dianner", "New photos are up! Tell me your favourite. 📸", 213, Color(0xFF77617E)))
    LazyColumn(contentPadding = PaddingValues(bottom = 12.dp)) {
        item {
            Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                stories.forEachIndexed { i, name -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(54.dp)) {
                    Box(Modifier.size(52.dp).border(2.dp, if(i==0) Panel2 else Green, CircleShape).padding(3.dp).clip(CircleShape).background(listOf(Color(0xFF48515E), Color(0xFF705B52), Color(0xFF3E6A72), Color(0xFF77617E), Color(0xFF786A4C), Color(0xFF4A6E5B))[i]), contentAlignment = Alignment.Center) { Text(if(i==0) "+" else name.take(1), color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold) }
                    Text(name, color = Color.White, fontSize = 9.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
                } }
            }
            Text(tr("Your feed", lang), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 17.dp, top = 8.dp, bottom = 8.dp))
        }
        items(posts) { p ->
            var liked by remember { mutableStateOf(false) }
            Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp).clip(RoundedCornerShape(18.dp)).background(Panel).padding(13.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(40.dp).clip(CircleShape).background(p.color), contentAlignment = Alignment.Center) { Text(p.name.take(1), color = Color.White, fontWeight = FontWeight.Bold) }
                    Column(Modifier.weight(1f).padding(start = 10.dp)) { Text(p.name, color = Color.White, fontWeight = FontWeight.SemiBold); Text(p.handle, color = Muted, fontSize = 10.sp) }
                    Icon(Icons.Default.MoreHoriz, null, tint = Muted)
                }
                Spacer(Modifier.height(12.dp)); Text(p.text, color = Color(0xFFE8EDF4), fontSize = 14.sp)
                Spacer(Modifier.height(12.dp))
                Box(Modifier.fillMaxWidth().height(142.dp).clip(RoundedCornerShape(13.dp)).background(Panel2), contentAlignment = Alignment.Center) { Icon(Icons.Default.Image, null, tint = Muted, modifier = Modifier.size(36.dp)) }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                    IconButton(onClick = { liked = !liked }) { Icon(if(liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if(liked) Color(0xFFFF5578) else Muted) }
                    Text("${p.likes + if(liked) 1 else 0}", color = Muted, fontSize = 12.sp)
                    IconButton(onClick = {}) { Icon(Icons.Default.ChatBubbleOutline, null, tint = Muted) }
                    IconButton(onClick = {}) { Icon(Icons.Default.Share, null, tint = Muted) }
                    Spacer(Modifier.weight(1f)); IconButton(onClick = {}) { Icon(Icons.Default.BookmarkBorder, null, tint = Muted) }
                }
            }
        }
    }
}

data class Chat(val name: String, val preview: String, val time: String, val unread: Int, val color: Color, val online: Boolean = false)
@Composable fun MessagesScreen(lang: String) {
    val chats = listOf(Chat("Jerome Bell", "WTF 😅 it's your design?", "13:24", 1, Color(0xFF8D5F4C), true), Chat("Jane Cooper", "I love U <3 💕", "12:42", 4, Color(0xFF3B626C)), Chat("Dianne Russell", "Apple is coming to Iran 👋 I was joking 😅", "12:15", 1, Color(0xFF826C8D), true), Chat("Eleanor Pena", "he is cheating on me 😅", "11:20", 1, Color(0xFF5D7357)), Chat("Marvin McKinney", "Prospect at me wandered on extended...", "22:42", 0, Blue), Chat("Guy Hawkins", "She who arrival end how fertile enabled", "21:06", 0, Color(0xFF96785B)), Chat("Wade Warren", "Months on ye at by esteem desire warmth...", "18:26", 0, Color(0xFF4B6F77)))
    Column {
        Text(tr("Today's Stories", lang), color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 18.dp, top = 3.dp, bottom = 12.dp))
        Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(11.dp)) { listOf("You", "Jerome", "Jane", "Dianne", "Eleanor").forEachIndexed { i, n -> Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(54.dp)) { Box(Modifier.size(51.dp).border(2.dp, Green, CircleShape).padding(3.dp).clip(CircleShape).background(listOf(Color(0xFF5D6473), Color(0xFF8D5F4C), Color(0xFF3B626C), Color(0xFF826C8D), Color(0xFF5D7357))[i]), contentAlignment = Alignment.Center) { Text(if(i==0) "+" else n.take(1), color = Color.White, fontWeight = FontWeight.Bold) }; Text(n, color = Color.White, fontSize = 9.sp, modifier = Modifier.padding(top = 4.dp)) } } }
        LazyColumn(Modifier.padding(top = 12.dp)) { items(chats) { c -> Row(Modifier.fillMaxWidth().clickable { }.padding(horizontal = 18.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(46.dp).clip(CircleShape).background(c.color), contentAlignment = Alignment.Center) { Text(c.name.take(1), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold); if(c.online) Box(Modifier.align(Alignment.BottomEnd).size(11.dp).clip(CircleShape).background(Green).border(2.dp, Bg, CircleShape)) }
            Column(Modifier.weight(1f).padding(start = 12.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { Text(c.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f)); Text(c.time, color = Muted, fontSize = 10.sp) }; Text(c.preview, color = Muted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp)) }
            if(c.unread > 0) { Spacer(Modifier.width(7.dp)); Box(Modifier.size(18.dp).clip(CircleShape).background(Green), contentAlignment = Alignment.Center) { Text(c.unread.toString(), color = Color.White, fontSize = 10.sp) } }
        } } }
    }
}

@Composable fun CreateScreen(lang: String) {
    var caption by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(20.dp)) { Text(tr("Create", lang), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(14.dp)); OutlinedTextField(caption, { caption = it }, modifier = Modifier.fillMaxWidth().height(150.dp), label = { Text(tr("What's on your mind?", lang)) }, colors = fieldColors()); Spacer(Modifier.height(12.dp)); OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.AddPhotoAlternate, null); Spacer(Modifier.width(8.dp)); Text("Add photo or video") }; Spacer(Modifier.height(12.dp)); Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text(tr("Share post", lang)) }; Text("Posting and media uploads need backend integration.", color = Muted, fontSize = 11.sp, modifier = Modifier.padding(top = 10.dp)) }
}
@Composable fun NotificationsScreen(lang: String) {
    Column(Modifier.fillMaxSize().padding(20.dp)) { Text(tr("Notifications", lang), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold); listOf("Jerome Bell liked your post.", "Jane Cooper started following you.", "Dianne Russell commented: Beautiful!").forEach { Row(Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Notifications, null, tint = Green); Text(it, color = Color.White, modifier = Modifier.padding(start = 12.dp), fontSize = 13.sp) }; HorizontalDivider(color = Panel2) } }
}
@Composable fun ProfileScreen(lang: String, onSettings: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) { Box(Modifier.size(90.dp).clip(CircleShape).background(Panel2), contentAlignment = Alignment.Center) { Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(46.dp)) }; Spacer(Modifier.height(12.dp)); Text(tr("Profile", lang), color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold); Text("@yourusername", color = Muted); Spacer(Modifier.height(20.dp)); Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { listOf("Posts", "Followers", "Following").forEach { Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("0", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text(it, color = Muted, fontSize = 12.sp) } } }; Spacer(Modifier.height(24.dp)); OutlinedButton(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Settings, null); Spacer(Modifier.width(8.dp)); Text("Settings and privacy") } }
}
@Composable fun SettingsScreen(lang: String, onLang: (String) -> Unit, back: () -> Unit, logout: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) { Row(verticalAlignment = Alignment.CenterVertically) { IconButton(onClick = back) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) }; Text(tr("Settings", lang), color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold) }; Spacer(Modifier.height(18.dp)); Text(tr("Language", lang), color = Muted, fontSize = 12.sp); Spacer(Modifier.height(8.dp)); LanguagePicker(lang, onLang); Spacer(Modifier.height(24.dp)); listOf("Privacy and account visibility", "Blocked accounts", "Notifications", "Help and support").forEach { Row(Modifier.fillMaxWidth().clickable { }.padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Settings, null, tint = Muted); Text(it, color = Color.White, modifier = Modifier.padding(start = 14.dp).weight(1f)); Icon(Icons.Default.ChevronRight, null, tint = Muted) } }; Spacer(Modifier.weight(1f)); OutlinedButton(onClick = logout, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFF7777))) { Icon(Icons.Default.Logout, null); Spacer(Modifier.width(8.dp)); Text("Log out") } }
}
