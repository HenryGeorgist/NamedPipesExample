/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package namedpipeplugin;
import com.rma.factories.NewObjectFactory;
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
import hec2.wat.plugin.WatPluginManager;
import java.util.List;
/**
 *
 * @author WatPowerUser
 */
public class NamedPipePlugin extends AbstractSelfContainedWatPlugin<NamedPipeAlternative> implements CreatableWatPlugin {
    public static final String PluginName = "NamedPipe Plugin";
    private static final String _pluginVersion = "1.0.0";
    private static final String _pluginSubDirectory = "NPP";
    private static final String _pluginExtension = ".npp";

    private static final String _net_NamedPipeExePath = "/jar/ext/NamedPipes/NamedPipes.exe";
    private static final NamedPipePipe _pluginNamedPipe = new NamedPipePipe(NamedPipePipeNames.getSenderPipeRootName(),NamedPipePipeNames.getRecieverPipeRootName());
    
    private Process _proc;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NamedPipePlugin p = new NamedPipePlugin();
    }
    public NamedPipePlugin(){
        super();
        setName(PluginName);
        setProgramOrderItem(new ProgramOrderItem(PluginName,
                "A plugin for connecting via named pipes to a .net library",
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
        fr.addMessage("Initalizing NPP plugin.");
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
        workingdir = workingdir.concat(_net_NamedPipeExePath);
        return workingdir;
    }
    @Override
    public String getVersion() {
        return _pluginVersion;
    }
    @Override
    public boolean saveProject() {
        boolean success = true;
        for(NamedPipeAlternative alt: _altList){
            if(!alt.saveData()){
                success = false;
                System.out.println("Alternative " + alt.getName() + " could not save");
            }
        }
        return success;
    }
    @Override
    protected NamedPipeAlternative newAlternative(String string) {
        return new NamedPipeAlternative(string);
    }
    @Override
    protected NewObjectFactory getAltObjectFactory() {
        return new NamedPipeAlternativeFactory(this);
    }
    @Override
    public List<DataLocation> getDataLocations(ModelAlternative ma, int i) {
        NamedPipeAlternative alt = getAlt(ma);
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
        NamedPipeAlternative alt = getAlt(ma);
        if(alt!=null){
            return alt.setDataLocations(list);
        }
        return true;
    }
    @Override
    public boolean compute(ModelAlternative ma) {
        NamedPipeAlternative alt = getAlt(ma);
        if(alt!=null){
            //check to see if alt has data locations, and if they have been linked.
            //check to see if alt has terrain and point shapefile paths.
            
            
            hec2.wat.model.ComputeOptions wco = (hec2.wat.model.ComputeOptions)ma.getComputeOptions();
            WatFrame fr = null;
            fr = hec2.wat.WAT.getWatFrame();
            
            fr.addMessage("Message SENT: Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());
            
            NamedPipeMessageResult result = _pluginNamedPipe.messagePipe("Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());
            return result.SuccessfulCompute();
        }
        return false;
    }
    @Override
    public void editAlternative(NamedPipeAlternative e) {
        WatFrame fr = null;
        fr = hec2.wat.WAT.getWatFrame();
        fr.addMessage("Requested Edit Alternative Action on NPP plugin. Alternative name: " + e._name );
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
        _pluginNamedPipe.messagePipe("Exit").SuccessfulConnection();
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
