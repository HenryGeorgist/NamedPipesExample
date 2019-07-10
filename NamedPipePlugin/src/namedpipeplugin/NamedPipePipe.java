/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namedpipeplugin;

import hec2.wat.client.WatFrame;
import java.io.RandomAccessFile;

/**
 *
 * @author Q0HECWPL
 */
public class NamedPipePipe {
    private RandomAccessFile _senderPipe;
    private RandomAccessFile _recieverPipe;
    private final String _senderPipeName;
    private final String _recieverPipeName;
    private final int _attempts = 5;
    
    public NamedPipePipe(String sender, String reciever){
        _senderPipeName = sender;
        _recieverPipeName = reciever;
    }
    private boolean createSenderPipe(){
        try{
        // Connect to the pipe
            _senderPipe = new RandomAccessFile("\\\\.\\pipe\\" + _senderPipeName, "rw");
            
        } catch (Exception e){
            //
            
            WatFrame fr = null;
            fr = hec2.wat.WAT.getWatFrame();
            fr.addMessage(e.getMessage());
            return false;
        }
        return true;
    }
    private boolean createRecieverPipe(){
        try{
        // Connect to the pipe
            _recieverPipe = new RandomAccessFile("\\\\.\\pipe\\" + _recieverPipeName, "rw");
            
        } catch (Exception e){
            //
            
            WatFrame fr = null;
            fr = hec2.wat.WAT.getWatFrame();
            fr.addMessage(e.getMessage());
            return false;
        }
        return true;
    }
    public NamedPipeMessageResult messagePipe(String message){
        NamedPipeMessageResult result = attemptMessagePipe(message);
        int j = 0;
        WatFrame fr = null;
        fr = hec2.wat.WAT.getWatFrame();
        while(!result.SuccessfulConnection()){

            try{
                Thread.sleep(250);
            }catch(Exception e){

            }
            j++;
            fr.addMessage("Retrying connections, Attempt " + (j + 1));
            result = attemptMessagePipe(message);
            if(j>_attempts){
                result.ComputeState(false);
                break;
            }
        }
        return result;
    }
    private NamedPipeMessageResult attemptMessagePipe(String message){
        WatFrame fr = null;
        NamedPipeMessageResult result = new NamedPipeMessageResult();
        fr = hec2.wat.WAT.getWatFrame();
        fr.addMessage("Requested to send message:" + message);
        try {
            boolean senderPipe = false;
            boolean recieverPipe = false;
            senderPipe = createSenderPipe();
            recieverPipe = createRecieverPipe();
            if(!senderPipe){
                if(recieverPipe){
                    _recieverPipe.close();
                    
                    return result;
                }
            }else if(!recieverPipe){
                if(senderPipe){
                    _senderPipe.close();
                    return result;
                }
            }else{
                result.ConnectionState(true);
                fr.addMessage("Connections Successful");
            }
            // write to pipe
            _senderPipe.write( message.getBytes() );
            fr.addMessage("Sent message:" + message + " to pipe " + _senderPipeName);
            _senderPipe.close();
            String response = _recieverPipe.readLine();
            _recieverPipe.close();
            fr.addMessage(response);
            if(response.contains("Compute Failed!")){
                fr.addMessage(response);
                
                return result;
            }else{
                result.ComputeState(true);
                return result;
            }
            
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fr.addMessage(e.getMessage());
          } finally{
            try{
            _senderPipe.close();
            }catch(Exception e1){
                fr.addMessage(e1.getMessage());
            }
            try{
            _recieverPipe.close();
            } catch (Exception e2){
                fr.addMessage(e2.getMessage());
            }
            
          }
        return result;
    }
}
