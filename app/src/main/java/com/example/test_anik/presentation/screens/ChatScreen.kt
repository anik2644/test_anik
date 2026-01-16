package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Message data model
data class ChatMessage(
    val id: Int,
    val text: String,
    val timestamp: Long,
    val isSentByUser: Boolean,
    val isRead: Boolean = true,
    val senderName: String = "",
    val senderAvatar: String = ""
)

// Chat state
data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false,
    val inputText: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var chatState by remember { mutableStateOf(ChatState(messages = getDummyChatMessages())) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll to bottom when new message arrives
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatState.messages.size - 1)
        }
    }

    // Animated background gradient
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0F2FE),
                        Color(0xFFDDD6FE),
                        Color(0xFFFCE7F3)
                    ),
                    startY = gradientOffset,
                    endY = gradientOffset + 2000f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            ChatHeader()

            // Messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(chatState.messages) { message ->
                    MessageBubble(message = message)
                }

                // Typing indicator
                if (chatState.isTyping) {
                    item {
                        TypingIndicator()
                    }
                }
            }

            // Input Field
            ChatInputField(
                value = chatState.inputText,
                onValueChange = { chatState = chatState.copy(inputText = it) },
                onSend = {
                    if (chatState.inputText.isNotBlank()) {
                        val newMessage = ChatMessage(
                            id = chatState.messages.size + 1,
                            text = chatState.inputText,
                            timestamp = System.currentTimeMillis(),
                            isSentByUser = true,
                            senderName = "You"
                        )
                        chatState = chatState.copy(
                            messages = chatState.messages + newMessage,
                            inputText = ""
                        )

                        // Simulate response
                        coroutineScope.launch {
                            chatState = chatState.copy(isTyping = true)
                            delay(2000)
                            val response = ChatMessage(
                                id = chatState.messages.size + 1,
                                text = getRandomResponse(),
                                timestamp = System.currentTimeMillis(),
                                isSentByUser = false,
                                senderName = "Sarah"
                            )
                            chatState = chatState.copy(
                                messages = chatState.messages + response,
                                isTyping = false
                            )
                        }
                    }
                }
            )
        }

        // Floating decorative elements
        FloatingDecorations()
    }
}

@Composable
fun ChatHeader() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Avatar with online status
            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF06B6D4),
                                    Color(0xFF3B82F6)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Online indicator with pulse animation
                val scale by rememberInfiniteTransition(label = "pulse").animateFloat(
                    initialValue = 1f,
                    targetValue = 1.3f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse_scale"
                )

                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .scale(scale)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                )
            }

            // Name and status
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sarah Johnson",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981))
                    )
                    Text(
                        text = "Online",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Voice Call",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6))
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(50)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInHorizontally(
                    initialOffsetX = { if (message.isSentByUser) it else -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) +
                scaleIn(initialScale = 0.8f)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isSentByUser)
                Arrangement.End else Arrangement.Start
        ) {
            Column(
                horizontalAlignment = if (message.isSentByUser)
                    Alignment.End else Alignment.Start
            ) {
                // Message bubble
                Box(
                    modifier = Modifier
                        .widthIn(max = 280.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = if (message.isSentByUser) 20.dp else 4.dp,
                                topEnd = if (message.isSentByUser) 4.dp else 20.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                        .background(
                            if (message.isSentByUser) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF3B82F6),
                                        Color(0xFF2563EB)
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color.White
                                    )
                                )
                            }
                        )
                        .then(
                            if (!message.isSentByUser) {
                                Modifier
                            } else Modifier
                        ),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        color = if (message.isSentByUser) Color.White else Color(0xFF1F2937),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        lineHeight = 22.sp
                    )
                }

                // Timestamp
                Text(
                    text = formatTimestamp(message.timestamp),
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val infiniteTransition = rememberInfiniteTransition(label = "dot_$index")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.7f,
                        targetValue = 1.3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, delayMillis = index * 150),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale_$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(Color(0xFF94A3B8))
                    )
                }
            }
        }
    }
}

