package sglclient.keyexchange;
/**
 * 自分が後に鍵を受け取る時呼び出す
 * 公開鍵の交換を行うクラス
 * 
 * @author nishimura
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class FromSend {
	private Socket socket;
	ServerSocket serversoc;
        //不正検知がOFFの時のコンストラクタ
	public FromSend(int roundport){
		 try {
			serversoc = new ServerSocket(roundport,100);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
        //不正検知がONの時のコンストラクタ
        public FromSend(String peerip,String serverip,int roundport){
                 try {
                    System.out.println("ソケットを生成/ServerSocketに接続を要求します");
                    System.out.println(serverip+":"+roundport);
                    serversoc = new ServerSocket(roundport);
                    socket=serversoc.accept();
                    socket.close();
                    serversoc.close();
                    
                    System.out.println("SGLサーバ："+socket.getInetAddress()+"との接続完了");//接続先アドレスを返して表示
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
                        System.out.println("ソケット生成プロセスでなんかあったぞ！");
		}     
	}
	/**
	 * 
	 * @param pk
	 * @return 受け取った公開鍵
	 */
	public String KeyExchange(BigInteger pk){
		String line = null;
		try {
			socket = serversoc.accept();
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//公開鍵を受信
			line = in.readLine();
			//System.out.println("Peer client :" + "受信:" + line);
			//公開鍵を送信
			String input = "pk:";
			input += ""+pk;
			out.println(input);
			//System.out.println("Peer client :" + "送信:" + input);
			socket.close();
			serversoc.close();
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return line;
		
	}
}
