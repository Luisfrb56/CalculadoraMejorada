package servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ServidorV2 {

    public static void main(String[] args) {
        try {
            /*
            Creamos el socket y el bind por el que nos conectaremos al host, aceptando las conexiones.
             */
            System.out.println("Creando socket servidor");

            ServerSocket serverSocket = new ServerSocket();

            System.out.println("Realizando el bind");

            InetSocketAddress addr = new InetSocketAddress("localhost", Integer.parseInt(JOptionPane.showInputDialog("Dime el numero de puerto:")));
            serverSocket.bind(addr);
            while (true) {

                System.out.println("Aceptando conexiones");

                Socket newSocket = serverSocket.accept();

                new cliente(newSocket).start();
            }

        } catch (IOException e) {

        }
    }
}

/*
            Tras recibir la conexion y crear los parametros de Input y Output stream
            creo dos parametros uno que sea el resultado de la operación y otro el mensaje que mandaremos 
            además guardaremos cada dato del array en un mensaje de tipo byte para tratarlos de uno en uno
            los leemos con is.read y los guardamos en una variable, los datos numericos de la operacion en
            tipo float porque la división o la raiz puede dar decimales y el operador en un tipo int.
 */
class cliente extends Thread {

    private Socket socket;
    InputStream is;
    OutputStream os;

    double resultado = 0;
    String mensajeida;
    public cliente(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();

    }

    public void run() {

        try {
            byte[] mensaje = new byte[25];

            is.read(mensaje);

            Double a = Double.valueOf(mensaje[0]);

            String cuenta = new String(mensaje);

            System.out.println(cuenta);

            String[] parametros = cuenta.split(" ");

            

            for (int i = 0; i < parametros.length; i++) {
                
                if (parametros[i].equalsIgnoreCase("+")) {
                   
                    resultado =(Double.parseDouble(parametros[0]) + Double.parseDouble(parametros[2]));
                    mensajeida=String.valueOf(resultado);
                    System.out.println(resultado);
                    
                    os.write(mensajeida.getBytes());
                    

                } else if (parametros[i].equalsIgnoreCase("-")) {
                   resultado =(Double.parseDouble(parametros[0]) - Double.parseDouble(parametros[2]));
                    mensajeida=String.valueOf(resultado);
                    System.out.println(resultado);
                    
                    os.write(mensajeida.getBytes());

                } else if (parametros[i].equalsIgnoreCase("*")) {
                    resultado =(Double.parseDouble(parametros[0]) * Double.parseDouble(parametros[2]));
                    mensajeida=String.valueOf(resultado);
                    System.out.println(resultado);
                    
                    os.write(mensajeida.getBytes());
                    
                } else if (parametros[i].equalsIgnoreCase("/")) {
                    double num1=Double.parseDouble(parametros[2]);
                
                    if(num1==0){
                        
                        mensajeida="Error divisor es 0";
                        os.write(mensajeida.getBytes());
                    }else{
                        
                        resultado =(Double.parseDouble(parametros[0]) / Double.parseDouble(parametros[2]));
                        mensajeida=String.valueOf(resultado);
                        System.out.println(resultado);
                    
                    os.write(mensajeida.getBytes());
                    }
                } else if (parametros[i].equalsIgnoreCase("√")) {
                    double num2=Double.parseDouble(parametros[0]);
                    if(num2<0){
                        
                         mensajeida="Error parametro negativo";
                        os.write(mensajeida.getBytes());
                        
                    }else{
                    resultado =Math.sqrt(Double.parseDouble(parametros[0]));
                    mensajeida=String.valueOf(resultado);
                    System.out.println(resultado);
                    
                    os.write(mensajeida.getBytes());
                    }
                }

            }

            System.out.println("Enviando Resultado");

            
        } catch (IOException ex) {
            Logger.getLogger(ServidorV2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //Cerramos la conexión
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }

    /*
            ahora tratamos los operadores, cada operacion tiene un codigo con la que si el mensaje recibido es igual a uno
            de estos codigos hara una u otra y mandara el resultado. 101=suma, 102=resta, 103=multiplicación, 104=división, 105=raiz.
            Con os.write mandaremos el resultado directamente o si queremos un mensaje mas largo podemos enviar el mensajeIda pero como
            esto vamos a mostrarlo por interfaz, el resultado solo nos llega.
     */
 /*
            Ahora cerramos el socket y el servidor para terminar la conexion cliente-servidor.
     */
}
