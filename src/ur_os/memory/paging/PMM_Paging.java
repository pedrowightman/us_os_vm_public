/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os.memory.paging;

import ur_os.memory.MemoryAddress;
import ur_os.memory.ProcessMemoryManager;
import ur_os.memory.MemoryManagerType;
import ur_os.system.OS;
import ur_os.virtualmemory.ProcessVirtualMemoryManager;

/**
 *
 * @author super
 */
public class PMM_Paging extends ProcessMemoryManager{
    
    PageTable pt;
    PageTable vpt;
    int assignedPages;
    int loadedPages;

    public PMM_Paging(int processSize, int assignedPages) {
        this(null,processSize,assignedPages);
    }
    
    public PMM_Paging(ur_os.process.Process p, int processSize, int assignedPages){
        super(p, MemoryManagerType.PAGING,processSize);
        pt = new PageTable(processSize,assignedPages,true);
        vpt = new PageTable(processSize,assignedPages,true);
        this.assignedPages = assignedPages; //Number of frames assigned to the process
        this.loadedPages = 0; //Number of loaded pages of the process
    }
    
    public PMM_Paging(int processSize) {
        this(processSize,3);
    }

    public PMM_Paging(PMM_Paging pmm) {
        super(pmm);
        if(pmm.getType() == this.getType()){
            this.pt = new PageTable(pmm.getPT());
            this.vpt = new PageTable(pmm.getVPT());
            this.assignedPages = pmm.assignedPages;
            this.loadedPages = pmm.loadedPages;
        }else{
            System.out.println("Error - Wrong PMM parameter");
        }
    }

    public int getAssignedPages() {
        return assignedPages;
    }

    public void setAssignedPages(int assignedPages) {
        if(assignedPages > 0)
            this.assignedPages = assignedPages;
        else
            this.assignedPages = vpt.size;
    }

    public int getLoadedPages() {
        return loadedPages;
    }

    public void setLoadedPages(int loadedPages) {
        this.loadedPages = loadedPages;
    }

    
    
    public PageTable getVPT() {
        return vpt;
    }
    
    public PageTable getPT() {
        return pt;
    }
    
    public void addFrameID(int frame){
        pt.addFrameID(frame);
    }
    
    public void addFrameID(int frame, boolean valid){
        pt.addFrameID(frame, valid);
    }
    
    public void addVFrameID(int frame){
        vpt.addFrameID(frame);
    }
    
    public void addVFrameID(int frame, boolean valid){
        vpt.addFrameID(frame,valid);
    }
    
    public void setFrameID(int page, int frame){
        pt.setFrameID(page, frame);
        setPageValid(page, true);
        pt.setPageDirty(page, false);
    }
    
    public MemoryAddress getPageMemoryAddressFromLocalAddress(int locAdd){
        int page = locAdd/OS.PAGE_SIZE;
        int offset = locAdd%OS.PAGE_SIZE;
        System.out.println("Accessing Page "+page+" and offset "+offset);
        this.addMemoryAccess(page); //Add the access to this page in the sequence of memory access
        return new MemoryAddress(page, offset);
    }
    
    public int getFrameMemoryAddressFromLogicalMemoryAddress(int page){
        return getFrameMemoryAddressFromLogicalMemoryAddress(new MemoryAddress(page, 0)).getDivision();
    }
    
    public MemoryAddress getFrameMemoryAddressFromLogicalMemoryAddress(MemoryAddress m){
        int frame = pt.getFrameIdFromPage(m.getDivision());
        
        if(frame == -1){
            System.out.println("Page fault!");
            return null;
        }else if(frame == -2){
            System.out.println("ERROR in memory access - Invalid page");
            return null;
        } 
        
        return new MemoryAddress(frame, m.getOffset());
    }
    
    
    public int getVFrameMemoryAddressFromLogicalMemoryAddress(int page){
        return getVFrameMemoryAddressFromLogicalMemoryAddress(new MemoryAddress(page, 0)).getDivision();
    }
    
    public MemoryAddress getVFrameMemoryAddressFromLogicalMemoryAddress(MemoryAddress m){
        int frame = vpt.getFrameIdFromPage(m.getDivision());
        
        //No possibility of page fault because all pages are loaded into the swap area
        if(frame == -2){
            System.out.println("ERROR in memory access - Invalid page");
            return null;
        } 
        
        return new MemoryAddress(frame, m.getOffset());
    }
    
   
    
     @Override
    public String toString(){
        return pt.toString();
    }

    public int getFrameInSwap(int page) {
        return vpt.getFrameIdFromPage(page);
    }
    
    public void setPageValid(int page, boolean valid){
        pt.setPageValid(page, valid);
        if(!valid)
            this.loadedPages--;
        else
            this.loadedPages++;
    }
    
    public boolean isPageDirty(int page){
        return pt.isPageDirty(page);
    }
    
    public void setPageDirty(int page, boolean valid){
        pt.setPageDirty(page, valid);
    }
    
    
    

    @Override
    public int getVictim(){
        if(this.loadedPages == this.assignedPages)
            return pvmm.getVictim(memoryAccesses,this.loadedPages);
        else
            return -1;
    }
    
}
