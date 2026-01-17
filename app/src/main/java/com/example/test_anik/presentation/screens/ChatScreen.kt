package com.example.test_anik.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
    val messageType: MessageType = MessageType.TEXT,
    val imageUrl: String? = null
)

enum class MessageType {
    TEXT, IMAGE, VOICE
}

// Chat state
data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isTyping: Boolean = false,
    val inputText: String = "",
    val isRecording: Boolean = false
)

@Composable
fun ChatScreen(
    onBackClick: () -> Unit = {}
) {
    var chatState by remember { mutableStateOf(ChatState(messages = getDummyChatMessages())) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll to bottom when new message arrives
    LaunchedEffect(chatState.messages.size) {
        if (chatState.messages.isNotEmpty()) {
            listState.animateScrollToItem(chatState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Header - This will be at the very top
        AIChatHeader(onBackClick = onBackClick)

        // Messages
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Welcome message when empty
            if (chatState.messages.isEmpty()) {
                item {
                    WelcomeMessage()
                }
            }

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
            isRecording = chatState.isRecording,
            onSend = {
                if (chatState.inputText.isNotBlank()) {
                    val newMessage = ChatMessage(
                        id = chatState.messages.size + 1,
                        text = chatState.inputText,
                        timestamp = System.currentTimeMillis(),
                        isSentByUser = true
                    )
                    chatState = chatState.copy(
                        messages = chatState.messages + newMessage,
                        inputText = ""
                    )

                    // Simulate AI response
                    coroutineScope.launch {
                        chatState = chatState.copy(isTyping = true)
                        delay(1500)
                        val response = ChatMessage(
                            id = chatState.messages.size + 1,
                            text = getAIResponse(newMessage.text),
                            timestamp = System.currentTimeMillis(),
                            isSentByUser = false
                        )
                        chatState = chatState.copy(
                            messages = chatState.messages + response,
                            isTyping = false
                        )
                    }
                }
            },
            onPhotoClick = {
                // Handle photo selection
                val newMessage = ChatMessage(
                    id = chatState.messages.size + 1,
                    text = "📷 Photo attached",
                    timestamp = System.currentTimeMillis(),
                    isSentByUser = true,
                    messageType = MessageType.IMAGE
                )
                chatState = chatState.copy(messages = chatState.messages + newMessage)
            },
            onVoiceClick = {
                chatState = chatState.copy(isRecording = !chatState.isRecording)

                if (chatState.isRecording) {
                    // Simulate voice message after recording stops
                    coroutineScope.launch {
                        delay(2000)
                        if (chatState.isRecording) {
                            chatState = chatState.copy(isRecording = false)
                            val voiceMessage = ChatMessage(
                                id = chatState.messages.size + 1,
                                text = "🎤 Voice message (0:03)",
                                timestamp = System.currentTimeMillis(),
                                isSentByUser = true,
                                messageType = MessageType.VOICE
                            )
                            chatState = chatState.copy(messages = chatState.messages + voiceMessage)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun AIChatHeader(onBackClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // Only status bar padding on header
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF6B7280)
                )
            }

            // AI Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6366F1),
                                Color(0xFF8B5CF6)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Name and status
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AI Assistant",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
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
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            // Menu button
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun WelcomeMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1).copy(alpha = 0.1f),
                            Color(0xFF8B5CF6).copy(alpha = 0.1f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "How can I help you today?",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ask me anything or share a photo",
            fontSize = 14.sp,
            color = Color(0xFF6B7280)
        )
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
                slideInVertically(
                    initialOffsetY = { 20 },
                    animationSpec = tween(300)
                )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isSentByUser)
                Arrangement.End else Arrangement.Start
        ) {
            // AI Avatar for received messages
            if (!message.isSentByUser) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF8B5CF6)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

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
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (message.isSentByUser) 16.dp else 4.dp,
                                bottomEnd = if (message.isSentByUser) 4.dp else 16.dp
                            )
                        )
                        .background(
                            if (message.isSentByUser) {
                                Color(0xFF6366F1)
                            } else {
                                Color.White
                            }
                        )
                        .then(
                            if (!message.isSentByUser) {
                                Modifier.border(
                                    1.dp,
                                    Color(0xFFE5E7EB),
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = 4.dp,
                                        bottomEnd = 16.dp
                                    )
                                )
                            } else Modifier
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    when (message.messageType) {
                        MessageType.VOICE -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (message.isSentByUser) Color.White else Color(0xFF6366F1),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = message.text,
                                    fontSize = 14.sp,
                                    color = if (message.isSentByUser) Color.White else Color(0xFF1F2937)
                                )
                            }
                        }
                        MessageType.IMAGE -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Image,
                                    contentDescription = null,
                                    tint = if (message.isSentByUser) Color.White else Color(0xFF6366F1),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = message.text,
                                    fontSize = 14.sp,
                                    color = if (message.isSentByUser) Color.White else Color(0xFF1F2937)
                                )
                            }
                        }
                        else -> {
                            Text(
                                text = message.text,
                                fontSize = 14.sp,
                                color = if (message.isSentByUser) Color.White else Color(0xFF1F2937),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Timestamp
                Text(
                    text = formatTimestamp(message.timestamp),
                    fontSize = 11.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // AI Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    val infiniteTransition = rememberInfiniteTransition(label = "dot_$index")
                    val alpha by infiniteTransition.animateFloat(
                        initialValue = 0.3f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(500, delayMillis = index * 150),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "alpha_$index"
                    )

                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF6366F1).copy(alpha = alpha))
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
    isRecording: Boolean,
    onSend: () -> Unit,
    onPhotoClick: () -> Unit,
    onVoiceClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding() // Add padding for navigation bar
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Photo button
            InputIconButton(
                icon = Icons.Default.CameraAlt,
                onClick = onPhotoClick,
                tint = Color(0xFF6B7280)
            )

            // Text field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF3F4F6))
            ) {
                if (isRecording) {
                    // Recording indicator
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val infiniteTransition = rememberInfiniteTransition(label = "recording")
                        val alpha by infiniteTransition.animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(500),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "recording_alpha"
                        )

                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Red.copy(alpha = alpha))
                        )
                        Text(
                            text = "Recording...",
                            fontSize = 14.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                } else {
                    TextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Type a message...",
                                color = Color(0xFF9CA3AF),
                                fontSize = 14.sp
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        maxLines = 4
                    )
                }
            }

            // Voice / Send button
            if (value.isBlank() && !isRecording) {
                InputIconButton(
                    icon = Icons.Default.Mic,
                    onClick = onVoiceClick,
                    tint = Color(0xFF6B7280)
                )
            } else if (isRecording) {
                InputIconButton(
                    icon = Icons.Default.Stop,
                    onClick = onVoiceClick,
                    tint = Color.Red,
                    backgroundColor = Color.Red.copy(alpha = 0.1f)
                )
            } else {
                // Send button
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6366F1))
                        .clickable(onClick = onSend),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InputIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color(0xFF6B7280),
    backgroundColor: Color = Color.Transparent
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Helper functions
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun getAIResponse(userMessage: String): String {
    val responses = listOf(
        "That's an interesting question! Let me think about it...",
        "I understand what you're asking. Here's what I think:",
        "Great point! Based on my analysis...",
        "Thanks for sharing that. Here's my response:",
        "I'd be happy to help with that!",
        "That's a thoughtful question. Let me explain...",
        "Sure, I can assist you with that.",
        "Here's what I found that might help you:"
    )
    return responses.random() + "\n\nIs there anything else you'd like to know?"
}

// Dummy data
fun getDummyChatMessages(): List<ChatMessage> {
    val now = System.currentTimeMillis()
    return listOf(
        ChatMessage(
            id = 1,
            text = "Hello! I'm your AI assistant. How can I help you today?",
            timestamp = now - 300000,
            isSentByUser = false
        ),
        ChatMessage(
            id = 2,
            text = "Hi! Can you help me understand how this app works?",
            timestamp = now - 240000,
            isSentByUser = true
        ),
        ChatMessage(
            id = 3,
            text = "Of course! This is a chat interface where you can:\n\n• Send text messages\n• Share photos using the camera icon\n• Send voice messages using the microphone\n\nFeel free to ask me anything!",
            timestamp = now - 180000,
            isSentByUser = false
        )
    )
}