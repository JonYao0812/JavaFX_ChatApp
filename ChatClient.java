/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JavaFX_ChatApp;

import csu_java2.chatServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
public class ChatClient extends Application{
 DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    TextArea ta = new TextArea();
    TextArea text = new TextArea();
    Button sendBtn = new Button("Send");
    String msg;
 
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
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    
    new Thread( () -> {
      try {
        //Create a socket that connects to server
        Socket socket = new Socket("127.0.0.1", 8000);
        ta.appendText("Connected to server at " + new Date()+"\n");
  
        // Create data input and output streams
        DataInputStream fromServer = new DataInputStream(
          socket.getInputStream());
        DataOutputStream toServer = new DataOutputStream(
          socket.getOutputStream());
  
        while (true) {
         //Send button event handler   
          sendBtn.setOnAction(e ->{
              try {
                  msg = text.getText();
                //Condition to leave conversation  
                  if(msg.equalsIgnoreCase("bye")){
                        ta.appendText("Client: "+ msg + "\nClient is offline");
                      //Output message to server                         
                        toServer.writeUTF(msg+"\n\nClient is offline."); 
                        toServer.flush();
                        socket.close();
                    } 
                  text.clear();
                  toServer.writeUTF(msg);  //Output message to server
                  toServer.flush();
                  ta.appendText("Client: "+ msg + '\n');
              } catch (IOException ex) {
                  Logger.getLogger(chatServer.class.getName()).log(Level.SEVERE, null, ex);
              }
          });
          msg = fromServer.readUTF();   //Reading port for inputs from server
          Platform.runLater(() -> {
            ta.appendText("Server: "+ msg + '\n');});  //Display message
          
          //if(text.getText().equalsIgnoreCase("bye")) break;
        }
      }
      catch(IOException ex) {
        ex.printStackTrace();
      }
    }).start();   //Start connection thread
    
    }
    public static void main(String[] args) {
        launch(args);
    }
}

