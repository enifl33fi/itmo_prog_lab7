package inputWorkers;

import exceptions.MaxRecursionDepthException;
import fileWorkers.ReaderFiles;
import general.GeneralVars;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InputHandler {
    private Scanner console = new Scanner(System.in);
    private boolean fromFile = false;
    private final ReaderFiles reader = new ReaderFiles();
    private final Stack<String> curExecutionFiles = new Stack<>();

    private final Stack<ArrayDeque<String>> curCommands = new Stack<>();


    public String getLine(){
        if (this.fromFile){
            if (!this.curCommands.isEmpty() || !this.curExecutionFiles.isEmpty()){
                ArrayDeque<String> lines = this.curCommands.peek();
                if (!lines.isEmpty()) {
                    return lines.poll();
                }
                this.curCommands.pop();
                this.curExecutionFiles.pop();
                if (this.curExecutionFiles.isEmpty()){
                    this.fromFile = false;
                }
                return null;
            }
        } else {
            try {
                System.out.print("-> ");
                return console.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
                System.out.println("Idk how that happened. Never mind.");
                this.console = new Scanner(System.in);
            }

        }
        return null;
    }

    public boolean isFromFile() {
        return this.fromFile;
    }

    public void setFromFile(boolean fromFile){
        this.fromFile = fromFile;
    }

    public void getCommands(String fileName){
        try {
            if (this.curExecutionFiles.search(fileName) != -1) {

                throw new MaxRecursionDepthException();
            }
            this.curExecutionFiles.push(fileName);
            ArrayDeque<String> lines = new ArrayDeque<>();
            try (InputStreamReader inputStream = new InputStreamReader(new FileInputStream(fileName))) {
                String line = this.reader.getLine(inputStream);
                while (line != null) {
                    lines.add(line);
                    line = this.reader.getLine(inputStream);
                }
                this.curCommands.push(lines);
            } catch (FileNotFoundException | SecurityException | NullPointerException e) {
                System.out.println(e.getMessage());
                System.out.println("Couldn't read given file.");
            } catch (IOException e) {
                System.out.println("Unexpected abortion. Some commands will be lost.");
            }
        }catch (MaxRecursionDepthException e) {
            System.out.println(e.getMessage());
        }
    }

    public String[] readElem() {
        String[] elemParts = new String[GeneralVars.VAR_COUNT - 3];
        int curSize = this.curCommands.peek().size();
        for (int i = 0; i < GeneralVars.VAR_COUNT - 3; i++) {
            if (this.fromFile && curSize > 0){
                elemParts[i] = this.getLine();
                curSize--;
            }
            else {
                elemParts[i] = "";
            }
        }
        return elemParts;
    }

    public ImmutablePair<String, String> signUp(){
        String secondChancePassword = "";
        System.out.println("Login can contain only word characters [a-zA-Z_0-9].");
        System.out.println("Type login:");
        String login = this.getLine();
        System.out.println("Password can't be empty.");
        System.out.println("Type password:");
        String password = this.getLine();
        while (!password.equals(secondChancePassword)){
            System.out.println("Repeat your password:");
            secondChancePassword = this.getLine();
        }
        return new ImmutablePair<>(login, password);
    }
    public ImmutablePair<String, String> signIn(){
        System.out.println("Type login:");
        String login = this.getLine();
        System.out.println("Type password:");
        String password = this.getLine();
        return new ImmutablePair<>(login, password);
    }
}

