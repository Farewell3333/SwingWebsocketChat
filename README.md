# Swing WebSocket Chat App
A simple group chat application built with Spring Boot, WebSocket (STOMP), and Swing.
## Features
- Real-time chat using WebSockets
- User list
- Message history (in memory)
## Technologies Used
- **Java 17**
- **Spring Boot 3.4.5** – Java backend framework
- **WebSocket with STOMP protocol** – real-time communication
- **SockJS** – fallback for browsers that don't fully support native WebSocket/STOMP
- **Swing** – frontend
- **Docker** - to run application with [Render](https://render.com/)
  
## Getting Started

1. Clone the repository
2. Open in your IDE
3. Run the class (`SwingWebsocketApplication`) and (`app`)
4. Type username to join chat

### 🔁 Alternatively
1. Clone the repository
2. Open in your IDE
3. Make sure Dockerfile is present
4. Deploy application on [Render](https://render.com/) as Web Service
5. Change *ws://localhost:8080* from url in class (`MyStompClient`) to render generated url
6. Run the class (`app`)
7. Type username to join chat





