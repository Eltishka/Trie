package com.piromant.infrastructure.network.client;

import com.piromant.dto.RequestType;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    private final SocketChannel socketChannel;

    public Client(String serverAddress, int serverPort) {
        try {
            this.socketChannel = SocketChannel.open();
            this.socketChannel.connect(new InetSocketAddress(serverAddress, serverPort));
            // this.socketChannel.configureBlocking(false);
            while (!this.socketChannel.finishConnect()){}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequest(Object payload) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream outputStream = new ObjectOutputStream(bos)) {

            outputStream.writeObject(payload);
            outputStream.flush();

            byte[] data = bos.toByteArray();

            ByteBuffer buffer = ByteBuffer.allocate(4 + data.length);
            buffer.putInt(data.length);
            buffer.put(data);
            buffer.flip();

            while (buffer.hasRemaining()) {
                this.socketChannel.write(buffer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public Object receiveObject() {
        try {
            ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
            while (lengthBuffer.hasRemaining()) {
                if (socketChannel.read(lengthBuffer) == -1) {
                    throw new EOFException("Канал закрыт до получения длины");
                }
            }
            lengthBuffer.flip();
            int length = lengthBuffer.getInt();
            ByteBuffer dataBuffer = ByteBuffer.allocate(length);
            while (dataBuffer.hasRemaining()) {
                if (socketChannel.read(dataBuffer) == -1) {
                    throw new EOFException("Канал закрыт во время получения объекта");
                }
            }
            dataBuffer.flip();

            try (ObjectInputStream ois =
                         new ObjectInputStream(new ByteArrayInputStream(dataBuffer.array()))) {
                Object obj = ois.readObject();
                System.out.println(obj);
                return obj;
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка при получении объекта", e);
        }
    }

    public void close() {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
