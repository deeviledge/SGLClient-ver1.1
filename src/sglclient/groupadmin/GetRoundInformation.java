/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sglclient.groupadmin;

import java.math.BigInteger;
import sglclient.keyexchange.*;
import sglclient.keyexchange.FromSend;
import sglclient.keyexchange.KeyAgreement;
import sglclient.keyexchange.Peer;
import sglclient.keyexchange.Round;
import sglclient.keyexchange.Mandate;
import sglclient.keyexchange.KEClient;
import sglclient.keyexchange.SaveKey;
import sglclient.keyexchange.SendFrom;


import sglclient.myinformation.MyInformation;


/**
 *
 * @author tatoo
 */
class GetRoundInformation {
        private int round,myid,user_flag,PeerID,MyID;
	GroupSend gs;
        String SendToIP;
        String ReceiveFromIP;
        int SendToID;
        int ReceiveFromID;
        boolean process_flag;
	/**
	 *  コンストラクタ
	 * @param round ラウンド数
	 * @param middlekey 中間鍵
	 */
	public GetRoundInformation(int round){
		gs = new GroupSend();
		this.round = round;
                try{
                    String exchangeFileName = "src/sglclient/conf/usr/xml_files/groups/Mandate_" + gs.getGroupName() + ".xml";
                    System.out.println(gs.getGroupName());
                    //　鍵配送指令書の解析
                    Mandate m = new Mandate( exchangeFileName );
                    MyInformation mi = new MyInformation();
                    myid = Integer.parseInt(mi.getUsrID());//MyInformationから自分のIDを取得する
                    //Peerの呼び出し
                    Peer peer = m.getKeyAgreement().getPeer(myid);
                    System.out.println("クライアントの種類を判断：①Wait：②Main＆Single＆First：③Main＆Single＆Second：④Dummy:⑤Mein&Double");
                    
                    //このラウンドで自分がWaitなのかExchangeなのかを判断
                    if(peer.getRoundList( round ).getbehavior().equals("Exchange")){
                    //Exchangeの場合
                        System.out.println("I'm not wait");
                        //送信するPeerの数が一人のとき
                        if(peer.getRoundList(round).getSendTo().getListSize()==1){	
                            SendToID = peer.getRoundList(round).getSendTo().getPeerList(0).getID();	//送信する相手のIDを取得
                            SendToIP = peer.getRoundList(round).getSendTo().getPeerList(0).getIP();	//IPを取得
                            user_flag=1;
                            PeerID=SendToID;
                            System.out.println("②Main＆Single");

                        }else if(peer.getRoundList(round).getSendTo().getListSize()==2){
                            ReceiveFromID = peer.getRoundList(round).getReceiveFrom().getID();	//受信する相手のIDを取得
                            ReceiveFromIP = peer.getRoundList(round).getReceiveFrom().getIP();	//受信する相手のIPを取得
                            user_flag=2;
                            PeerID=ReceiveFromID;
                            System.out.println("⑤Main&Double");
                        }
                    }else{
                    //Waitの場合
                        user_flag=3;
                        System.out.println("I'm not wait");
                    }
                }catch(Exception e){
                    System.out.println("Error：GetRoundInformationでエラーが発生");
                }
	}
        
        public boolean CompareID(){
            process_flag=false;
            //鍵を送る相手が1人かつ送信相手より自分のIDのほうが若い場合
            if(user_flag==1){
                System.out.println("I'm single");
                if(myid<SendToID){
                    process_flag=true;
                    System.out.println("I'm first");
                }                     
            
            //鍵を送る相手が2人かつ、うちメインユーザのほうの送信相手より自分のIDのほうが若い場合
            }else if(user_flag==2){
                System.out.println("I'm double");
                if(myid<ReceiveFromID){
                    process_flag=true;
                    System.out.println("I'm first");
                }                   
            }
            return process_flag;
        }
        
        //送信先のIDを取得
        public int GetSendToID(){
            return PeerID;
        }
        
        //自分のIDを取得
        public int GetMyID(){
            return myid;
        }
}
