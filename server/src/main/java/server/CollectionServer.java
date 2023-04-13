package server;

import collections.InteractiveCollection;
import dataBases.DataBasesManager;
import managers.CommandManager;
import network.requests.Request;
import network.Response;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class CollectionServer {

    private final int port;
    private final ClientHandler clientHandler = new ClientHandler();

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private final ExecutorService acceptThread = Executors.newSingleThreadExecutor();

    private final CommandManager commandManager;

    private final DataBasesManager dbManager;
    private final InteractiveCollection curCol;


    public CollectionServer(int port, CommandManager commandManager, InteractiveCollection curCol, DataBasesManager dbManager){
        this.port = port;
        this.commandManager = commandManager;
        this.curCol = curCol;
        this.dbManager = dbManager;
    }

    public void run(){
        try(Scanner console = new Scanner(System.in)) {
            this.acceptClients();
            while (true) {
                if (System.in.available() > 0) {
                    String line = console.nextLine();
                    if (line.equals("exit")) {
                        this.acceptThread.shutdown();
                        this.fixedThreadPool.shutdown();
                        this.forkJoinPool.shutdown();
                        break;
                    }
                }
                readRequest();
                handleRequest();
                outResponse();
            }
        } catch (IOException e) {
            System.out.println("server input error: " + e.getMessage());
        } catch (NoSuchElementException e){
            System.out.println("bad input: "+ e.getMessage());
            System.exit(0);
        }
    }

    public void start(String username, String password){
        try (ServerSocket cs = new ServerSocket(this.port)) {
            this.clientHandler.setServer(cs);
            this.clientHandler.setCommandManager(this.commandManager);
            this.clientHandler.setDbManager(this.dbManager);
            this.run();
        } catch (IOException e) {
            System.out.println("can't create server: " + e.getMessage());
        }
    }

    public void acceptClients(){
        this.acceptThread.submit(() -> {
            while (!acceptThread.isShutdown()){
                try {
                    this.clientHandler.acceptClient();
                } catch (IOException e) {
                    System.out.println("error while accepting client: " + e.getMessage());
                }
            }
        });
    }

    public void readRequest(){
        Socket client = this.clientHandler.getClient();
        if (client != null) {
            this.forkJoinPool.submit(() -> {
                try {
                    this.clientHandler.getRequest(client);
                    this.clientHandler.addClient(client);
                } catch (SocketException e){
                    this.clientHandler.removeClient(client);
                } catch (IOException e) {
                    this.clientHandler.removeClient(client);
                    System.out.println("error while reading: " + e.getMessage());

                }
            });
        }
    }

    public void handleRequest(){
        Pair<Socket, Request> curRequest = this.clientHandler.getCurRequest();
        if (curRequest != null){
            Request req = curRequest.getRight();
            Socket client = curRequest.getLeft();
            new Thread(() -> {
                this.clientHandler.getResponse(client, req);
            }).start();
        }
    }

    public void outResponse(){
        Pair<Socket, Response> curResponse = this.clientHandler.getCurResponse();
        if (curResponse != null) {
            Response res = curResponse.getRight();
            Socket client = curResponse.getLeft();
            this.fixedThreadPool.submit(() -> {
                try {
                    this.clientHandler.sendResponse(client, res);
                } catch (SocketException e){
                    this.clientHandler.removeClient(client);
                } catch (IOException e) {
                    this.clientHandler.removeClient(client);
                    System.out.println("error while writing: " + e.getMessage());
                }
            });
        }
    }
}
