import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class DAS {
    public static void main(String[] args) {
        int port = 0;
        int number = 0;
        DatagramSocket masterSocket;

        if (args.length != 2) {

            System.out.println("Za mało parametrów!!!");
            System.exit(1);
        }
        try {
            port = Integer.parseInt(args[0]);
            number = Integer.parseInt(args[1]);

            if (port < 1 || port > 65535) {
                throw new Log("Port poza zakresem.");
            }

            if (number < -1) {
                throw new Log("Number musi być większy lub równy -1.");
            }
        } catch (NumberFormatException e) {
            Log.log("Parametry muszą być liczbami całkowitymi.");
            System.exit(1);
        } catch (Log e){
            Log.log(e.getMessage());
            System.exit(1);
        }

        try {
            masterSocket = new DatagramSocket(port);
            masterSocket.setBroadcast(true);
            System.out.println("Wystartowano w trybie MASTER na porcie " + port);
            System.out.println("Liczba przekazana w parametrze: " + number);
            startMaster(masterSocket, port, number);
        } catch (SocketException e) {
            System.out.println("Port " + port + " już używany. Startowanie trybu SLAVE. ");
            startSlave(port, number);
        }
    }

    private static void startMaster(DatagramSocket socket, int port, int number) {
        byte[] buffer = new byte[1024];

        odbierzKomunikat();
        List<Integer> values = new ArrayList<>();

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String receivedStr = new String(packet.getData(), 0, packet.getLength()).trim();
                int receivedValue;
                receivedValue = Integer.parseInt(receivedStr);

                if (receivedValue > 0) {
                    System.out.println("Otrzymana wartość: " + receivedValue);
                    values.add(receivedValue);
                } else if (receivedValue == 0) {
                    double average = wyliczSrednia(values, number);
                    System.out.println("Otrzymana wartość: " + receivedValue);
                    System.out.println("Średnia: " + average);
                    wyslijKomunikat(socket, String.valueOf(average));
                } else if (receivedValue == -1) {
                    System.out.println("Otrzymana wartość: " + receivedValue);
                    wyslijKomunikat(socket, String.valueOf(receivedValue));
                    System.out.println("Zamykanie gniazda i kończenie pracy.");
                    socket.close();
                    System.exit(0);
                }
            } catch (IOException e) {
                System.err.println("Błąd podczas odbierania wiadomości: " + e.getMessage());
            }
        }
    }

    private static void startSlave(int port, int number) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);

            System.out.println("Gniazdo UDP utworzone na porcie: " + socket.getLocalPort());

            String message = String.valueOf(number);
            byte[] data = message.getBytes();

            InetAddress masterAddress = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(data, data.length, masterAddress, port);
            socket.send(packet);
            System.out.println("Wysłano wartość " + number + " do mastera na porcie " + port);
        } catch (IOException e) {
            System.err.println("Błąd przy wysyłaniu wiadomości do mastera: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.exit(0);
        }
    }

    private static double wyliczSrednia(List<Integer> values, int number) {
        int count = values.size();
        double sum = 0.0;

        for (int val : values) {
            sum += val;
        }

        sum += (number != 0 && number != -1) ? number : 0;
        count += (number != 0 && number != -1) ? 1 : 0;

        double average = (count == 0) ? 0.0 : sum / count;
        return Math.round(average * 10.0) / 10.0;
    }

    private static void wyslijKomunikat(DatagramSocket socket, String message) {
        try {
            byte[] data = message.getBytes();
            InetAddress broadcastAddress = InetAddress.getByName("localhost");
            DatagramPacket broadcastPacket = new DatagramPacket(data, data.length, broadcastAddress, 60000);
            socket.send(broadcastPacket);
            System.out.println("Wysłano komunikat rozgłoszeniowy: " + message);
        } catch (IOException e) {
            System.err.println("Błąd: " + e.getMessage());
        }
    }

    private static void odbierzKomunikat() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null) {
                    System.out.println("Adres rozgłoszeniowy: " + broadcast.getHostAddress());
                }
            }
        } catch (SocketException | UnknownHostException e) {
            System.err.println("Błąd podczas obliczania adresu rozgłoszeniowego: " + e.getMessage());
        }
    }
}
