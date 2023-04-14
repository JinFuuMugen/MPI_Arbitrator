import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpConfig {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("IP address: " + ip.getHostAddress());
        System.out.println("Hostname: " + ip.getHostName());
        Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
        while (interfaceList.hasMoreElements()) {
            NetworkInterface iface = interfaceList.nextElement();
            System.out.println("Interface name: " + iface.getName());
            Enumeration<InetAddress> addressList = iface.getInetAddresses();
            while (addressList.hasMoreElements()) {
                InetAddress address = addressList.nextElement();
                System.out.println("  Address: " + address);
            }
        }
    }
}
