package com.liyu.protocol;
import java.net.Socket;

public interface Protocol {
	public void service(Socket socket);
}
