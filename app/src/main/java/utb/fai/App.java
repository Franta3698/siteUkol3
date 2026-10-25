package utb.fai;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {
        int port = 12345;
        int maxThreads = 10;

        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Neplatný formát portu, používá se výchozí: " + port);
            }
        }

        if (args.length >= 2) {
            try {
                maxThreads = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Neplatný formát maximálního počtu vláken, používá se výchozí: " + maxThreads);
            }
        }


        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo server spuštěn na portu " + port + " s limitem " + maxThreads + " vláken.");


            while (true) {
                Socket clientSocket = serverSocket.accept();
                

                ClientThread clientThread = new ClientThread(clientSocket);

                executor.execute(clientThread);
            }
        } catch (IOException e) {
            System.err.println("Chyba serveru na portu " + port + ": " + e.getMessage());
        } finally {
            executor.shutdown();
        }
    }
}