@Composable
fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Add button
            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3F4F6))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color(0xFF3B82F6)
                )
            }

            // Text field
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .weight(1f),
                placeholder = {
                    Text(
                        "Type a message...",
                        color = Color(0xFF9CA3AF),
                        fontSize = 15.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF3B82F6),
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedContainerColor = Color(0xFFF9FAFB),
                    unfocusedContainerColor = Color(0xFFF9FAFB)
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )

            // Send button with animation
            val rotation by rememberInfiniteTransition(label = "send").animateFloat(
                initialValue = 0f,
                targetValue = if (value.isNotBlank()) 360f else 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "rotation"
            )

            IconButton(
                onClick = onSend,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (value.isNotBlank()) {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF3B82F6),
                                    Color(0xFF2563EB)
                                )
                            )
                        } else {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE5E7EB),
                                    Color(0xFFE5E7EB)
                                )
                            )
                        }
                    ),
                enabled = value.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if (value.isNotBlank()) rotation else 0f)
                )
            }
        }
    }
}

@Composable
fun BoxScope.FloatingDecorations() {
    // Floating circles
    val infiniteTransition = rememberInfiniteTransition(label = "decorations")

    val offset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset1"
    )

    val offset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset2"
    )

    Box(
        modifier = Modifier
            .size(150.dp)
            .offset(x = (-30).dp, y = 100.dp + offset1.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3B82F6).copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopStart)
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .offset(x = 200.dp, y = (-50).dp + offset2.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFEC4899).copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
            .align(Alignment.TopEnd)
    )
}

// Helper functions
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun getRandomResponse(): String {
    val responses = listOf(
        "That sounds great! 😊",
        "I totally agree with you!",
        "Haha, that's funny! 😄",
        "Let me think about that...",
        "Absolutely! When should we meet?",
        "Thanks for sharing that with me!",
        "I'm so glad to hear that! 💙",
        "That's a brilliant idea!",
        "Count me in! 🎉"
    )
    return responses.random()
}

// Dummy data
fun getDummyChatMessages(): List<ChatMessage> {
    val now = System.currentTimeMillis()
    return listOf(
        ChatMessage(
            id = 1,
            text = "Hey! How's your day going?",
            timestamp = now - 3600000,
            isSentByUser = false,
            senderName = "Sarah"
        ),
        ChatMessage(
            id = 2,
            text = "Pretty good! Just working on this new chat UI. What about you?",
            timestamp = now - 3540000,
            isSentByUser = true
        ),
        ChatMessage(
            id = 3,
            text = "That's awesome! I'd love to see it when you're done 😊",
            timestamp = now - 3480000,
            isSentByUser = false,
            senderName = "Sarah"
        ),
        ChatMessage(
            id = 4,
            text = "Sure thing! I'm adding some cool animations to it",
            timestamp = now - 3420000,
            isSentByUser = true
        ),
        ChatMessage(
            id = 5,
            text = "Oh nice! Are you using Jetpack Compose for this?",
            timestamp = now - 3360000,
            isSentByUser = false,
            senderName = "Sarah"
        ),
        ChatMessage(
            id = 6,
            text = "Yes! Compose makes animations so much easier. The bubble animations turned out really smooth!",
            timestamp = now - 3300000,
            isSentByUser = true
        ),
        ChatMessage(
            id = 7,
            text = "That's what I've heard! I should really start learning Compose too 📱",
            timestamp = now - 3240000,
            isSentByUser = false,
            senderName = "Sarah"
        ),
        ChatMessage(
            id = 8,
            text = "You definitely should! The declarative approach is a game changer. Want me to send you some resources?",
            timestamp = now - 3180000,
            isSentByUser = true
        ),
        ChatMessage(
            id = 9,
            text = "That would be amazing! Thank you so much! 🙏",
            timestamp = now - 3120000,
            isSentByUser = false,
            senderName = "Sarah"
        ),
        ChatMessage(
            id = 10,
            text = "No problem! I'll compile a list of my favorite tutorials and send them over",
            timestamp = now - 3060000,
            isSentByUser = true
        )
    )
}