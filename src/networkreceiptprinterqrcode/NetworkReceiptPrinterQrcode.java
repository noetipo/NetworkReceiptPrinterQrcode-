/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networkreceiptprinterqrcode;
import java.io.*;
import java.net.*;
import java.util.HashMap;
/**
 *
 * @author noe
 */
public class NetworkReceiptPrinterQrcode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String content = "201122380";
        String printerIp = "192.168.0.25";
        int printerPort = 7654;

        try {
            Socket socket = new Socket(printerIp, printerPort);
            PrintWriter pwr  = new PrintWriter( new BufferedWriter( new OutputStreamWriter( socket.getOutputStream() )), true);
            BufferedReader br = new BufferedReader( new InputStreamReader( socket.getInputStream() ));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in;

            
            
            
            
            
            
            
            
            
            
            
            
            
            
             HashMap commands = new HashMap();
        String[] commandSequence = {"model", "size", "error", "store", "content", "print"};
        int contentLen = content.length();
        int resultLen = 0;
        byte[] command;

        // QR Code: Select the model
        //              Hex     1D      28      6B      04      00      31      41      n1(x32)     n2(x00) - size of model
        // set n1 [49 x31, model 1] [50 x32, model 2] [51 x33, micro qr code]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=140
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x04, (byte) 0x00, (byte) 0x31, (byte) 0x41, (byte) 0x32, (byte) 0x00};
        commands.put("model", command);
        resultLen += command.length;

        // QR Code: Set the size of module
        // Hex      1D      28      6B      03      00      31      43      n
        // n depends on the printer
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=141
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x43, (byte) 0x06};
        commands.put("size", command);
        resultLen += command.length;

        //          Hex     1D      28      6B      03      00      31      45      n
        // Set n for error correction [48 x30 -> 7%] [49 x31-> 15%] [50 x32 -> 25%] [51 x33 -> 30%]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=142
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x45, (byte) 0x33};
        commands.put("error", command);
        resultLen += command.length;

        // QR Code: Store the data in the symbol storage area
        // Hex      1D      28      6B      pL      pH      31      50      30      d1...dk
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=143
        //                        1D          28          6B         pL          pH  cn(49->x31) fn(80->x50) m(48->x30) d1…dk
        int storeLen = contentLen + 3;
        byte store_pL = (byte) (storeLen % 256);
        byte store_pH = (byte) (storeLen / 256);
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, store_pL, store_pH, (byte) 0x31, (byte) 0x50, (byte) 0x30};
        commands.put("store", command);
        resultLen += command.length;

        // QR Code content
        command = content.getBytes();
        commands.put("content", command);
        resultLen += command.length;

        // QR Code: Print the symbol data in the symbol storage area
        // Hex      1D      28      6B      03      00      31      51      m
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=144
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x51, (byte) 0x30};
        commands.put("print", command);
        resultLen += command.length;

        int cnt = 0;
        int commandLen = 0;
        byte[] result = new byte[resultLen];
        for (String currCommand : commandSequence) {
            command = (byte[]) commands.get(currCommand);
            commandLen = command.length;
            System.arraycopy(command, 0, result, cnt, commandLen);
            cnt += commandLen;
        }

            
            
            
            
            
            
            byte[] byteArray = result;
           
               
            in = new DataInputStream(new ByteArrayInputStream(byteArray));
           
            while (in.available() != 0) {
                out.write(in.readByte());
                out.write(in.readByte());
                               
            }
            pwr.println("hola");
            out.writeByte(0x00);
            out.flush();

            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
        /*public static byte[] qrCode(String content) {
        HashMap commands = new HashMap();
        String[] commandSequence = {"model", "size", "error", "store", "content", "print"};
        int contentLen = content.length();
        int resultLen = 0;
        byte[] command;

        // QR Code: Select the model
        //              Hex     1D      28      6B      04      00      31      41      n1(x32)     n2(x00) - size of model
        // set n1 [49 x31, model 1] [50 x32, model 2] [51 x33, micro qr code]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=140
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x04, (byte) 0x00, (byte) 0x31, (byte) 0x41, (byte) 0x32, (byte) 0x00};
        commands.put("model", command);
        resultLen += command.length;

        // QR Code: Set the size of module
        // Hex      1D      28      6B      03      00      31      43      n
        // n depends on the printer
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=141
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x43, (byte) 0x06};
        commands.put("size", command);
        resultLen += command.length;

        //          Hex     1D      28      6B      03      00      31      45      n
        // Set n for error correction [48 x30 -> 7%] [49 x31-> 15%] [50 x32 -> 25%] [51 x33 -> 30%]
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=142
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x45, (byte) 0x33};
        commands.put("error", command);
        resultLen += command.length;

        // QR Code: Store the data in the symbol storage area
        // Hex      1D      28      6B      pL      pH      31      50      30      d1...dk
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=143
        //                        1D          28          6B         pL          pH  cn(49->x31) fn(80->x50) m(48->x30) d1…dk
        int storeLen = contentLen + 3;
        byte store_pL = (byte) (storeLen % 256);
        byte store_pH = (byte) (storeLen / 256);
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, store_pL, store_pH, (byte) 0x31, (byte) 0x50, (byte) 0x30};
        commands.put("store", command);
        resultLen += command.length;

        // QR Code content
        command = content.getBytes();
        commands.put("content", command);
        resultLen += command.length;

        // QR Code: Print the symbol data in the symbol storage area
        // Hex      1D      28      6B      03      00      31      51      m
        // https://reference.epson-biz.com/modules/ref_escpos/index.php?content_id=144
        command = new byte[]{(byte) 0x1d, (byte) 0x28, (byte) 0x6b, (byte) 0x03, (byte) 0x00, (byte) 0x31, (byte) 0x51, (byte) 0x30};
        commands.put("print", command);
        resultLen += command.length;

        int cnt = 0;
        int commandLen = 0;
        byte[] result = new byte[resultLen];
        for (String currCommand : commandSequence) {
            command = (byte[]) commands.get(currCommand);
            commandLen = command.length;
            System.arraycopy(command, 0, result, cnt, commandLen);
            cnt += commandLen;
        }

        return result;
    }
    */
    
}
