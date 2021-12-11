package org.library.client;

import javafx.scene.control.Button;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.model.User;

import java.awt.print.Book;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class ToServer {
    private BufferedReader acceptMessage;
    private BufferedWriter sendMessage;
    private OutputStream out;
    private InputStream input;

    private static final String escFromServer = "esc";

    public ToServer(Socket clientSocket) {
        try {
            this.out = clientSocket.getOutputStream();
            this.input = clientSocket.getInputStream();
            acceptMessage = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            sendMessage = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void esc(OutputStream out) {
        try {
            ObjectOutput outObj = new ObjectOutputStream(out);
            outObj.writeObject(escFromServer);
            outObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized <T> void send(T o, OutputStream out) {
        try {
            ObjectOutput outObj = new ObjectOutputStream(out);
            outObj.writeObject(o);
            outObj.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static <T> T accept(InputStream in) {

        try {
            ObjectInputStream input = new ObjectInputStream(in);
            T o = (T) input.readObject();
            return o;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    /*public void chekUserInDatabase(Button button){
        try {
            String answer = acceptMessage.readLine();
            if(!answer.equals("false")){

                String[] string = answer.split(" ");
                User.currentUser = new User(string[0],string[1],string[2]);
                if(User.currentUser.getRoll().equals("admin")) {
                    NewWindow.newWindowOpen.openWindow(button, "../views/adminWindow.fxml");

                }else{
                    NewWindow.newWindowOpen.openWindow(button, "../views/userWindow.fxml");

                }
            }else{

                AlertWindow.alertWindow.alertWindow("Неверный логин или пароль");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public LinkedList<Book> allBooks() throws IOException {

        send("вывести все книги");
        LinkedList<Book> books = new LinkedList<>();

        int sizeList = Integer.parseInt(acceptMessage.readLine());
        for(int i=0;i<sizeList;i++){
            addBookInList(acceptMessage.readLine(), books);
        }

        return books;
    }

    private void addBookInList(String string, LinkedList<Book> books){
        String[] product;
        product = string.split(" ");
        books.add(new Book(Integer.parseInt(product[0]),product[1],product[2],product[3],product[4]));

    }


    public void redactionBook(int id, String name, String cost, String count, String type) {
        send("редактировать книгу");
        send(id+" "+name+" "+cost+" "+count+" "+type);
    }



    public void deleteBook(int id) {
        send("удалить книгу");
        send(String.valueOf(id));
    }

    public void addNewBookInDataBase(String name, String cost, String count, String type) {
        send("добавить книгу");
        send(name+" "+cost+" "+count+" "+type);
    }*/
}
