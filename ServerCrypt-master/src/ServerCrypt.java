import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;

public class ServerCrypt {
    public static void main (String[] args) throws Exception{
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextInt();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        Path pathToPublic = Paths.get("C:\\Users\\kalki\\Desktop\\publicKey.dat");
        byte[] bytePublicKey = publicKey.getEncoded();
        Files.write(pathToPublic, bytePublicKey);
        System.out.println("Server> Key pair has been generated. Public key was sent to client. Waiting...");
        System.in.read();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
        Path pathToKey = Paths.get("C:\\Users\\kalki\\Desktop\\cryptedKey.dat");
        byte[] cryptedKey = Files.readAllBytes(pathToKey);
        SecretKey secretKey = (SecretKey)cipher.unwrap(cryptedKey, "AES", Cipher.SECRET_KEY);
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String message = "Message for encryption";
        byte[] byteMessage = message.getBytes();
        System.out.println("Server> Message to encrypt is: " + message);
        byte[] cryptedMessage = new byte[cipher.getOutputSize(byteMessage.length)];
        int ctLength = cipher.update(byteMessage, 0, byteMessage.length, cryptedMessage, 0);
        ctLength += cipher.doFinal(cryptedMessage, ctLength);
        System.out.println("Server> Encrypted message is: " + new String(cryptedMessage));
        Path pathToMessage = Paths.get("C:\\Users\\kalki\\Desktop\\message.dat");
        Files.write(pathToMessage, cryptedMessage);
    }
}
