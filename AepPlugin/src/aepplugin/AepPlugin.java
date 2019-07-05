/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aepplugin;
import com.rma.client.Browser;
import com.rma.client.BrowserAction;
import com.rma.factories.NewObjectFactory;
import com.rma.io.FileManagerImpl;
import hec.appInterface.AppDaddy;
import hec2.map.GraphicElement;
import hec2.model.DataLocation;
import hec2.model.ProgramOrderItem;
import hec2.plugin.action.EditAction;
import hec2.plugin.action.OutputElement;
import hec2.plugin.lang.ModelLinkingException;
import hec2.plugin.lang.OutputException;
import hec2.plugin.model.ModelAlternative;
import hec2.wat.client.WatFrame;
import hec2.wat.plugin.AbstractSelfContainedWatPlugin;
import hec2.wat.plugin.CreatableWatPlugin;
import hec2.wat.plugin.SimpleWatPlugin;
import hec2.wat.plugin.WatPluginManager;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import rma.swing.RmaImage;
/**
 *
 * @author WatPowerUser
 */
public class AepPlugin extends AbstractSelfContainedWatPlugin<AepAlternative> implements CreatableWatPlugin {
    public static final String PluginName = "AEP Plugin";
    private static final String _pluginVersion = "1.0.0";
    private static final String _pluginSubDirectory = "AEPPlugin";
    private static final String _pluginExtension = ".aep";
    private static final String _senderPipeName = "Named_Pipe_Reciever";
    private static final String _recieverPipeName = "Named_Pipe_Sender";
    private static final String _RAS_AEPExePath = "/jar/ext/NamedPipes/NamedPipes.exe";
    private RandomAccessFile _senderPipe;
    private RandomAccessFile _recieverPipe;
    
    private Process _proc;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AepPlugin p = new AepPlugin();
    }
    public AepPlugin(){
        super();
        setName(PluginName);
        setProgramOrderItem(new ProgramOrderItem(PluginName,
                "A plugin for creating AEP data from HEC-RAS results",
                false,1,"shortname","Images/fda/wsp.png"));
        WatPluginManager.register(this);
//
//        List<String> paths = new ArrayList<>();
//        paths.add(getApplicationPath());
//        paths.add("-pipeNames");
//        paths.add(_senderPipeName);
//        paths.add(_recieverPipeName);
        WatFrame fr = null;
        fr = hec2.wat.WAT.getWatFrame();
        fr.addMessage("Initalizing AEP plugin.");
//        try
//        {
//            Runtime rt = Runtime.getRuntime();
//            _proc = rt.exec(paths.toArray(new String[0]));
//            fr.addMessage("Starting: " + getApplicationPath());
//
//        }
//        catch (IOException e)
//        {
//            System.out.println("AepPlugin:IOException launching NamedPipes "+e);
//            e.printStackTrace();
//        }
    }
    @Override
    protected String getAltFileExtension() {
        return _pluginExtension;
    }
    @Override
    public String getPluginDirectory() {
        return _pluginSubDirectory;
    }
        private String getApplicationPath(){
        String workingdir = AppDaddy.getApp().getWorkingDir();
        //workingdir = RMAIO.getDirectoryFromPath(workingdir);
        workingdir = workingdir.concat(_RAS_AEPExePath);
        return workingdir;
    }
    @Override
    public String getVersion() {
        return _pluginVersion;
    }
    @Override
    public boolean saveProject() {
        boolean success = true;
        for(AepAlternative alt: _altList){
            if(!alt.saveData()){
                success = false;
                System.out.println("Alternative " + alt.getName() + " could not save");
            }
        }
        return success;
    }
    @Override
    protected AepAlternative newAlternative(String string) {
        return new AepAlternative(string);
    }
    @Override
    protected NewObjectFactory getAltObjectFactory() {
        return new AepAlternativeFactory(this);
    }
    @Override
    public List<DataLocation> getDataLocations(ModelAlternative ma, int i) {
        AepAlternative alt = getAlt(ma);
        if(alt==null)return null;
        if(DataLocation.INPUT_LOCATIONS == i){
            //input
            return alt.getInputDataLocations();
        }else{
            //ouput
            return alt.getOutputDataLocations();
        }
    }
    @Override
    public boolean setDataLocations(ModelAlternative ma, List<DataLocation> list) throws ModelLinkingException {
        AepAlternative alt = getAlt(ma);
        if(alt!=null){
            return alt.setDataLocations(list);
        }
        return true;
    }
    @Override
    public boolean compute(ModelAlternative ma) {
        AepAlternative alt = getAlt(ma);
        if(alt!=null){
            //check to see if alt has data locations, and if they have been linked.
            //check to see if alt has terrain and point shapefile paths.
            
            
            hec2.wat.model.ComputeOptions wco = (hec2.wat.model.ComputeOptions)ma.getComputeOptions();
            WatFrame fr = null;
            fr = hec2.wat.WAT.getWatFrame();
            
            fr.addMessage("Message SENT: Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());
            int j = 0;
            AepMessageResult result = messagePipe("Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());
            while(!result.SuccessfulConnection()){
                
                try{
                    Thread.sleep(250);
                }catch(Exception e){
                    
                }
                j++;
                fr.addMessage("Retrying connections, Attempt " + (j + 1));
                result = messagePipe("Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());
                if(j>5) return false;
            };
            return result.SuccessfulCompute();
        }
        return false;
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
    private AepMessageResult messagePipe(String message){
        WatFrame fr = null;
        AepMessageResult result = new AepMessageResult();
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
    @Override
    public void editAlternative(AepAlternative e) {
        WatFrame fr = null;
        fr = hec2.wat.WAT.getWatFrame();
        fr.addMessage("Requested Edit Alternative Action on AEP plugin. Alternative name: " + e._name );
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public List<GraphicElement> getGraphicElements(ModelAlternative ma) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public List<OutputElement> getOutputReports(ModelAlternative ma) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public boolean displayEditor(GraphicElement ge) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public boolean displayOutput(OutputElement oe, List<ModelAlternative> list) throws OutputException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public List<EditAction> getEditActions(ModelAlternative ma) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public void editAction(String string, ModelAlternative ma) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public boolean close(boolean bln) {
        int j = 0;
        while(!messagePipe("Exit").SuccessfulConnection()){
            j++;
            if(j>5) break;
        };
        if (_proc != null )
        {
                _proc.destroy();
                _proc = null;
        }
        return true;
    }

    @Override
    public boolean displayApplication() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
