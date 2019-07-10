/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package namedpipeplugin;
import com.rma.io.RmaFile;
import hec.heclib.dss.DSSPathname;
import hec2.model.DataLocation;
import hec2.plugin.PathnameUtilities;
import hec2.plugin.model.ModelAlternative;
import hec2.plugin.selfcontained.SelfContainedPluginAlt;
import hec2.wat.client.WatFrame;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
/**
 *
 * @author WatPowerUser
 */
public class NamedPipeAlternative extends SelfContainedPluginAlt{
    private List<DataLocation> _dataLocations = new ArrayList<>();
    private String _pluginVersion;
    private static final String DocumentRoot = "NamedPipeAlternative";
    private static final String AlternativeNameAttribute = "Name";
    private static final String AlternativeDescriptionAttribute = "Desc";
    private NamedPipePipe _altNamedPipe;
    private String _pointShapeFilePath;
    private String _terrainFilePath;
    public String getTerrainFilePath(){
        return _terrainFilePath;
    }
    public String getPointShapeFilePath(){
        return _pointShapeFilePath;
    }
    public void setTerrainFilePath(String path){
        _terrainFilePath = path;
    }
    public void setPointShapeFilePath(String path){
        _pointShapeFilePath = path;
    }
    public NamedPipeAlternative(){
        super();
        _dataLocations = new ArrayList<>();
    }
    public NamedPipeAlternative(String name){
        this();
        setName(name);
        _altNamedPipe = new NamedPipePipe(NamedPipePipeNames.getAltSenderPipeName(name),NamedPipePipeNames.getAltRecieverPipeName(name));
    }
    @Override
    public boolean saveData(RmaFile file){
        if(file!=null){
            Element root = new Element(DocumentRoot);
            root.setAttribute(AlternativeNameAttribute,getName());
            root.setAttribute(AlternativeDescriptionAttribute,getDescription());
            if(_dataLocations!=null){
                saveDataLocations(root,_dataLocations);
            }
            Document doc = new Document(root);
            return writeXMLFile(doc,file);
        }
        return false;
    }
    @Override
    protected boolean loadDocument(org.jdom.Document dcmnt) {
        if(dcmnt!=null){
            org.jdom.Element ele = dcmnt.getRootElement();
            if(ele==null){
                System.out.println("No root element on the provided XML document.");
                return false;   
            }
            if(ele.getName().equals(DocumentRoot)){
                setName(ele.getAttributeValue(AlternativeNameAttribute));
                setDescription(ele.getAttributeValue(AlternativeDescriptionAttribute));
            }else{
                System.out.println("XML document root was imporoperly named.");
                return false;
            }
            if(_dataLocations==null){
                _dataLocations = new ArrayList<>();
            }
            _dataLocations.clear();
            loadDataLocations(ele, _dataLocations);
            setModified(false);
            
            //
            return true;
        }else{
            System.out.println("XML document was null.");
            return false;
        }
    }
    public List<DataLocation> getOutputDataLocations(){
       //construct output data locations 
	return defaultDataLocations();
    }
    public List<DataLocation> getInputDataLocations(){
        //construct input data locations.
	return defaultDataLocations();
    }
    private List<DataLocation> defaultDataLocations(){
       	if(!_dataLocations.isEmpty()){
            return _dataLocations;
        }
        List<DataLocation> dlList = new ArrayList<>();
        //create a default location so that links can be initialized.
        DataLocation dloc = new DataLocation(this.getModelAlt(),"RAS_Output","RAS HDF Output");
        DataLocation dlocTerrain = new DataLocation(this.getModelAlt(),"RAS_Terrain","RAS Terrain File");
        DataLocation dlocPoints = new DataLocation(this.getModelAlt(),"Point Shapefile","Point Shapefile File");
        dlList.add(dloc);
        dlList.add(dlocTerrain);
        dlList.add(dlocPoints);
	return dlList; 
    }
    public boolean setDataLocations(List<DataLocation> dataLocations){
        boolean retval = false;
        
        //need to store terrain file and point shapefile paths
        for(DataLocation dl : dataLocations){
            if(!_dataLocations.contains(dl)){
                setModified(true);
                _dataLocations.add(dl);
                retval = true;
            }else{
                setModified(true);
                retval = true;
            }
        }
        if(retval)saveData();
	return retval;
    }
    @Override
    public boolean isComputable() {
        return true;
    }
    @Override
    public boolean compute() {
        hec2.wat.model.ComputeOptions wco = (hec2.wat.model.ComputeOptions)this.getModelAlt().getComputeOptions();
        WatFrame fr = null;
        fr = hec2.wat.WAT.getWatFrame();

        fr.addMessage("Message SENT: Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());

        NamedPipeMessageResult result = _altNamedPipe.messagePipe("Compute Event " + wco.getCurrentEventNumber() + " in Lifecycle " + wco.getCurrentLifecycleNumber());
        return result.SuccessfulCompute();
    }
    @Override
    public boolean cancelCompute() {
        return false;
    }
    @Override
    public String getLogFile() {
        return null;
    }
    @Override
    public int getModelCount() {
        return 1;
    }

}
