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
            Creamos el server socket y realizamos el inetSocketaddress pidiendonos en este caso el numero de puerto
            cuando alguien se conecte sacamos por pantalla una aceptando conexiones y empieza a correr la clase donde esta el hilo
            que vamos a realizar.
             */
            System.out.println("Creando socket servidor");

            ServerSocket serverSocket = new ServerSocket();

            System.out.println("Realizando el bind");

            InetSocketAddress addr = new InetSocketAddress("localhost", Integer.parseInt(JOptionPane.showInputDialog("Dime el numero de puerto:")));
            serverSocket.bind(addr);
            while (true) {

                System.out.println("Aceptando conexiones");

                Socket newSocket = serverSocket.accept();
                //Llamada a la clase e iniciando el hilo
                new cliente(newSocket).start();
            }

        } catch (IOException e) {

        }
    }
}

class cliente extends Thread {
    /*
    Clase que extiende de Thread para ser un hilo, declaramos el socket y los input y output  que usaremos
    */
    private Socket socket;
    InputStream is;
    OutputStream os;

    double resultado = 0;
    String mensajeida;
    /*
    creamos el constructor
    */
    public cliente(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();

    }

    public void run() {
        /*
        Metodo run en el que entraran para enviar y recibir mensajes mas de un cliente a la vez.
        */
        try {
            /*
            Creamos un dowhile para que pueda mandar y recibir continuamente, guardamos en una variable de tipo
            double y cuenta, en un array paramentros se guardan los 3 datos enviados haciendo un split que en mi caso es un espacio.
            */
            do{
                
            byte[] mensaje = new byte[25];

            is.read(mensaje);

            Double a = Double.valueOf(mensaje[0]);

            String cuenta = new String(mensaje);

            System.out.println(cuenta);

            String[] parametros = cuenta.split(" ");

            

            /*
            Un for para leer los parametros y trabajar con ellos, en cada if sabremos la operacion que debe ejecutar
            dentro de cada if realiza la operacion guardando el resultado en una variable y manda el resultado.
            */
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
                    /*
                    En este caso de la division  guardamos el segundo parametro en una variable
                    y si este parametro es igual a 0 salta un error, si no es cero calcula normal
                    */
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
                    
                    /*
                    Guardamos el primer parametro para saber si es menor que cero, si es asi, salta 
                    */
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

        }while(true);  
            
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
