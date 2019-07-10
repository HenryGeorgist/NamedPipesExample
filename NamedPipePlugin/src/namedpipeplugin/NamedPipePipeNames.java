/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namedpipeplugin;

/**
 *
 * @author Q0HECWPL
 */
public class NamedPipePipeNames {
    private static final String _senderPipeRootName = "Named_Pipe_Reciever";
    private static final String _recieverPipeRootName = "Named_Pipe_Sender";
    public static String getSenderPipeRootName(){
        return _senderPipeRootName;
    }
    public static String getRecieverPipeRootName(){
        return _recieverPipeRootName;
    }
    public static String getAltSenderPipeName(String altName){
        return _senderPipeRootName + "_" + altName;
    }
    public static String getAltRecieverPipeName(String altName){
        return _recieverPipeRootName + "_" + altName;
    }
}
