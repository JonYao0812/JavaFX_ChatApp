/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_ChatApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author chengzhongito
 */
public class ChatServer extends Application{
TextArea ta = new TextArea();
    TextArea text = new TextArea();
    Button sendBtn = new Button("Send");
    String msg;
    DataInputStream inputFromClient = null;
    DataOutputStream outputToClient = null;
    Thread con = new Thread();
    
    public void start(Stage primaryStage) {
    
        BorderPane root = new BorderPane();
        text.setPrefSize(200, 70);
        text.setWrapText(true);
        sendBtn.setPrefSize(60, 70);
        root.setTop(ta);
        root.setCenter(text);
        root.setRight(sendBtn);
        BorderPane.setMargin(ta, new Insets(10,10,10,10));
        BorderPane.setMargin(text, new Insets(0,0,0,10));
        BorderPane.setMargin(sendBtn, new Insets(0,10,0,10));
    
        Scene scene = new Scene(new ScrollPane(root), 560, 280);
        primaryStage.setTitle("Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    
    
    
    Thread con = new Thread( () -> {
      try {
          
        // Create a server socket
        ServerSocket serverSocket = new ServerSocket(8000);
        Platform.runLater(() ->
          ta.appendText("Server started at " + new Date() + '\n'));
  
        // Listen for a connection request
        Socket socket = serverSocket.accept();
        ta.appendText("Client joined\n");
  
        // Create data input and output streams
        DataInputStream inputFromClient = new DataInputStream(
          socket.getInputStream());
        DataOutputStream outputToClient = new DataOutputStream(
          socket.getOutputStream());
  
        while (true) {
          //Send button event handler  
            sendBtn.setOnAction(e ->{
              try {
                  msg = text.getText();
                  //Condition to close server
                    if(msg.equalsIgnoreCase("bye")){
                        ta.appendText("Server: "+ msg + "\n\nSocket Closed");
                        outputToClient.writeUTF(msg+"\n\nServer is closed.");
                        outputToClient.flush();
                        socket.close();
                    } 
                  text.clear();
                  outputToClient.writeUTF(msg);  //Output message to client 
                  outputToClient.flush();
                  ta.appendText("Server: "+ msg + '\n');
          
              } catch (IOException ex) {
                  Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          msg = inputFromClient.readUTF();  //Reading socket for inputs from client
  
          Platform.runLater(() -> {
            ta.appendText("Client: "+ msg + '\n');  //Display messages sent from client
          });
        }
      }
      catch(IOException ex) {
        ex.printStackTrace();
      }
    });
    con.start(); //Start connection thread
  }
    
    public void closeCon(){
        con.stop();
    }
 
  public static void main(String[] args) {
    launch(args);
  }
    
}

