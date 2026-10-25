package utb.fai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket clientSocket;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {

                writer.println(inputLine);

                if ("QUIT".equalsIgnoreCase(inputLine.trim())) {
                    break;
                }
            }
        } catch (IOException e) {

            System.err.println("Chyba při komunikaci s klientem: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.err.println("Nepodařilo se uzavřít socket klienta: " + e.getMessage());
            }
        }
    }
}