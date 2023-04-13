package server;

import dataBases.DataBasesManager;
import managers.CommandManager;
import network.requests.Request;
import network.Response;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientHandler {
    private final int serverAcceptWaitTime = 10;
    private final int serverReadWaitTime = 50;
    private final ConcurrentLinkedQueue<Socket> clients = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Pair<Socket, Request>> curRequests = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Pair<Socket, Response>> curResponses = new ConcurrentLinkedQueue<>();
    private ServerSocket server;

    private CommandManager commandManager;

    private DataBasesManager dbManager;

    public void setServer(ServerSocket server) {
        this.server = server;
    }
    public void removeClient(Socket client){
        try {
            client.close();
            this.clients.remove(client);
        } catch (IOException e) {
            System.out.println("Problems while closing the client: " + e.getMessage());
        }
    }

    public Socket getClient(){
        return this.clients.poll();
    }

    public Pair<Socket, Request> getCurRequest(){
        return this.curRequests.poll();
    }

    public Pair<Socket, Response> getCurResponse(){
        return this.curResponses.poll();
    }

    public void addClient(Socket client){
        clients.add(client);
    }


    public void acceptClient() throws IOException {

        try {
            this.server.setSoTimeout(this.serverAcceptWaitTime);
            Socket client = this.server.accept();
            this.clients.add(client);
        } catch (SocketTimeoutException ignored) {
        }
    }
    public void getRequest(Socket client) throws IOException {
        try {
            client.setSoTimeout(this.serverReadWaitTime);
            InputStream is = client.getInputStream();
            byte[] inData = new byte[2048];
            int state = is.read(inData);
            if (state == -1 || state == 0){
                this.removeClient(client);
            } else{
                Request req = SerializationUtils.deserialize(inData);
                curRequests.add(new ImmutablePair<>(client, req));
            }

        }catch (SocketTimeoutException ignored){

        }
    }

    public void getResponse(Socket client, Request req){
        Response res = this.commandManager.getCommand(req.getCommandType()).execute(req);
        this.curResponses.add(new ImmutablePair<>(client, res));
    }

    public void sendResponse(Socket client, Response res) throws IOException {
        OutputStream os = client.getOutputStream();
        byte[] outData = SerializationUtils.serialize(res);
        os.write(outData);

    }


    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setDbManager(DataBasesManager dbManager) {
        this.dbManager = dbManager;
    }
}
