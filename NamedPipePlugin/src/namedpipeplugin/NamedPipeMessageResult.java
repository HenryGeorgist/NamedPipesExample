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
public class NamedPipeMessageResult {
    private boolean _connectionSuccessful;
    private boolean _computeSuccessful;
    public NamedPipeMessageResult(){
        _connectionSuccessful = false;
        _computeSuccessful = false;
    }
    public void ConnectionState(boolean state){
        _connectionSuccessful = state;
    }
    public boolean SuccessfulConnection(){
        return _connectionSuccessful;
    }
    public void ComputeState(boolean state){
        _computeSuccessful = state;
    }
    public boolean SuccessfulCompute(){
        return _computeSuccessful;
    }
}
