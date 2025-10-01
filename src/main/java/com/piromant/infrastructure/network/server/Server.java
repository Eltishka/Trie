package com.piromant.infrastructure.network.server;

import lombok.AllArgsConstructor;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@AllArgsConstructor
public class Server implements Runnable {

    private final int port;
    private final HandlerInvoker handlerInvoker;


    @Override
    public void run() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(true);

            System.out.println("Сервер слушает порт " + port);

            while (!Thread.currentThread().isInterrupted()) {
                SocketChannel clientChannel = serverChannel.accept();
                Thread.ofVirtual().start(() -> handleAccept(clientChannel));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка работы сервера", e);
        }
    }

    private void handleAccept(SocketChannel channel) {
        try (channel) {
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            while (lengthBuffer.hasRemaining()) {
                if (channel.read(lengthBuffer) == -1) {
                    throw new EOFException("Клиент закрыл соединение (длина)");
                }
            }
            lengthBuffer.flip();
            int length = lengthBuffer.getInt();

            ByteBuffer dataBuffer = ByteBuffer.allocate(length);
            while (dataBuffer.hasRemaining()) {
                if (channel.read(dataBuffer) == -1) {
                    throw new EOFException("Клиент закрыл соединение (данные)");
                }
            }
            dataBuffer.flip();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()));
            Object request = ois.readObject();

            Object response = this.handlerInvoker.invokeHandler(request);

            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {

                oos.writeObject(response);
                oos.flush();

                byte[] respBytes = bos.toByteArray();

                ByteBuffer outBuffer = ByteBuffer.allocate(4 + respBytes.length);
                outBuffer.putInt(respBytes.length);
                outBuffer.put(respBytes);
                outBuffer.flip();

                while (outBuffer.hasRemaining()) {
                    channel.write(outBuffer);
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
