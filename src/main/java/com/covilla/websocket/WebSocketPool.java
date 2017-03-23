package com.covilla.websocket;

import java.util.ArrayList;
import java.util.List;

public class WebSocketPool {
	public static final List<LoginWebSocket> SOCKETS = new ArrayList<LoginWebSocket>();
	
	public static List<LoginWebSocket> getSockets() {
		return SOCKETS;
	}
	
	public static int getSocketsCount(){
		return SOCKETS.size();
	}

	public static void removeSocket(LoginWebSocket myWebSocket){
		SOCKETS.remove(myWebSocket);
	}

	public static LoginWebSocket findByCode(String code){
		for(LoginWebSocket webSocket : SOCKETS){
			if(code.equals(webSocket.getLoginCode())){
				return webSocket;
			}
		}
		return null;
	}

}
