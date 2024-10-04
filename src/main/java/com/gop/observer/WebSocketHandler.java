package com.gop.observer;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // 해당 객체에 세션을 담아두어 사용하게 된다.
    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();

    // 웹소켓 서버에 접속한 이후 동작하는 메소드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        CLIENTS.put(session.getId(), session);  // 세션 연결

//        CLIENTS.entrySet().forEach( arg->{
//            try {
//                CharSequence message = session.getId() + "님이 입장하셨습니다.";
//                arg.getValue().sendMessage(new TextMessage(message));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

    // 웹소켓 서버접속 종료 후 동작하는 메소드.
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        CLIENTS.entrySet().forEach( arg->{
//            try {
//                CharSequence message = session.getId() + "님이 퇴장하셨습니다.";
//                arg.getValue().sendMessage(new TextMessage(message));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        CLIENTS.remove(session.getId());        // 세션 제거
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();  //메시지를 보낸 아이디
        CLIENTS.entrySet().forEach( arg->{
            if(!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 어떤 이벤트가 발생했을 때 호출하는 메서드
    public void onEventTrigger() throws Exception {
        // 이벤트가 발생하면 모든 클라이언트에게 메시지를 보냄
        CLIENTS.entrySet().forEach( arg->{
            try {
                arg.getValue().sendMessage(new TextMessage("이벤트 발생"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}